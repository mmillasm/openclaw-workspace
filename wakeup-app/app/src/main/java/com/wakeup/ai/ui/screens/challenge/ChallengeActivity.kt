package com.wakeup.ai.ui.screens.challenge

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.wakeup.ai.data.local.WakeUpDatabase
import com.wakeup.ai.domain.model.ChallengeState
import com.wakeup.ai.domain.model.ChallengeType
import com.wakeup.ai.domain.model.DifficultyLevel
import com.wakeup.ai.domain.usecase.ChallengeEngine
import com.wakeup.ai.domain.usecase.GeneratedChallenge
import com.wakeup.ai.domain.usecase.ChallengeParams
import com.wakeup.ai.ml.PhotoChallengeVerifier
import com.wakeup.ai.service.AlarmScheduler
import com.wakeup.ai.service.AlarmService
import com.wakeup.ai.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File
import java.util.Locale
import javax.inject.Inject
import kotlin.math.sqrt

@AndroidEntryPoint
class ChallengeActivity : ComponentActivity() {

    @Inject lateinit var challengeEngine: ChallengeEngine
    @Inject lateinit var database: WakeUpDatabase
    @Inject lateinit var alarmScheduler: AlarmScheduler
    @Inject lateinit var photoVerifier: PhotoChallengeVerifier

    private var alarmService: AlarmService? = null
    private var serviceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // Service connected
            serviceBound = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Full screen, show on lock screen, turn screen on
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        // Keep screen on during challenge
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Bind to service if already running
        Intent(this, AlarmService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        val alarmId = intent.getStringExtra(AlarmScheduler.EXTRA_ALARM_ID) ?: run {
            finish()
            return
        }
        val challengeTypeName = intent.getStringExtra(AlarmScheduler.EXTRA_CHALLENGE_TYPE)
            ?: ChallengeType.MATH.name
        val challengeType = try {
            ChallengeType.valueOf(challengeTypeName)
        } catch (e: Exception) {
            ChallengeType.MATH
        }

        setContent {
            WakeUpTheme {
                ChallengeScreen(
                    alarmId = alarmId,
                    initialChallengeType = challengeType,
                    onCompleted = { onChallengeDone(true) },
                    onFailed = { onChallengeDone(false) },
                    challengeEngine = challengeEngine,
                    database = database,
                    photoVerifier = photoVerifier
                )
            }
        }
    }

    private fun onChallengeDone(completed: Boolean) {
        try {
            val intent = Intent(this, AlarmService::class.java).apply {
                action = if (completed) AlarmService.ACTION_DISMISS else AlarmService.ACTION_STOP_ALARM
            }
            startService(intent)
        } catch (e: Exception) { }
        finish()
    }

    override fun onDestroy() {
        if (serviceBound) {
            unbindService(serviceConnection)
            serviceBound = false
        }
        super.onDestroy()
    }
}

