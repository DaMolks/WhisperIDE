package github

class GithubManager(private val auth: GithubAuth) {
    private var currentRepository: String? = null
    
    fun setCurrentRepository(repo: String) {
        currentRepository = repo
    }
    
    suspend fun createRepository(name: String, isPrivate: Boolean = false) {
        // TODO: Implémenter la création de repo via l'API GitHub
    }
    
    suspend fun syncFiles(localPath: String, remotePath: String) {
        // TODO: Implémenter la synchronisation des fichiers
    }
    
    suspend fun getRepositories(): List<String> {
        // TODO: Récupérer la liste des dépôts
        return emptyList()
    }
}
