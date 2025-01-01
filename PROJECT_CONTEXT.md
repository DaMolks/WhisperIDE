# WhisperIDE - Contexte du Projet

## 🚀 Vue d'Ensemble
WhisperIDE est un environnement de développement intégré (IDE) innovant pour Android, conçu pour offrir une expérience de développement améliorée par l'IA.

## 📐 Architecture Technique
- **Langage**: Kotlin Multiplatform
- **UI Framework**: Compose Desktop
- **Build System**: Gradle
- **Packaging**: GraalVM
- **AI Integration**: Ollama

## 🛠 Structure du Projet
```
WhisperIDE/
├── shared/           # Logique métier partagée
├── desktop/          # Interface et composants desktop
│   └── src/
│       └── jvmMain/
│           ├── kotlin/
│           │   ├── ui/           # Composants d'interface
│           │   ├── github/       # Gestion GitHub
│           │   └── core/         # Logiques centrales
└── gradle/           # Configuration Gradle
```

## 🔍 Dernières Modifications Significatives
### FileExplorer.kt
- Ajout d'une synchronisation GitHub améliorée
- Gestion des états de synchronisation
- Amélioration de l'alignement UI

## 🚧 Points d'Attention
### Zones à Améliorer
- Implémentation complète de la synchronisation GitHub
- Gestion des erreurs dans les interactions GitHub
- Optimisation des performances de l'explorateur de fichiers

### Limitations Connues
- Synchronisation GitHub basique
- Gestion d'erreurs minimale
- Support multiplateforme à consolider

## 🧩 Dépendances Clés
- Kotlin 1.9.0
- Compose Desktop 1.5.10
- Kotlinx Coroutines
- JGit pour interactions GitHub

## 🎯 Vision Produit
Créer un IDE Android intelligent qui :
- Facilite le développement mobile
- Intègre l'IA de manière transparente
- Offre une expérience utilisateur fluide
- Simplifie la gestion de projet

## 🛤️ Roadmap Potentielle
1. Amélioration de l'intégration GitHub
2. Développement des fonctionnalités d'IA
3. Optimisation des performances
4. Extension du support multiplateforme

## 🔧 Configuration de Développement
### Prérequis
- JDK 17+
- Kotlin 1.9.0
- Gradle 8.5

### Commandes Principales
```bash
# Compiler le projet
./gradlew build

# Lancer l'application desktop
./gradlew desktop:run
```

## 📌 Conventions de Développement
- Suivre les conventions Kotlin
- Documenter les nouvelles fonctionnalités
- Écrire des tests unitaires
- Utiliser des imports explicites
- Gestion des erreurs explicite

## 🤝 Contribution
Consulter le fichier CONTRIBUTING.md pour les détails sur la contribution au projet.

## 📞 Contact
Pour plus d'informations, contacter le mainteneur principal.
