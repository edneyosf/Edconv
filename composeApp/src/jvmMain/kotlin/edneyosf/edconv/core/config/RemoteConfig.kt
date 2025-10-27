package edneyosf.edconv.core.config

import java.net.URI

private const val URL = "https://raw.githubusercontent.com/edneyosf/Edconv/main/.env"

class RemoteConfig {

    var latestVersion: String? = null
    var donationUrl: String? = null
    var releasesUrl: String? = null

    fun loadEnv() {
        val url = URI(URL).toURL()
        val lines = url.readText().lines()
        val data = lines
            .filter { it.contains(other = "=") && !it.trim().startsWith(prefix = "#") }
            .associate {
                val (key, value) = it.split("=", limit = 2)
                key.trim() to value.trim().removeSurrounding(delimiter = "\"")
            }

        latestVersion = data["APP_VERSION"]
        donationUrl = data["DONATION_URL"]
        releasesUrl = data["APP_RELEASES_URL"]
    }
}