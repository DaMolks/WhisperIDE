#!/usr/bin/env python3
import os
import sys
import subprocess
import urllib.request
import zipfile

def install_gradle():
    gradle_url = 'https://services.gradle.org/distributions/gradle-8.5-bin.zip'
    gradle_dir = os.path.join(os.getcwd(), 'gradle')
    gradle_zip = os.path.join(gradle_dir, 'gradle.zip')
    gradle_install_dir = os.path.join(gradle_dir, 'gradle-8.5')
    
    os.makedirs(gradle_dir, exist_ok=True)
    
    print('Téléchargement de Gradle...')
    urllib.request.urlretrieve(gradle_url, gradle_zip)
    
    with zipfile.ZipFile(gradle_zip, 'r') as zip_ref:
        zip_ref.extractall(gradle_dir)
    
    return os.path.join(gradle_install_dir, 'bin', 'gradle.bat')

def main():
    gradle_executable = install_gradle()
    
    try:
        # Options de débogage Gradle
        debug_options = [
            '--info',        # Informations détaillées
            '--stacktrace',  # Trace des erreurs
            '--scan',        # Rapport de build
            '--debug'        # Niveau de débogage maximal
        ]
        
        # Commande de lancement avec options de débogage
        full_command = [gradle_executable, 'desktop:run'] + debug_options
        
        # Lancement avec affichage des logs
        process = subprocess.Popen(
            full_command, 
            stdout=subprocess.PIPE, 
            stderr=subprocess.STDOUT, 
            text=True,
            shell=True
        )
        
        # Afficher les logs en temps réel
        while True:
            output = process.stdout.readline()
            if output == '' and process.poll() is not None:
                break
            if output:
                print(output.strip())
        
        # Vérifier le code de retour
        if process.poll() != 0:
            print(f'Erreur de build. Code de retour : {process.poll()}')
    
    except Exception as e:
        print(f'Erreur lors du lancement : {e}')
        sys.exit(1)

if __name__ == '__main__':
    main()