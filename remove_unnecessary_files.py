import os

files_to_remove = [
    'gradlew',
    'gradlew.bat',
    'bootstrap.bat',
    'bootstrap.sh'
]

for file in files_to_remove:
    try:
        os.remove(file)
        print(f'Suppression de {file}')
    except FileNotFoundError:
        print(f'{file} non trouvé, déjà supprimé')