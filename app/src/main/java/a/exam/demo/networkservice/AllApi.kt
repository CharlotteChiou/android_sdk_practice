package a.exam.demo.networkservice

object AllApi {

    private external fun baseUrlFromJNI(boolean: Boolean): String

    // full url: https://api.newscatcherapi.com/v2/search?q=Tesla
    const val NEWS_URL = "https://api.newscatcherapi.com/"
    private const val V2 = "v2/"
    const val SEARCH_NEWS = V2 + "search?q=Tesla"
}