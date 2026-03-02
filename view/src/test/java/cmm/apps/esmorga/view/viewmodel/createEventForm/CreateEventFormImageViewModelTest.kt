package cmm.apps.esmorga.view.viewmodel.createEventForm

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.createeventimage.CreateEventFormImageViewModel
import cmm.apps.esmorga.view.createevent.createeventimage.model.CreateEventFormImageEffect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateEventFormImageViewModelTest {

    private lateinit var viewModel: CreateEventFormImageViewModel
    private val eventForm = CreateEventForm(name = "Test Event")

    @Before
    fun setup() {
        viewModel = CreateEventFormImageViewModel(eventForm)
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
    fun `given preview shown when create event clicked then emits navigate next with updated form`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.jpg")
            awaitItem()

            viewModel.onPreviewClick()
            awaitItem()

            viewModel.effect.test {
                viewModel.onCreateEventClick()

                val effect = awaitItem()
                assertTrue(effect is CreateEventFormImageEffect.NavigateNext)
                val navigateEffect = effect as CreateEventFormImageEffect.NavigateNext
                assertEquals("https://example.com/image.jpg", navigateEffect.eventForm.imageUrl)
                assertEquals("Test Event", navigateEffect.eventForm.name)

                cancelAndIgnoreRemainingEvents()
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given preview not shown when create event clicked then emits navigate next with null image url`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onImageUrlChanged("https://example.com/image.jpg")
            awaitItem()

            viewModel.effect.test {
                viewModel.onCreateEventClick()

                val effect = awaitItem()
                assertTrue(effect is CreateEventFormImageEffect.NavigateNext)
                val navigateEffect = effect as CreateEventFormImageEffect.NavigateNext
                assertNull(navigateEffect.eventForm.imageUrl)

                cancelAndIgnoreRemainingEvents()
            }

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
}
