package cmm.apps.esmorga.view.viewmodel.createEventForm

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.CreateEventUseCase
import cmm.apps.esmorga.view.createevent.CreateEventFlowSession
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.createeventimage.CreateEventFormImageViewModel
import cmm.apps.esmorga.view.createevent.createeventimage.model.CreateEventFormImageEffect
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateEventFormImageViewModelTest {

    private lateinit var viewModel: CreateEventFormImageViewModel
    private lateinit var flowViewModel: CreateEventFlowSession
    private val mockCreateEventUseCase = mockk<CreateEventUseCase>(relaxed = true)

    @Before
    fun setup() {
        flowViewModel = CreateEventFlowSession().apply {
            updateTitle(name = "Test Event", description = null)
        }
        viewModel = CreateEventFormImageViewModel(flowViewModel, mockCreateEventUseCase)
    }

    @Test
    fun `given initial state then uiState is empty`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()

            assertEquals("", initialState.imageUrl)
            assertFalse(initialState.showPreview)
            assertNull(initialState.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid jpg image url when preview clicked then state shows preview without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.jpg")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertEquals("https://example.com/image.jpg", state.imageUrl)
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid jpeg image url when preview clicked then state shows preview without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.jpeg")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid png image url when preview clicked then state shows preview without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.png")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid gif image url when preview clicked then state shows preview without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.gif")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid webp image url when preview clicked then state shows preview without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.webp")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid http url when preview clicked then state shows preview without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("http://example.com/image.png")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given url with query parameters when preview clicked then state shows preview without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.jpg?width=100&height=100")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given url with case insensitive extension when preview clicked then state shows preview without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.JPG")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given blank url when preview clicked then state shows error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("   ")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertEquals(R.string.inline_error_image_url_required, state.imageError)
            assertFalse(state.showPreview)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given empty url when preview clicked then state shows error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertEquals(R.string.inline_error_image_url_required, state.imageError)
            assertFalse(state.showPreview)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given invalid url format when preview clicked then state shows error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("not a url")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertEquals(R.string.inline_error_image_url_required, state.imageError)
            assertFalse(state.showPreview)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given url without protocol when preview clicked then state shows error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("example.com/image.jpg")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertEquals(R.string.inline_error_image_url_required, state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given url with ftp protocol when preview clicked then state shows error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("ftp://example.com/image.jpg")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertEquals(R.string.inline_error_image_url_required, state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given url with unsupported image format when preview clicked then state shows error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.bmp")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertEquals(R.string.inline_error_image_url_required, state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given url without file extension when preview clicked then state shows error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertEquals(R.string.inline_error_image_url_required, state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given image in preview when delete clicked then state clears all fields`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.jpg")
            awaitItem()

            viewModel.onPreviewClick()
            awaitItem()

            viewModel.onDeleteImageClick()

            val state = awaitItem()
            assertEquals("", state.imageUrl)
            assertFalse(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given image url when image url changed then error is cleared`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onPreviewClick()
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.jpg")

            val state = awaitItem()
            assertEquals("https://example.com/image.jpg", state.imageUrl)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid image when back clicked then emits navigate back effect`() = runTest {
        viewModel.onImageUrlChanged("https://example.com/image.jpg")

        viewModel.effect.test {
            viewModel.onBackClick()

            val effect = awaitItem()
            assertEquals(CreateEventFormImageEffect.NavigateBack, effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given preview shown when delete clicked then removes preview and image url`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.jpg")
            awaitItem()

            viewModel.onPreviewClick()
            awaitItem()

            viewModel.onDeleteImageClick()

            val state = awaitItem()
            assertEquals("", state.imageUrl)
            assertFalse(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given url with subdomain when preview clicked then state shows preview without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://images.example.com/path/to/image.jpg")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given url with multiple extensions when preview clicked then validates only last extension`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.backup.jpg")
            awaitItem()

            viewModel.onPreviewClick()

            val state = awaitItem()
            assertTrue(state.showPreview)
            assertNull(state.imageError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid image url when preview clicked then flowViewModel is updated with image url`() = runTest {
        viewModel.onImageUrlChanged("https://example.com/image.jpg")
        viewModel.onPreviewClick()

        assertEquals("https://example.com/image.jpg", flowViewModel.eventForm.value.imageUrl)
    }

    @Test
    fun `given image in preview when delete clicked then flowViewModel image url is updated to null`() = runTest {
        viewModel.onImageUrlChanged("https://example.com/image.jpg")
        viewModel.onPreviewClick()

        viewModel.onDeleteImageClick()

        assertNull(flowViewModel.eventForm.value.imageUrl)
    }

    @Test
    fun `given image url stored in flowViewModel when viewModel created then restores image url and showPreview`() = runTest {
        val flowViewModelWithImage = CreateEventFlowSession().apply {
            updateImage("https://example.com/restored.jpg")
        }
        val restoredViewModel = CreateEventFormImageViewModel(flowViewModelWithImage, mockCreateEventUseCase)

        restoredViewModel.uiState.test {
            val state = awaitItem()
            assertEquals("https://example.com/restored.jpg", state.imageUrl)
            assertTrue(state.showPreview)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given image preview persisted on back when viewModel recreated then state is restored`() = runTest {
        viewModel.onImageUrlChanged("https://example.com/restored-on-back.jpg")
        viewModel.onPreviewClick()
        viewModel.onBackClick()

        val restoredViewModel = CreateEventFormImageViewModel(flowViewModel, mockCreateEventUseCase)

        restoredViewModel.uiState.test {
            val state = awaitItem()
            assertEquals("https://example.com/restored-on-back.jpg", state.imageUrl)
            assertTrue(state.showPreview)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
