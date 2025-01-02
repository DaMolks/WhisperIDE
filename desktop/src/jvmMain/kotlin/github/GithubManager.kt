package github

import java.time.LocalDate

class GithubManager(private val auth: GithubAuth = githubAuth) {
    fun initializeGithub(token: String, expirationDate: LocalDate): Boolean {
        return try {
            // TODO: Vérifier la validité du token avec l'API GitHub
            auth.setCredentials(token, expirationDate)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun disconnectGithub() {
        auth.clearCredentials()
    }

    fun isTokenValid(): Boolean {
        if (!auth.isAuthenticated()) return false

        val remainingDays = auth.getRemainingDays() ?: return false
        if (remainingDays <= 0) {
            auth.clearCredentials()
            return false
        }

        return true
    }

    fun getToken(): String? {
        return if (isTokenValid()) auth.getToken() else null
    }

    fun checkTokenExpiration() {
        if (!auth.isAuthenticated()) return

        val daysRemaining = auth.getRemainingDays() ?: return
        if (daysRemaining <= 0) {
            disconnectGithub()
        }
    }
}