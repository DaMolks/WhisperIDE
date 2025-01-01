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
        subprocess.Popen([gradle_executable, 'desktop:run'], shell=True)
    except Exception as e:
        print(f'Erreur lors du lancement : {e}')
        sys.exit(1)

if __name__ == '__main__':
    main()