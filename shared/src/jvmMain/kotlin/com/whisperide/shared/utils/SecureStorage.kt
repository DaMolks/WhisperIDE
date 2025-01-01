package com.whisperide.shared.utils

import java.io.File
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

class SecureStorage(private val storageDir: File) {
    private val algorithm = "AES/CBC/PKCS5Padding"
    private val salt = "WhisperIDE".toByteArray() // Devrait être unique par installation
    
    init {
        storageDir.mkdirs()
    }
    
    private fun getKey(password: String): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }
    
    fun saveSecurely(key: String, value: String) {
        val file = File(storageDir, "$key.enc")
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, getKey(key), IvParameterSpec(iv))
        
        val encrypted = cipher.doFinal(value.toByteArray())
        val combined = iv + encrypted
        
        file.writeBytes(combined)
    }
    
    fun readSecurely(key: String): String? {
        val file = File(storageDir, "$key.enc")
        if (!file.exists()) return null
        
        val combined = file.readBytes()
        val iv = combined.sliceArray(0..15)
        val encrypted = combined.sliceArray(16 until combined.size)
        
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, getKey(key), IvParameterSpec(iv))
        
        return String(cipher.doFinal(encrypted))
    }
    
    fun remove(key: String) {
        File(storageDir, "$key.enc").delete()
    }
}
