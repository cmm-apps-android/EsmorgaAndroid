package cmm.apps.esmorga.data


object CacheHelper {

    const val DEFAULT_CACHE_TTL = 5 * 60 * 1000 // 5 mins

    fun shouldReturnCache(dataTime: Long) = dataTime > System.currentTimeMillis() - DEFAULT_CACHE_TTL
}