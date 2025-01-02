package github

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.time.LocalDate
import java.io.File

class GithubManager(private val auth: GithubAuth = githubAuth) {
    private val client = HttpClient(CIO)

    suspend fun initializeGithub(token: String, expirationDate: LocalDate): Boolean {
        return try {
            // Vérifie le token avec l'API GitHub
            val response = client.get("https://api.github.com/user") {
                headers {
                    append(HttpHeaders.Authorization, "token $token")
                    append(HttpHeaders.Accept, "application/vnd.github.v3+json")
                    append(HttpHeaders.UserAgent, "WhisperIDE")
                }
            }

            if (response.status == HttpStatusCode.OK) {
                auth.setCredentials(token, expirationDate)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    fun syncFiles(files: List<File>) {
        // TODO: Implémenter la synchronisation avec GitHub
        println("Synchronisation de ${files.size} fichiers...")
    }
}