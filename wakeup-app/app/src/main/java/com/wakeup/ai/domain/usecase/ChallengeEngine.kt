package com.wakeup.ai.domain.usecase

import com.wakeup.ai.data.local.WakeUpDatabase
import com.wakeup.ai.data.local.entity.ChallengeResultEntity
import com.wakeup.ai.domain.model.ChallengeType
import com.wakeup.ai.domain.model.DifficultyLevel
import com.wakeup.ai.domain.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

/**
 * Motor de desafíos con IA adaptativa.
 * Aprende del historial del usuario para ajustar la dificultad
 * y el tipo de desafío óptimo.
 */
@Singleton
class ChallengeEngine @Inject constructor(
    @ApplicationContext private val context: android.content.Context,
    private val database: WakeUpDatabase
) {
    companion object {
        // Rangos objetivo de tiempo de completado (ms)
        private const val MIN_COMPLETION_TIME = 20_000L  // 20 segundos (demasiado fácil)
        private const val TARGET_COMPLETION_TIME = 45_000L  // 45 segundos (ideal)
        private const val MAX_COMPLETION_TIME = 90_000L  // 90 segundos (duro pero posible)

        // Ajuste de dificultad
        private const val DIFFICULTY_UP_THRESHOLD = 25_000L  // Si completa en <25s → dificultad++
        private const val DIFFICULTY_DOWN_THRESHOLD = 90_000L  // Si tarda >90s o falla → dificultad--

        // Mínimo de datos para ajustar
        private const val MIN_RESULTS_FOR_ADJUSTMENT = 3
    }

    /**
     * Genera el desafío del día basándose en el perfil del usuario.
     */
    suspend fun generateChallenge(
        alarmId: String,
        baseChallengeType: ChallengeType,
        overrideDifficulty: Int? = null
    ): GeneratedChallenge {
        val profile = buildUserProfile()
        val todayOfWeek = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK)
        val todayDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        // Determinar dificultad
        val difficulty = overrideDifficulty
            ?: calculateOptimalDifficulty(profile, todayOfWeek)

        // Determinar tipo de desafío
        val challengeType = selectOptimalChallengeType(profile, baseChallengeType, todayOfWeek)

        // Generar parámetros específicos del desafío
        val params = generateChallengeParams(challengeType, difficulty)

        return GeneratedChallenge(
            id = UUID.randomUUID().toString(),
            alarmId = alarmId,
            type = challengeType,
            difficulty = DifficultyLevel.from(difficulty),
            params = params,
            startedAt = System.currentTimeMillis()
        )
    }

    /**
     * Registra el resultado de un desafío y ajusta el perfil.
     */
    suspend fun recordResult(
        challengeId: String,
        alarmId: String,
        type: ChallengeType,
        difficulty: Int,
        startedAt: Long,
        completedAt: Long?,
        wasCompleted: Boolean,
        attempts: Int
    ) {
        val completionTime = if (completedAt != null) completedAt - startedAt else 0L
        val dayOfWeek = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK)

        val entity = ChallengeResultEntity(
            id = challengeId,
            alarmId = alarmId,
            challengeType = type.name,
            difficultyLevel = difficulty,
            startedAt = startedAt,
            completedAt = completedAt,
            wasCompleted = wasCompleted,
            attempts = attempts,
            dayOfWeek = dayOfWeek
        )
        database.challengeResultDao().insertResult(entity)

        // Actualizar perfil del usuario
        updateUserProfile(type, difficulty, completionTime, wasCompleted, dayOfWeek)
    }

    private suspend fun buildUserProfile(): UserProfile {
        val entity = database.userProfileDao().getUserProfileOnce()
        if (entity == null) {
            // Crear perfil inicial
            val newProfile = com.wakeup.ai.data.local.entity.UserProfileEntity(
                id = "local_user",
                currentStreak = 0,
                longestStreak = 0,
                totalAlarmsCompleted = 0,
                totalAlarmsMissed = 0,
                avgCompletionTimeMs = 0,
                currentDifficultyLevel = 2,
                challengeTypeSuccessRates = "{}",
                weekdayPatterns = "{}",
                lastActiveDate = ""
            )
            database.userProfileDao().insertProfile(newProfile)
            return UserProfile()
        }

        return UserProfile(
            currentStreak = entity.currentStreak,
            longestStreak = entity.longestStreak,
            totalAlarmsCompleted = entity.totalAlarmsCompleted,
            totalAlarmsMissed = entity.totalAlarmsMissed,
            avgCompletionTimeMs = entity.avgCompletionTimeMs,
            currentDifficultyLevel = entity.currentDifficultyLevel
        )
    }

    private suspend fun updateUserProfile(
        type: ChallengeType,
        difficulty: Int,
        completionTime: Long,
        wasCompleted: Boolean,
        dayOfWeek: Int
    ) {
        val entity = database.userProfileDao().getUserProfileOnce() ?: return
        val todayDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        // Actualizar streak
        var newStreak = entity.currentStreak
        var longestStreak = entity.longestStreak
        val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE)

        when {
            wasCompleted && entity.lastActiveDate == yesterday -> newStreak++
            wasCompleted && entity.lastActiveDate != todayDate -> newStreak = 1
            !wasCompleted && entity.lastActiveDate == todayDate -> {} // Ya se perdió antes
            !wasCompleted -> newStreak = 0
        }

        if (newStreak > longestStreak) longestStreak = newStreak

        // Calcular nueva dificultad basándose en tiempo
        var newDifficulty = entity.currentDifficultyLevel
        if (wasCompleted && completionTime > 0) {
            when {
                completionTime < DIFFICULTY_UP_THRESHOLD && newDifficulty < 5 -> newDifficulty++
                completionTime > DIFFICULTY_DOWN_THRESHOLD && newDifficulty > 1 -> newDifficulty--
            }
        } else if (!wasCompleted && newDifficulty > 1) {
            newDifficulty-- // Fallar = reducir dificultad
        }

        // Recalcular promedio de tiempo
        val totalTime = entity.avgCompletionTimeMs * entity.totalAlarmsCompleted
        val newTotal = if (wasCompleted) totalTime + completionTime else totalTime
        val newCount = if (wasCompleted) entity.totalAlarmsCompleted + 1 else entity.totalAlarmsCompleted
        val newAvgTime = if (newCount > 0) newTotal / newCount else 0L

        val updatedEntity = entity.copy(
            currentStreak = newStreak,
            longestStreak = longestStreak,
            totalAlarmsCompleted = if (wasCompleted) entity.totalAlarmsCompleted + 1 else entity.totalAlarmsCompleted,
            totalAlarmsMissed = if (!wasCompleted) entity.totalAlarmsMissed + 1 else entity.totalAlarmsMissed,
            avgCompletionTimeMs = newAvgTime,
            currentDifficultyLevel = newDifficulty,
            lastActiveDate = todayDate
        )

        database.userProfileDao().insertProfile(updatedEntity)
    }

    private fun calculateOptimalDifficulty(profile: UserProfile, dayOfWeek: Int): Int {
        // Si es un día difícil históricamente, reducir un poco
        val dayPattern = profile.weekdayPatterns[dayOfWeek]
        val dayAdjustment = if (dayPattern != null && dayPattern.completionRate < 0.6) -1 else 0

        return (profile.currentDifficultyLevel + dayAdjustment).coerceIn(1, 5)
    }

    private fun selectOptimalChallengeType(
        profile: UserProfile,
        preferred: ChallengeType,
        dayOfWeek: Int
    ): ChallengeType {
        // Si el tipo preferido tiene tasa de éxito baja, probar otro
        val preferredRate = profile.challengeTypeSuccessRates[preferred] ?: 0.7f

        return if (preferredRate < 0.5) {
            // Buscar el tipo con mejor tasa de éxito
            profile.challengeTypeSuccessRates.entries
                .filter { it.value >= 0.7f }
                .maxByOrNull { it.value }
                ?.key
                ?: ChallengeType.entries.filter { it != preferred }.random()
        } else {
            preferred
        }
    }

    private fun generateChallengeParams(type: ChallengeType, difficulty: Int): ChallengeParams {
        return when (type) {
            ChallengeType.MATH -> generateMathParams(difficulty)
            ChallengeType.SHAKE -> generateShakeParams(difficulty)
            ChallengeType.PHOTO -> ChallengeParams() // Params vienen del registro
            ChallengeType.VOICE -> ChallengeParams() // Phrase viene del registro
            ChallengeType.MEMORY -> generateMemoryParams(difficulty)
            ChallengeType.QUIZ -> generateQuizParams(difficulty)
            else -> ChallengeParams()
        }
    }

    private fun generateMathParams(difficulty: Int): ChallengeParams {
        val (a, b, c, d, hasParenthesis) = when (difficulty) {
            1 -> MathProblem(2, 10, 0, 1, false)   // a + b
            2 -> MathProblem(1, 12, 0, 1, false)   // a × b
            3 -> MathProblem(1, 12, 1, 20, false)  // a × b + c
            4 -> MathProblem(1, 10, 1, 10, true)    // (a + b) × c
            else -> MathProblem(2, 12, 1, 12, true) // a × b + c ÷ d
        }

        val (question, answer) = if (!hasParenthesis && c == 0 && d == 1) {
            val x = (1..a).random()
            val y = (1..b).random()
            val op = if (difficulty == 1) "+" else "×"
            val ans = if (difficulty == 1) x + y else x * y
            "($x $op $y)" to ans.toString()
        } else {
            val x = (1..a).random()
            val y = (1..b).random()
            val z = (1..c).random()
            val w = (1..d).random().takeIf { it > 0 } ?: 1
            when (difficulty) {
                3 -> {
                    val prod = x * y
                    "($x × $y) + $z = ?" to (prod + z).toString()
                }
                4 -> {
                    val sum = x + y
                    "($x + $y) × $z = ?" to (sum * z).toString()
                }
                else -> {
                    val prod = x * y
                    val divResult = prod / w
                    "$x × $y ÷ $w = ?" to divResult.toString()
                }
            }
        }

        return ChallengeParams(
            question = question,
            answer = answer,
            difficulty = difficulty
        )
    }

    private fun generateShakeParams(difficulty: Int): ChallengeParams {
        val shakeCount = when (difficulty) {
            1 -> 10
            2 -> 25
            3 -> 50
            4 -> 75
            else -> 100
        }
        val timeLimit = when (difficulty) {
            in 1..2 -> 0 // Sin límite de tiempo
            3 -> 30
            else -> 20
        }

        return ChallengeParams(
            targetShakes = shakeCount,
            timeLimitSeconds = timeLimit,
            requireHold = difficulty >= 3
        )
    }

    private fun generateMemoryParams(difficulty: Int): ChallengeParams {
        val sequenceLength = 3 + difficulty
        val numbers = (1..9).shuffled().take(sequenceLength)
        return ChallengeParams(
            sequence = numbers,
            displayDurationMs = (3000L - difficulty * 300L).coerceAtLeast(1500L)
        )
    }

    private fun generateQuizParams(difficulty: Int): ChallengeParams {
        val quizzes = listOf(
            "¿Cuántos días tiene una semana?" to "7",
            "¿Qué color se obtiene mezclando azul + amarillo?" to "verde",
            "¿Cuántas letras tiene la palabra 'sol'?" to "3",
            "¿Qué número viene después del 8?" to "9",
            "¿Cuántas esquinas tiene un triángulo?" to "3",
            "¿Qué fruta es roja por fuera y verde por dentro?" to "sandia",
            "¿Cuántos minutos tiene una hora?" to "60",
            "¿Qué forma tiene una moneda?" to "circulo"
        )
        val quiz = quizzes.random()
        return ChallengeParams(
            question = quiz.first,
            answer = quiz.second.lowercase()
        )
    }

    private data class MathProblem(
        val maxA: Int, val maxB: Int,
        val maxC: Int, val maxD: Int,
        val hasParenthesis: Boolean
    )
}

data class GeneratedChallenge(
    val id: String,
    val alarmId: String,
    val type: ChallengeType,
    val difficulty: DifficultyLevel,
    val params: ChallengeParams,
    val startedAt: Long
)

data class ChallengeParams(
    val question: String? = null,
    val answer: String? = null,
    val targetShakes: Int = 0,
    val timeLimitSeconds: Int = 30,
    val requireHold: Boolean = false,
    val sequence: List<Int> = emptyList(),
    val displayDurationMs: Long = 2000L
)
