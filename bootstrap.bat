@echo off

# Vérifier si Gradle est installé
where gradle >nul 2>nul
if %ERRORLEVEL% neq 0 (
    # Télécharger Gradle
    mkdir gradle
    powershell -Command "Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-8.5-bin.zip' -OutFile 'gradle/gradle-8.5-bin.zip'"
    
    # Décompresser
    powershell -Command "Expand-Archive -Path 'gradle/gradle-8.5-bin.zip' -DestinationPath 'gradle'"
    
    # Ajouter au PATH
    set "PATH=%PATH%;%CD%\gradle\gradle-8.5\bin"
)

# Lancer l'application
gradle desktop:run