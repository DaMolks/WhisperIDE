package github

class GithubManager(private val auth: GithubAuth = githubAuth) {
    fun initializeGithub(token: String) {
        // TODO: Implémenter l'initialisation GitHub
    }

    fun disconnectGithub() {
        auth.clearCredentials()
    }
}