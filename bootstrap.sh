#!/bin/bash

# Vérifier si Gradle est installé
if ! command -v gradle &> /dev/null; then
    # Créer un dossier gradle
    mkdir -p gradle
    
    # Télécharger Gradle
    wget https://services.gradle.org/distributions/gradle-8.5-bin.zip -O gradle/gradle-8.5-bin.zip
    
    # Décompresser
    unzip gradle/gradle-8.5-bin.zip -d gradle
    
    # Ajouter au PATH
    export PATH=$PATH:$(pwd)/gradle/gradle-8.5/bin
fi

# Lancer l'application
gradle desktop:run