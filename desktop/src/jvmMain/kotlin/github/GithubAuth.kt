package github

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class GithubAuth {
    var token by mutableStateOf<String?>(null)
        private set
        
    fun setToken(newToken: String) {
        // TODO: Ajouter le chiffrement du token
        token = newToken
    }
    
    fun clearToken() {
        token = null
    }
    
    fun isAuthenticated() = token != null
}