@Composable
fun ChallengeScreen(
    alarmId: String,
    initialChallengeType: ChallengeType,
    onCompleted: () -> Unit,
    onFailed: () -> Unit,
    challengeEngine: ChallengeEngine,
    database: WakeUpDatabase,
    photoVerifier: PhotoChallengeVerifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var challenge by remember { mutableStateOf<GeneratedChallenge?>(null) }
    var challengeState by remember { mutableStateOf(ChallengeState.GENERATING) }
    var elapsedTime by remember { mutableLongStateOf(0L) }
    var attempts by remember { mutableIntStateOf(0) }

    // Timer
    LaunchedEffect(challenge?.id) {
        if (challenge != null) {
            val start = System.currentTimeMillis()
            while (challengeState == ChallengeState.ACTIVE || challengeState == ChallengeState.SOLVING) {
                elapsedTime = System.currentTimeMillis() - start
                delay(100)
            }
        }
    }

    // Generate challenge on start
    LaunchedEffect(alarmId) {
        val generated = challengeEngine.generateChallenge(
            alarmId = alarmId,
            baseChallengeType = initialChallengeType
        )
        challenge = generated
        challengeState = ChallengeState.ACTIVE
    }

    // Record result on completion
    fun handleCompletion(success: Boolean) {
        scope.launch {
            challenge?.let { ch ->
                challengeEngine.recordResult(
                    challengeId = ch.id,
                    alarmId = alarmId,
                    type = ch.type,
                    difficulty = ch.difficulty.level,
                    startedAt = ch.startedAt,
                    completedAt = System.currentTimeMillis(),
                    wasCompleted = success,
                    attempts = attempts
                )
            }
        }
        challengeState = if (success) ChallengeState.COMPLETED else ChallengeState.FAILED
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Background,
                        Surface
                    )
                )
            )
    ) {
        when (challengeState) {
            ChallengeState.GENERATING -> {
                LoadingState()
            }
            ChallengeState.ACTIVE, ChallengeState.SOLVING -> {
                challenge?.let { ch ->
                    ChallengeContent(
                        challenge = ch,
                        elapsedTime = elapsedTime,
                        onAnswer = { correct ->
                            attempts++
                            if (correct) {
                                handleCompletion(true)
                            }
                        },
                        onSkip = { handleCompletion(false) }
                    )
                }
            }
            ChallengeState.COMPLETED -> {
                CompletedState(elapsedTime, onDismiss = onCompleted)
            }
            ChallengeState.FAILED, ChallengeState.SKIPPED -> {
                FailedState(onDismiss = onFailed)
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Generando desafío...", color = TextSecondary)
        }
    }
}

@Composable
fun ChallengeContent(
    challenge: GeneratedChallenge,
    elapsedTime: Long,
    onAnswer: (Boolean) -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Timer + Challenge Type
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Challenge type badge
            SuggestionChip(
                onClick = { },
                label = { Text(challenge.type.displayName) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = SurfaceVariant
                )
            )

            // Timer
            TimerDisplay(elapsedTime = elapsedTime)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Challenge area
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            when (challenge.type) {
                ChallengeType.MATH -> MathChallenge(
                    params = challenge.params,
                    onCorrect = { onAnswer(true) },
                    onWrong = { onAnswer(false) }
                )
                ChallengeType.SHAKE -> ShakeChallenge(
                    params = challenge.params,
                    onComplete = { onAnswer(true) }
                )
                ChallengeType.VOICE -> VoiceChallenge(
                    params = challenge.params,
                    onComplete = { onAnswer(true) },
                    onFail = { onAnswer(false) }
                )
                ChallengeType.QUIZ -> QuizChallenge(
                    params = challenge.params,
                    onCorrect = { onAnswer(true) },
                    onWrong = { onAnswer(false) }
                )
                else -> Text(
                    "Desafío: ${challenge.type.displayName}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface
                )
            }
        }

        // Skip button
        TextButton(onClick = onSkip) {
            Text("Omitir (pierdo la racha)", color = TextSecondary)
        }
    }
}

@Composable
fun TimerDisplay(elapsedTime: Long) {
    val seconds = (elapsedTime / 1000).toInt()
    val minutes = seconds / 60
    val secs = seconds % 60

    Text(
        String.format("%02d:%02d", minutes, secs),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Primary
    )
}

@Composable
fun MathChallenge(
    params: ChallengeParams,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    var answer by remember { mutableStateOf("") }
    var shake by remember { mutableFloatStateOf(0f) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            params.question ?: "?",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            textStyle = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center),
            placeholder = { Text("?", style = MaterialTheme.typography.headlineMedium) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (answer.trim() == params.answer) {
                        onCorrect()
                    } else {
                        onWrong()
                        answer = ""
                    }
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = SurfaceVariant
            ),
            modifier = Modifier.fillMaxWidth(0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (answer.trim() == params.answer) {
                    onCorrect()
                } else {
                    onWrong()
                    answer = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Verificar", modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp))
        }
    }
}

