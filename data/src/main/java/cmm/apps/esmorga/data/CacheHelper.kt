package cmm.apps.esmorga.data


object CacheHelper {

    private const val DEFAULT_CACHE_TTL = 30 * 60 * 1000 // 30 mins

    fun shouldReturnCache(dataTime: Long) = dataTime > System.currentTimeMillis() - DEFAULT_CACHE_TTL
}