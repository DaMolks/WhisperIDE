package github

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.whisperide.shared.utils.SecureStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GithubAuth {
    private val storage = SecureStorage(File(System.getProperty("user.home"), ".whisperide"))
    private var _token = mutableStateOf<String?>(null)
    private var _expirationDate = mutableStateOf<LocalDateTime?>(null)
    
    val token by _token
    val expirationDate by _expirationDate
    
    init {
        // Charger le token existant
        val savedToken = storage.readSecurely("github_token")
        if (savedToken != null) {
            val parts = savedToken.split("|") 
            if (parts.size == 2) {
                _token.value = parts[0]
                _expirationDate.value = LocalDateTime.parse(parts[1])
            }
        }
    }
    
    suspend fun updateToken(newToken: String, expiryDate: LocalDateTime? = null) = withContext(Dispatchers.IO) {
        try {
            // Vérifier la validité du token
            val (isValid, expiry) = validateToken(newToken)
            if (!isValid) {
                throw IllegalArgumentException("Token GitHub invalide")
            }
            
            // Stocker le token et sa date d'expiration
            _token.value = newToken
            _expirationDate.value = expiry ?: expiryDate
            
            // Sauvegarder de manière sécurisée
            val expiryStr = _expirationDate.value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) ?: ""
            storage.saveSecurely("github_token", "$newToken|$expiryStr")
            
            true
        } catch (e: Exception) {
            _token.value = null
            _expirationDate.value = null
            storage.remove("github_token")
            false
        }
    }
    
    private suspend fun validateToken(token: String): Pair<Boolean, LocalDateTime?> = withContext(Dispatchers.IO) {
        try {
            val url = java.net.URL("https://api.github.com/user")
            val connection = url.openConnection() as java.net.HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $token")
            
            val response = connection.responseCode
            if (response == 200) {
                // Essayer d'extraire la date d'expiration des en-têtes
                val expiry = connection.headerFields["github-authentication-token-expiration"]?.firstOrNull()?.let { expiryStr ->
                    try {
                        Instant.parse(expiryStr).atZone(ZoneId.systemDefault()).toLocalDateTime()
                    } catch (e: Exception) {
                        null
                    }
                }
                Pair(true, expiry)
            } else {
                Pair(false, null)
            }
        } catch (e: Exception) {
            Pair(false, null)
        }
    }
    
    fun clearToken() {
        _token.value = null
        _expirationDate.value = null
        storage.remove("github_token")
    }
    
    fun isAuthenticated() = token != null
}
