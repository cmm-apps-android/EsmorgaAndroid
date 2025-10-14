package cmm.apps.esmorga.data


object CacheHelper {

    const val DEFAULT_CACHE_TTL = 0 // 30 mins

    fun shouldReturnCache(dataTime: Long) = dataTime > System.currentTimeMillis() - DEFAULT_CACHE_TTL
}