@Composable
fun ShakeChallenge(
    params: ChallengeParams,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    var shakeCount by remember { mutableIntStateOf(0) }
    var lastShakeTime by remember { mutableLongStateOf(0L) }
    var isHolding by remember { mutableStateOf(false) }

    val threshold = 12f
    val cooldown = 300L

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event ?: return
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val magnitude = sqrt(x * x + y * y + z * z)

                if (magnitude > threshold) {
                    val now = System.currentTimeMillis()
                    if (now - lastShakeTime > cooldown) {
                        lastShakeTime = now
                        shakeCount++
                        if (shakeCount >= params.targetShakes) {
                            if (params.requireHold) {
                                isHolding = true
                            } else {
                                onComplete()
                            }
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Phone icon with shake animation
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text("📳", fontSize = 64.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "$shakeCount / ${params.targetShakes}",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = Primary
        )

        Text(
            "sacudidas",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = { (shakeCount.toFloat() / params.targetShakes).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Primary,
            trackColor = SurfaceVariant
        )

        if (params.requireHold && isHolding) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "🎯 ¡Sostén el teléfono quieto 3 segundos!",
                color = Success,
                fontWeight = FontWeight.Medium
            )
            // Simple hold detection
            LaunchedEffect(isHolding) {
                delay(3000)
                onComplete()
            }
        }
    }
}

@Composable
fun VoiceChallenge(
    params: ChallengeParams,
    onComplete: () -> Unit,
    onFail: () -> Unit
) {
    var isListening by remember { mutableStateOf(false) }
    var recognizedText by remember { mutableStateOf("") }

    val context = LocalContext.current
    var recognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }

    DisposableEffect(Unit) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        }
        onDispose {
            recognizer?.destroy()
        }
    }

    LaunchedEffect(recognizer) {
        recognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                recognizedText = text

                // Check if it matches (fuzzy)
                val target = params.question ?: ""
                if (text.lowercase().contains(target.lowercase().take(5))) {
                    onComplete()
                } else {
                    isListening = false
                }
            }
            override fun onError(error: Int) {
                isListening = false
                if (error == SpeechRecognizer.ERROR_NO_MATCH) {
                    onFail()
                }
            }
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { isListening = false }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Di la siguiente frase:",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            params.question ?: "Buenos días",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Mic button
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isListening) 1.15f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    if (isListening) Primary else SurfaceVariant
                )
                .clickable {
                    if (!isListening) {
                        isListening = true
                        val intent = Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                            putExtra(android.speech.RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                        }
                        try {
                            recognizer?.startListening(intent)
                        } catch (e: Exception) {
                            isListening = false
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (isListening) Icons.Default.Mic else Icons.Default.MicOff,
                "Mic",
                tint = OnPrimary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            if (isListening) "Escuchando..." else "Toca para hablar",
            color = TextSecondary
        )

        if (recognizedText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Dijiste: \"$recognizedText\"",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun QuizChallenge(
    params: ChallengeParams,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    var answer by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            params.question ?: "?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
            placeholder = { Text("Tu respuesta") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (answer.trim().lowercase() == params.answer?.lowercase()) {
                        onCorrect()
                    } else {
                        onWrong()
                        answer = ""
                    }
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = SurfaceVariant
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (answer.trim().lowercase() == params.answer?.lowercase()) {
                    onCorrect()
                } else {
                    onWrong()
                    answer = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Responder")
        }
    }
}

@Composable
fun CompletedState(elapsedTime: Long, onDismiss: () -> Unit) {
    val seconds = (elapsedTime / 1000).toInt()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎉", fontSize = 80.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "¡Despertaste!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Success
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Tiempo: ${seconds}s",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text("A empezar el día", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
        }
    }
}

@Composable
fun FailedState(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("😴", fontSize = 80.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Racha perdida",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Error
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Mañana será diferente",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceVariant)
            ) {
                Text("OK")
            }
        }
    }
}
