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

# Création du dossier logs
log_dir = 'logs'
os.makedirs(log_dir, exist_ok=True)

# Gestion des anciens logs
for log_file in os.listdir(log_dir):
    if log_file != 'latest.log':
        os.remove(os.path.join(log_dir, log_file))

# Nom du fichier log avec horodatage
log_filename = os.path.join(log_dir, f'{datetime.now().strftime("%Y%m%d_%H%M%S")}.log')
latest_log = os.path.join(log_dir, 'latest.log')

# Configuration du logging
logging.basicConfig(
    filename=log_filename, 
    level=logging.INFO, 
    format='%(asctime)s - %(levelname)s: %(message)s'
)

# Copie vers latest.log
shutil.copy2(log_filename, latest_log)

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

def main():
    gradle_executable = install_gradle()
    
    try:
        logging.info('Lancement de la compilation...')
        process = subprocess.Popen(
            [gradle_executable, 'desktop:run'], 
            stdout=subprocess.PIPE, 
            stderr=subprocess.STDOUT, 
            text=True
        )
        
        # Filtrer et afficher uniquement les messages importants
        important_keywords = ['BUILD', 'ERROR', 'FAILURE', 'SUCCESS']
        while True:
            output = process.stdout.readline()
            if output == '' and process.poll() is not None:
                break
            if output:
                # Filtre les messages importants
                if any(keyword in output for keyword in important_keywords):
                    print(output.strip())
                    logging.info(output.strip())
        
        if process.poll() != 0:
            logging.error(f'Erreur de build. Code de retour : {process.poll()}')
    except Exception as e:
        logging.error(f'Erreur lors du lancement : {e}')
        sys.exit(1)

if __name__ == '__main__':
    main()