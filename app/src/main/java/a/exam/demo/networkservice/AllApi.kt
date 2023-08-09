package a.exam.demo.networkservice

object AllApi {

    private external fun baseUrlFromJNI(boolean: Boolean): String

    // news full url: https://api.newscatcherapi.com/v2/search?q=Tesla, limit 50 times
    /**
     * {
     *  "status": "error",
     * "error_code": "LimitReached",
     * "message": "Monthly API calls limit reached: 50"
     * }
     */
    const val NEWS_URL = "https://api.newscatcherapi.com/"
    private const val V2 = "v2/"
    const val SEARCH_NEWS = V2 + "search?q=Tesla"

    // movie full url: https://api.themoviedb.org/3/movie/popular?language=en-US&page=1
    const val MOVIE_URL = "https://api.themoviedb.org/"
    private const val V3 = "3/"
    const val SEARCH_MOVIE = V3 + "movie/popular"
}