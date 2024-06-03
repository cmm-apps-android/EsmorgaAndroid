package cmm.apps.esmorga.data

import org.junit.Assert
import org.junit.Test

class CacheHelperTest {

    @Test
    fun `given a date fifteen minutes in the past when checking if cache should be used then true should be returned`() {
        val fifteenMinutes = 15 * 60 * 1000
        val shouldUseCache = CacheHelper.shouldReturnCache(System.currentTimeMillis() - fifteenMinutes)
        Assert.assertTrue(shouldUseCache)
    }

    @Test
    fun `given a date fourtyfive minutes in the past when checking if cache should be used then true should be returned`() {
        val fifteenMinutes = 45 * 60 * 1000
        val shouldUseCache = CacheHelper.shouldReturnCache(System.currentTimeMillis() - fifteenMinutes)
        Assert.assertFalse(shouldUseCache)
    }

    @Test
    fun `given a date in the future when checking if cache should be used then true should be returned`() {
        val fifteenMinutes = 15 * 60 * 1000
        val shouldUseCache = CacheHelper.shouldReturnCache(System.currentTimeMillis() + fifteenMinutes)
        Assert.assertTrue(shouldUseCache)
    }
}