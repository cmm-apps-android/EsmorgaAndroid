package cmm.apps.esmorga.view.screenshot

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.test.FakeImageLoaderEngine
import com.android.ide.common.rendering.api.SessionParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule


open class BaseScreenshotTest {
    //record screenshots with: ./gradlew view:recordPaparazziDebug (optional: --tests=com.package.ClassName.test_name)
    //execute tests with ./gradlew view:verifyPaparazziDebug

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false
    )

    @OptIn(ExperimentalCoilApi::class)
    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val engine = FakeImageLoaderEngine.Builder()
            .intercept({ it is String }, ColorDrawable(0xFF641C34.toInt()))
            .default(ColorDrawable(Color.BLUE))
            .build()
        val imageLoader = ImageLoader.Builder(paparazzi.context)
            .components { add(engine) }
            .build()
        Coil.setImageLoader(imageLoader)
    }

}