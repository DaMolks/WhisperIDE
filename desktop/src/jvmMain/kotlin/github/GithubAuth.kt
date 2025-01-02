package github

import java.time.LocalDate

class GithubAuth {
    private var token: String? = null
    private var _expirationDate: LocalDate? = null
    val expirationDate: LocalDate? get() = _expirationDate

    fun setCredentials(newToken: String, expiration: LocalDate) {
        token = newToken
        _expirationDate = expiration
    }

    fun clearCredentials() {
        token = null
        _expirationDate = null
    }

    fun isAuthenticated(): Boolean = token != null

    fun getToken(): String? = token

    fun getRemainingDays(): Long? {
        return _expirationDate?.let { expDate ->
            val today = LocalDate.now()
            java.time.temporal.ChronoUnit.DAYS.between(today, expDate)
        }
    }
}