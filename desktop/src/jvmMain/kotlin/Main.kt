@Composable
fun MainScreen() {
    var showSettings by remember { mutableStateOf(false) }
    var showGithubLogin by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }
    var showProjectManager by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Sidebar
            Sidebar(
                showProjectManager = { showProjectManager = true },
                showSettings = { showSettings = true },
                showGithubLogin = { showGithubLogin = true },
                showLogoutConfirm = { showLogoutConfirm = true }
            )

            // Zone principale
            MainContent()
        }

        // Dialogs
        if (showSettings) {
            AlertDialog(
                onDismissRequest = { showSettings = false },
                title = { Text("Paramètres") },
                text = { Text("Configuration de WhisperIDE") },
                confirmButton = {
                    Button(onClick = { showSettings = false }) {
                        Text("Fermer")
                    }
                }
            )
        }
        
        if (showGithubLogin) {
            GithubLoginDialog(
                auth = githubAuth,
                onDismiss = { showGithubLogin = false }
            )
        }
        
        if (showLogoutConfirm) {
            LogoutConfirmation(
                onConfirm = {
                    githubAuth.clearToken()
                    showLogoutConfirm = false
                },
                onDismiss = { showLogoutConfirm = false }
            )
        }
        
        if (showProjectManager) {
            ProjectDialog(
                projectManager = projectManager,
                onDismiss = { showProjectManager = false }
            )
        }
    }
}