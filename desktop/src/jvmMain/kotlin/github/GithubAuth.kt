package github

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class GithubAuth {
    private var _token = mutableStateOf<String?>(null)
    val token by _token
        
    fun updateToken(newToken: String) {
        // TODO: Ajouter le chiffrement du token
        _token.value = newToken
    }
    
    fun clearToken() {
        _token.value = null
    }
    
    fun isAuthenticated() = token != null
}
