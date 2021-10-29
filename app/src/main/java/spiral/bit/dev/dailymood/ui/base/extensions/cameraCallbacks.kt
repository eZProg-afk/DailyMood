package spiral.bit.dev.dailymood.ui.base.extensions

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun ImageCapture.takePictureAwait(
    outputFileOptions: ImageCapture.OutputFileOptions,
    executor: java.util.concurrent.Executor
) = suspendCoroutine<ImageCapture.OutputFileResults> { continuation ->
    takePicture(outputFileOptions, executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                continuation.resume(outputFileResults)
            }

            override fun onError(exception: ImageCaptureException) {
                continuation.resumeWithException(exception)
            }
        }
    )
}