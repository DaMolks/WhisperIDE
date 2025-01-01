#!/usr/bin/env python3
import os
import sys
import subprocess
import platform
import urllib.request
import zipfile

def install_gradle():
    gradle_url = 'https://services.gradle.org/distributions/gradle-8.5-bin.zip'
    gradle_dir = os.path.join(os.getcwd(), 'gradle')
    gradle_zip = os.path.join(gradle_dir, 'gradle.zip')
    gradle_install_dir = os.path.join(gradle_dir, 'gradle-8.5')
    
    # Créer le répertoire si nécessaire
    os.makedirs(gradle_dir, exist_ok=True)
    
    # Télécharger Gradle
    print('Téléchargement de Gradle...')
    urllib.request.urlretrieve(gradle_url, gradle_zip)
    
    # Décompresser
    with zipfile.ZipFile(gradle_zip, 'r') as zip_ref:
        zip_ref.extractall(gradle_dir)
    
    return gradle_install_dir

def find_gradle():
    # Chercher Gradle dans le répertoire courant
    for root, dirs, files in os.walk(os.getcwd()):
        if 'gradle.bat' in files or 'gradle' in files:
            return os.path.join(root, 'bin')
    return None

def main():
    # Chercher Gradle
    gradle_path = find_gradle()
    
    if not gradle_path:
        print('Gradle non trouvé. Installation en cours...')
        gradle_path = os.path.join(install_gradle(), 'bin')
    
    # Ajouter Gradle au PATH
    os.environ['PATH'] = gradle_path + os.pathsep + os.environ['PATH']
    
    # Lancer l'application
    try:
        subprocess.run(['gradle', 'desktop:run'], check=True, shell=True)
    except subprocess.CalledProcessError as e:
        print(f'Erreur lors du lancement : {e}')
        sys.exit(1)

if __name__ == '__main__':
    main()