package com.wakeup.ai.domain.model

/**
 * Tipos de desafío disponibles en la app.
 * Cada uno tiene su propia lógica de validación y UI.
 */
enum class ChallengeType(val displayName: String, val icon: String, val description: String) {
    MATH("Math", "1234", "Resuelve un problema matemático"),
    SHAKE("Shake", "📳", "Sacude el teléfono para apagarla"),
    PHOTO("Photo", "📷", "Toma foto de un objeto registrado"),
    VOICE("Voice", "🎤", "Pronuncia una frase para apagar"),
    MEMORY("Memory", "🧠", "Recuerda una secuencia de números"),
    SEQUENCE("Sequence", "🔢", "Repite la secuencia en orden"),
    QUIZ("Quiz", "❓", "Responde una trivia rápida"),
    SCAN_BARCODE("Barcode", "📺", "Escanea un código de barras"),
    WALK_STEPS("Walk", "🚶", "Camina N pasos para apagar");

    companion object {
        fun random(): ChallengeType = entries.random()
    }
}

/**
 * Dificultad del desafío (1-5).
 * La IA adaptativa ajusta esto según el historial del usuario.
 */
data class DifficultyLevel(
    val level: Int,
    val multiplier: Float = 1f + (level - 1) * 0.25f,
    val timeLimitSeconds: Int = 30 + (level - 1) * 10
) {
    companion object {
        val MIN = DifficultyLevel(1)
        val MAX = DifficultyLevel(5)
        fun from(level: Int) = DifficultyLevel(level.coerceIn(1, 5))
    }
}

/**
 * Estado de la alarma.
 */
enum class AlarmState {
    SCHEDULED,
    FIRING,
    COMPLETED,
    SNOOZED,
    MISSED
}

/**
 * Estado del desafío.
 */
enum class ChallengeState {
    GENERATING,
    ACTIVE,
    SOLVING,
    COMPLETED,
    FAILED,
    SKIPPED
}

/**
 * Resultado de un desafío completado.
 */
data class ChallengeResult(
    val id: String,
    val alarmId: String,
    val challengeType: ChallengeType,
    val difficultyLevel: Int,
    val startedAt: Long,
    val completedAt: Long?,
    val wasCompleted: Boolean,
    val attempts: Int,
    val dayOfWeek: Int,
    val completionTimeMs: Long
) {
    val isSuccess: Boolean get() = wasCompleted && completionTimeMs > 0
}

/**
 * Perfil del usuario para IA adaptativa.
 */
data class UserProfile(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalAlarmsCompleted: Int = 0,
    val totalAlarmsMissed: Int = 0,
    val avgCompletionTimeMs: Long = 0,
    val currentDifficultyLevel: Int = 2,
    val challengeTypeSuccessRates: Map<ChallengeType, Float> = emptyMap(),
    val weekdayPatterns: Map<Int, AvgDayStats> = emptyMap(),
    val lastActiveDate: String = ""
)

data class AvgDayStats(
    val dayOfWeek: Int,
    val avgCompletionTimeMs: Long,
    val completionRate: Float
)

/**
 * Configuración de una alarma.
 */
data class Alarm(
    val id: String,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: Int, // bitmask: bit 0 = Sunday
    val isEnabled: Boolean = true,
    val label: String = "",
    val toneUri: String = "",
    val vibrate: Boolean = true,
    val challengeType: ChallengeType = ChallengeType.MATH,
    val difficultyLevel: Int = 2,
    val isSmartWakeEnabled: Boolean = false,
    val photoReferencePath: String? = null, // para PHOTO challenge
    val voicePhrase: String? = null,         // para VOICE challenge
    val barcodeValue: String? = null,         // para SCAN_BARCODE challenge
    val createdAt: Long = System.currentTimeMillis()
) {
    val timeFormatted: String
        get() = String.format("%02d:%02d", hour, minute)

    fun isActiveOnDay(dayOfWeek: Int): Boolean {
        // dayOfWeek: 1=Sunday, 7=Saturday (Calendar convention)
        val bit = if (dayOfWeek == 1) 0 else dayOfWeek - 1
        return (daysOfWeek shr bit) and 1 == 1
    }

    fun getActiveDays(): List<Int> {
        return (0..6).filter { ((daysOfWeek shr it) and 1) == 1 }
            .map { if (it == 0) 1 else it + 1 }
    }
}
