package com.wakeup.ai.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.OptIn
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Verificador de desafío de foto usando ML Kit Image Labeling (on-device).
 * No usa cloud, todo se procesa localmente en el dispositivo.
 *
 * Estrategia:
 * 1. Usuario registra foto de referencia → extraemos labels (牙刷, sink, bathroom)
 * 2. En el desafío: usuario toma foto → extraemos labels
 * 3. Comparamos: si ≥2 labels en común con confidence > 0.7 → match
 */
@Singleton
class PhotoChallengeVerifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.6f)
            .build()
    )

    companion object {
        private const val MIN_COMMON_LABELS = 2
        private const val MIN_CONFIDENCE = 0.65f
        private const val IMAGE_SIZE = 512 // px para resize
    }

    /**
     * Extrae labels de una imagen de referencia y los guarda como string.
     * Llamado cuando el usuario registra la foto del objeto.
     */
    suspend fun extractLabels(uri: Uri): List<String> {
        return extractLabelsFromUri(uri)
    }

    suspend fun extractLabelsFromPath(path: String): List<String> {
        val file = File(path)
        if (!file.exists()) return emptyList()

        val bitmap = BitmapFactory.decodeFile(path) ?: return emptyList()
        return extractLabelsFromBitmap(bitmap)
    }

    private suspend fun extractLabelsFromUri(uri: Uri): List<String> {
        return try {
            val bitmap = context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            } ?: return emptyList()
            extractLabelsFromBitmap(bitmap)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @OptIn(com.google.mlkit.vision.common.ExperimentalGetImage::class)
    private suspend fun extractLabelsFromBitmap(bitmap: Bitmap): List<String> {
        val resized = resizeBitmap(bitmap, IMAGE_SIZE)
        val image = InputImage.fromBitmap(resized, 0)

        return suspendCancellableCoroutine { continuation ->
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    val labelStrings = labels
                        .filter { it.confidence > MIN_CONFIDENCE }
                        .map { it.text.lowercase() }
                    continuation.resume(labelStrings)
                }
                .addOnFailureListener {
                    continuation.resume(emptyList())
                }
        }
    }

    /**
     * Verifica si la foto tomada por el usuario matchea con la referencia.
     * Retorna true si ≥2 labels en común con confidence adecuada.
     */
    suspend fun verifyMatch(referenceLabels: List<String>, capturedUri: Uri): Boolean {
        if (referenceLabels.isEmpty()) return false

        val capturedLabels = extractLabelsFromUri(capturedUri)
        return verifyMatchByLabels(referenceLabels, capturedLabels)
    }

    suspend fun verifyMatch(referenceLabels: List<String>, capturedPath: String): Boolean {
        if (referenceLabels.isEmpty()) return false

        val capturedLabels = extractLabelsFromPath(capturedPath)
        return verifyMatchByLabels(referenceLabels, capturedLabels)
    }

    private fun verifyMatchByLabels(reference: List<String>, captured: List<String>): Boolean {
        if (reference.isEmpty() || captured.isEmpty()) return false

        val refLower = reference.map { it.lowercase() }
        val capLower = captured.map { it.lowercase() }

        // Contar labels en común
        val commonCount = refLower.count { refLabel ->
            capLower.any { capLabel ->
                // Match exacto
                refLabel == capLabel ||
                // Match parcial (contenido)
                capLabel.contains(refLabel) ||
                refLabel.contains(capLabel) ||
                // Sinonyms comunes
                areSynonyms(refLabel, capLabel)
            }
        }

        return commonCount >= MIN_COMMON_LABELS
    }

    /**
     * Sinonyms mapping para mejorar matching.
     */
    private fun areSynonyms(a: String, b: String): Boolean {
        val synonyms = mapOf(
            "bathroom" to setOf("toilet", "restroom", "washroom"),
            "sink" to setOf("faucet", "basin", "washbasin"),
            "toothbrush" to setOf("brush", "teeth", "dental"),
            "kitchen" to setOf("stove", "oven", "cooking"),
            "bedroom" to setOf("bed", "pillow", "mattress", "sleep"),
            "coffee" to setOf("cup", "mug", "caffeine", "drink"),
            "mirror" to setOf("reflection", "glass", "bathroom"),
            "window" to setOf("curtain", "glass", "view"),
            "desk" to setOf("table", "office", "computer"),
            "phone" to setOf("mobile", "cellphone", "smartphone")
        )

        val aSet = synonyms[a] ?: emptySet()
        val bSet = synonyms[b] ?: emptySet()

        return a in bSet || b in aSet ||
            aSet.any { it in bSet }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val ratio = width.toFloat() / height.toFloat()

        val newWidth: Int
        val newHeight: Int
        if (width > height) {
            newWidth = maxSize
            newHeight = (maxSize / ratio).toInt()
        } else {
            newHeight = maxSize
            newWidth = (maxSize * ratio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
