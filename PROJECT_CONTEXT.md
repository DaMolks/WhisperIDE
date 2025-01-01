# WhisperIDE - Contexte du Projet

## 🚀 Vue d'Ensemble
WhisperIDE est un environnement de développement intégré (IDE) innovant pour Android, conçu pour offrir une expérience de développement améliorée par l'IA.

## 🗔 Refonte UI - Stratégie

### Objectifs Principaux
- Centrer l'expérience utilisateur sur l'interaction IA
- Rendre l'IDE secondaire par rapport à l'assistant conversationnel
- Créer une interface modulaire et flexible

### Composants Actuels à Refondre
1. `MainScreen.kt` : Point d'entrée principal de l'UI
2. `Sidebar.kt` : Navigation latérale
3. `FileExplorer.kt` : Gestion des fichiers
4. `MainContent.kt` : Contenu principal

### Principes de Conception
- Interface conversationnelle en premier plan
- Outils IDE accessibles mais non intrusifs
- Support du mode jour/nuit
- Modularité maximale

### Modifications Prévues
- Nouveau design centré sur le chat IA
- Menu latéral escamotable
- Thème sombre par défaut
- Système de gestion de thème dynamique

## 🛠 Étapes de Mise en Œuvre
1. Créer un nouveau composant de layout principal
2. Développer un système de thème flexible
3. Implémenter un menu latéral modulaire
4. Intégrer le mode jour/nuit

## 🚧 Points d'Attention
- Garder la logique métier existante
- Minimiser les modifications dans les composants de logique
- Assurer la compatibilité avec les fonctionnalités existantes

## 📅 Prochaines Étapes
- Concevoir le nouveau layout
- Implémenter le gestionnaire de thème
- Tester l'intégration des composants