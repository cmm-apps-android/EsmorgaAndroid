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
import org.junit.Before
import org.junit.Rule


open class BaseScreenshotTest {
    //execute tests with: ./gradlew view:recordPaparazziDebug

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false
    )

    @OptIn(ExperimentalCoilApi::class)
    @Before
    fun before() {
        val engine = FakeImageLoaderEngine.Builder()
            .intercept({ it is String }, ColorDrawable(Color.BLACK))
            .default(ColorDrawable(Color.BLUE))
            .build()
        val imageLoader = ImageLoader.Builder(paparazzi.context)
            .components { add(engine) }
            .build()
        Coil.setImageLoader(imageLoader)
    }

}