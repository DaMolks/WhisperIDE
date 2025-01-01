#!/usr/bin/env python3
import os
import sys
import subprocess
import platform

def install_gradle():
    # Téléchargement et installation de Gradle
    gradle_url = 'https://services.gradle.org/distributions/gradle-8.5-bin.zip'
    os_name = platform.system().lower()
    
    if os_name == 'windows':
        subprocess.run(['powershell', '-Command', f'Invoke-WebRequest -Uri {gradle_url} -OutFile gradle.zip'], check=True)
        subprocess.run(['powershell', '-Command', 'Expand-Archive -Path gradle.zip -DestinationPath .'], check=True)
    else:
        subprocess.run(['wget', gradle_url, '-O', 'gradle.zip'], check=True)
        subprocess.run(['unzip', 'gradle.zip'], check=True)

def main():
    try:
        # Vérifier si Gradle est installé
        subprocess.run(['gradle', '--version'], capture_output=True, text=True)
    except FileNotFoundError:
        print('Gradle non trouvé. Installation en cours...')
        install_gradle()
    
    # Lancer l'application
    subprocess.run(['gradle', 'desktop:run'], check=True)

if __name__ == '__main__':
    main()