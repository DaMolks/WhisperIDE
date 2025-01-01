#!/usr/bin/env python3
import os
import sys
import subprocess
import urllib.request
import zipfile
import logging
import winreg
from datetime import datetime
import shutil
import signal
import threading
import queue

# Création du dossier logs
log_dir = 'logs'
os.makedirs(log_dir, exist_ok=True)

# Nom du fichier log avec horodatage
log_timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
log_filename = os.path.join(log_dir, f'{log_timestamp}.log')
latest_log = os.path.join(log_dir, 'latest.log')

# Configuration du logging
logging.basicConfig(
    level=logging.INFO,
    handlers=[
        logging.FileHandler(log_filename),
        logging.FileHandler(latest_log),
        logging.StreamHandler(sys.stdout)
    ],
    format='%(asctime)s - %(levelname)s: %(message)s'
)

# Variable globale pour gérer le processus
process = None
log_queue = queue.Queue()

def enqueue_output(out, queue):
    for line in iter(out.readline, b''):
        queue.put(line.decode('utf-8').strip())
    out.close()

def signal_handler(sig, frame):
    global process
    print('\nInterruption détectée. Arrêt du processus...')
    if process:
        process.terminate()
        process.wait()
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)

def add_to_path(path):
    try:
        key = winreg.OpenKey(winreg.HKEY_CURRENT_USER, r"Environment", 0, winreg.KEY_ALL_ACCESS)
        path_value, _ = winreg.QueryValueEx(key, "PATH")
        
        if path not in path_value.split(os.pathsep):
            new_path = path_value + os.pathsep + path
            winreg.SetValueEx(key, "PATH", 0, winreg.REG_EXPAND_SZ, new_path)
            winreg.CloseKey(key)
            logging.info(f"Ajout de {path} au PATH")
    except Exception as e:
        logging.error(f"Erreur lors de l'ajout au PATH : {e}")

def install_gradle():
    gradle_url = 'https://services.gradle.org/distributions/gradle-8.5-bin.zip'
    gradle_dir = os.path.join(os.getcwd(), 'gradle')
    gradle_zip = os.path.join(gradle_dir, 'gradle.zip')
    gradle_install_dir = os.path.join(gradle_dir, 'gradle-8.5')
    gradle_bin = os.path.join(gradle_install_dir, 'bin')
    
    os.makedirs(gradle_dir, exist_ok=True)
    
    logging.info('Téléchargement de Gradle...')
    urllib.request.urlretrieve(gradle_url, gradle_zip)
    
    logging.info('Décompression de Gradle...')
    with zipfile.ZipFile(gradle_zip, 'r') as zip_ref:
        zip_ref.extractall(gradle_dir)
    
    add_to_path(gradle_bin)
    
    return os.path.join(gradle_bin, 'gradle.bat')

def process_logs():
    while True:
        try:
            line = log_queue.get(timeout=0.1)
            print(line)
            logging.info(line)
        except queue.Empty:
            if process and process.poll() is not None:
                break

def main():
    global process
    gradle_executable = install_gradle()
    
    try:
        logging.info('Lancement de la compilation...')
        process = subprocess.Popen(
            [gradle_executable, 'desktop:run', '--info'], 
            stdout=subprocess.PIPE, 
            stderr=subprocess.STDOUT, 
            text=False  # Pas de texte ici pour un meilleur contrôle de l'encodage
        )
        
        # Thread pour collecter les logs
        log_thread = threading.Thread(target=enqueue_output, args=(process.stdout, log_queue))
        log_thread.daemon = True
        log_thread.start()
        
        # Traitement des logs dans le thread principal
        process_logs()
        
        if process.poll() != 0:
            logging.error(f'Erreur de build. Code de retour : {process.poll()}')
    except Exception as e:
        logging.error(f'Erreur lors du lancement : {e}')
        sys.exit(1)

if __name__ == '__main__':
    main()