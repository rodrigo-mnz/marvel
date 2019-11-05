package com.rcaetano.marvelheroes.data

import com.rcaetano.marvelheroes.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.and
import java.io.UnsupportedEncodingException

class RepositoryImpl(
    private val apiService: ApiService
) : Repository {

    override suspend fun listCharacters() =
        withContext(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis().toString()
            apiService.listCharacters(
                buildHash(timestamp),
                timestamp
            )
        }

    private fun buildHash(timestamp: String): String {
        try {
            val combination = timestamp + BuildConfig.PRIVATE_API_KEY + BuildConfig.PUBLIC_API_KEY
            val md = java.security.MessageDigest.getInstance("MD5")
            val array = md.digest(combination.toByteArray(charset("UTF-8")))
            val sb = StringBuffer()
            for (i in array.indices) {
                sb.append(Integer.toHexString(array[i] and 0xFF or 0x100).substring(1, 3))
            }
            return sb.toString()
        } catch (e: java.security.NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ""
    }
}
