# SPEC.md — WakeUp AI: Despertador con Micro-Desafíos

## 1. Concepto & Visión

**WakeUp AI** es un despertador inteligente que te fuerza a participar activamente en la mañana a través de micro-desafíos generados por IA. No es solo "resolver un math problem" — es que cada día el desafío se adapte a tu patrón de sueño, historial de completado, y aluda a algo que te motive realmente a salir de la cama.

La experiencia: suena la alarma, aparece un desafío único del día, lo completas, y ya estás despierto, ligeramente desafiado, y con un streak subiéndose. El diferenciador central: **IA que aprende tu patrón de levantarte y ajusta la dificultad para que siempre te tome entre 30-90 segundos, ni tan fácil que no te despierte, ni tan difícil que te frustres.**

Nombre clave interno: **WakeUp**
- App ID: `com.wakeup.ai`
- Mínimo SDK: 26 (Android 8.0)
- Target SDK: 35 (Android 15)
- Idioma: Kotlin, Jetpack Compose

---

## 2. Design Language

### Estética
**"Dark Neon Morning"** — Un despertar desde la oscuridad. Fondos oscuros con acentos de luz cálida que simulan el amanecer gradual. Minimalista pero con carácter. Piensa: despertador de hotel premium convertido en app.

### Paleta de colores
```
Background:        #0D0D1A (deep space blue-black)
Surface:           #1A1A2E (elevated surface)
Surface Variant:   #252540 (cards, dialogs)
Primary:           #FF6B35 (warm sunrise orange)
Primary Variant:   #FF8C5A (light mode / pressed)
Secondary:         #7B61FF (electric violet — streak/achievement accent)
Tertiary:          #00D9C0 (teal — AI/smart indicator)
On Background:     #F0F0F5
On Surface:        #E8E8EE
On Primary:        #FFFFFF
Success:           #4ADE80 (streak maintained)
Error:             #FF4757 (missed day)
Text Secondary:    #8A8AA8
Divider:           #2D2D4A
```

### Tipografía
- **Display/Challenge Title:** Space Grotesk Bold, 32sp
- **Headlines:** Space Grotesk SemiBold, 24sp
- **Body:** Inter Regular, 16sp
- **Caption/Labels:** Inter Medium, 13sp
- **Challenge Numbers:** Space Grotesk Mono, 48sp

### Sistema Espacial
- Base unit: 4dp
- Spacing scale: 4, 8, 12, 16, 24, 32, 48, 64
- Corner radius: 12dp (cards), 24dp (bottom sheets), 50% (FABs)
- Elevation: 0dp (background) → 8dp (cards) → 16dp (modals)

### Filosofía de Movimiento
- **Alarma sonar:** pulso de luz naranja desde el centro, vibración sincronizada
- **Desafío aparecer:** slide-up con spring physics (damping 0.7, stiffness 300)
- **Streak counter:** contador animado con número rebotando
- **Completado:** confetti particles + haptic success + screen flash
- **Transiciones de pantalla:** shared element transitions, 300ms ease-out

---

## 3. Arquitectura

### Patrón: Clean Architecture + MVVM

```
app/
├── ui/                    # Capa de presentación
│   ├── screens/           # Pantallas completas
│   ├── components/        # Componentes reutilizables
│   ├── navigation/        # NavGraph y rutas
│   └── theme/             # Colores, tipografía, shapes
├── domain/                # Lógica de negocio pura
│   ├── model/             # Modelos de dominio
│   ├── usecase/           # Casos de uso
│   └── repository/        # Interfaces de repositorio
├── data/                  # Implementaciones
│   ├── local/             # Room DB, DataStore, DAOs
│   ├── repository/        # Implementaciones concretas
│   └── ml/                # Modelos de ML Kit
├── service/               # Foreground services (alarm, challenge)
├── receiver/              # BroadcastReceivers
├── di/                    # Hilt modules
└── util/                  # Extensions, constants, helpers
```

### Flujo de datos

```
User sets alarm
    → AlarmRepository.saveAlarm()
    → ScheduleAlarmUseCase (calcula próximo trigger)
    → AlarmSchedulerService (exact alarm via AlarmManager)
    → [DOSP: DEVICE RESTARTS]
    → BootReceiver → re-schedule all active alarms

ALARM FIRES (en BroadcastReceiver)
    → AlarmTriggerService (foreground service arranca)
    → AIChallengeGenerator.generateTodayChallenge(userId)
    → ChallengeNotification → full-screen activity
    → User completes challenge
    → ChallengeResultRepository.save(result)
    → AdaptiveDifficultyUseCase (actualiza perfil)
    → StreakManager.updateStreak()
    → Dismiss alarm

SLEEP CYCLE (opcional, con sensores)
    → SleepTrackingService (background)
    → SleepDataRepository.save(segment)
    → SmartWakeUseCase (despierta en light sleep ±30min)
```

### Estado de la app

```
AlarmState:
  - SCHEDULED
  - FIRING (alarma sonando + desafío activo)
  - COMPLETED (usuario completó desafío)
  - SNOOZED
  - MISSED (no se completó, se guardó como missed)

ChallengeState:
  - GENERATING
  - ACTIVE (desafío en pantalla)
  - SOLVING (usuario interactuando)
  - COMPLETED
  - FAILED
```

---

## 4. Features (Priorizadas)

### Fase 1 — MVP (v1.0)
1. **Alarmas configurables** — hora, días de semana, tono, vibración, label
2. **Motor de desafíos** — 4 tipos base:
   - `MATH`: Sumas/restas/multiplicación de dificultad variable
   - `SHAKE`: Agitar el teléfono N veces (N configurable por IA)
   - `PHOTO`: Tomar foto de objeto pre-registrado (ML Kit Image Labeling)
   - `VOICE`: Pronunciar frase específica (ML Kit Speech Recognition)
3. **IA Adaptativa** — ajusta dificultad según historial de completado
4. **Streak System** — contador diario, longest streak, streak freeze
5. **Confiabilidad** — foreground service, exact alarms, Doze bypass, test de alarms

### Fase 2 — Engagement (v1.5)
6. **Daily Challenge Card** — pantalla resumen post-despertar con stats
7. **Insights semanales** — "Te tomó 2min 40seg esta semana"
8. **Challenges rotativos** — pool de 10+ tipos de desafíos
9. **Integración Health Connect** — registra que efectivamente te levantaste
10. **Tonos y sonidos** — biblioteca de sonidos, sunrise gradual audio

### Fase 3 — Social + AI (v2.0)
11. **Buddy Alarms** — alarma que depende de que tu amigo también complete el suyo
12. **Weekly Leaderboard** — anónimo, ¿cuántos se levantaron antes que tú?
13. **AI Challenge Generator** — LLM genera desafíos narrativos únicos ("Resuelve el acertijo: ¿Qué tiene llaves pero no cierra puertas?")
14. **Widget deホーム screen** — acceso rápido a siguiente alarma

### No-Lista (out of scope inicial)
- Sleep tracking con wearables (solo acelerómetro local)
- Cloud sync multi-dispositivo
- Family/shared accounts
- Widgets complejos

---

## 5. Desafíos Técnicos Clave

### 5.1 Confiabilidad de alarma (crítico)
```kotlin
// Problema: Android 12+ requiere USE_EXACT_ALARM permission
// Problema: Doze mode puede retrasar exact alarms
// Problema: fabricantes (Xiaomi, Samsung, Huawei) matan apps en background

// Solución en capas:
// Capa 1: AndroidAlarmManager.setExactAndAllowWhileIdle()
// Capa 2: ForegroundService con sticky notification durante la alarma
// Capa 3: BroadcastReceiver con HIGH_PRIORITY intent filter
// Capa 4: WakeLock parcial durante challenge activo
// Capa 5: AlarmIntegrityChecker en cada app open → test de que la alarma func
// Capa 6: Manufacturer-specific Doze whitelist guidance para usuario
```

### 5.2 Desafío de foto con IA local
```kotlin
// ML Kit Image Labeling (on-device, no cloud)
// 1. Usuario registra foto de referencia (Bitmap → compressed → local file)
// 2. Alarma: se abre cámara, usuario toma foto
// 3. ML Kit procesa ambas imágenes localmente
// 4. Se comparan labels:牙刷 vs "teeth", "sink", "bathroom"
// 5. Match si ≥2 labels en común + confidence > 0.7

// Alternativa: uso de TensorFlow Lite con modelo custom de similarity
```

### 5.3 Reconocimiento de voz local
```kotlin
// Speech recognition via Android SpeechRecognizer API (on-device donde disponible)
// Requiere: android.permission.RECORD_AUDIO
// Fallback: para dispositivos sin speech, usar MATH como fallback
```

### 5.4 IA adaptativa (backend-agnostic)
```kotlin
// Todo local, sin servidor, sin cuenta requerida para MVP
// UserChallengeProfile (Room entity):
//   - avg_completion_time_ms: Long
//   - challenge_type_success_rate: Map<ChallengeType, Float>
//   - weekday_pattern: Map<DayOfWeek, AvgCompletion>
//   - current_difficulty_level: Int (1-5)
//
// AdaptiveDifficultyUseCase:
//   if (completion_time < 20sec) difficulty++
//   if (completion_time > 90sec || failed) difficulty--
//   if (user consistently fails type X) swap to type Y
//   if (tuesday pattern is bad) set challenge to easier on tue
```

### 5.5 Batería ligera
```
Meta: < 1% batería/hora en background idle
- No foreground service hasta que alarma fire
- AlarmManager para scheduling (no WorkManager para esto)
- ML Kit on-device (no network calls en desafío)
- Batch DB writes
- WakeLock solo durante challenge activo (~30-90 segundos)
```

---

## 6. Stack Tecnológico

| Componente | Tecnología | Alternativa |
|---|---|---|
| **Lenguaje** | Kotlin 1.9+ | — |
| **UI** | Jetpack Compose (Material 3) | XML views (si BLoC) |
| **DI** | Hilt | Koin (más simple) |
| **DB** | Room 2.6 | DataStore (solo prefs) |
| **Preferences** | DataStore (Preferences) | SharedPreferences |
| **Scheduling** | AlarmManager + WorkManager | — |
| **ML** | ML Kit (Image Labeling, Speech) | TensorFlow Lite |
| **Audio** | ExoPlayer / MediaPlayer | — |
| **Navigation** | Compose Navigation | — |
| **Analytics** | Firebase Analytics (opcional v1+) | — |
| **Build** | Gradle 8 + Kotlin DSL | — |
| **Min SDK** | 26 | — |
| **Target SDK** | 35 | — |

---

## 7. Modelo de Datos (Room)

```kotlin
// Alarm
@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val hour: Int,
    val minute: Int,
    val daysOfWeek: Int, // bitmask: bit 0 = Sunday, bit 6 = Saturday
    val isEnabled: Boolean = true,
    val label: String = "",
    val toneUri: String = "",
    val vibrate: Boolean = true,
    val challengeType: ChallengeType = ChallengeType.MATH,
    val difficultyLevel: Int = 2,
    val isSmartWakeEnabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

// ChallengeResult
@Entity(tableName = "challenge_results")
data class ChallengeResultEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val alarmId: String,
    val challengeType: ChallengeType,
    val startedAt: Long,
    val completedAt: Long?,
    val wasCompleted: Boolean,
    val attempts: Int = 0,
    val dayOfWeek: Int
)

// UserProfile
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "local_user",
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalAlarmsCompleted: Int = 0,
    val totalAlarmsMissed: Int = 0,
    val avgCompletionTimeMs: Long = 0,
    val currentDifficultyLevel: Int = 2,
    val lastActiveDate: String = "" // yyyy-MM-dd
)

enum class ChallengeType {
    MATH,
    SHAKE,
    PHOTO,
    VOICE,
    MEMORY,       // recordar secuencia
    SEQUENCE,     // repetir números
    QUIZ,         // pregunta trivial
    SCAN_BARCODE, // escanear código de barras específico
    WALK_STEPS    // caminar N pasos (acelerómetro)
}
```

---

## 8. Pantallas

### 8.1 Home Screen (`/`)
- Lista de alarmas con toggle de enable/disable
- FAB para agregar alarma
- Streak counter prominente en top
- Pull-to-refresh (test alarms)
- Bottom bar: Home | Stats | Settings

### 8.2 Alarm Editor (`/alarm/:id?`)
- Time picker (scroll wheel estilo iOS)
- Selector de días (chips toggles: L M X J V S D)
- Selector de tono (grid de opciones + preview)
- Tipo de desafío (cards con preview del desafío)
- Toggle de smart wake (opcional)
- Guardar / Eliminar

### 8.3 Challenge Screen (`/challenge/:alarmId`)
- **Full-screen, no status bar, no navigation**
- Título del desafío animado
- Timer counting up (desde que apareció)
- Área de input del desafío (teclado, cámara, etc.)
- Progress bar o indicador visual
- Botón de emergencia: "Omitir desafío" (reduce streak)

### 8.4 Post-Challenge Screen (`/complete`)
- Confetti animation
- Tiempo que tomó
- Streak actualizado (+1 o 💀)
- "Despertaste a las HH:MM" con mensaje motivacional
- Botón: "Ver stats" o "OK, a empezar el día"

### 8.5 Stats Screen (`/stats`)
- Streak actual con flame animation
- Heatmap de últimos 30 días (completado vs perdido)
- Gráfico: tiempo promedio de levantado por día
- Logro más reciente desbloqueado
- Insights: "Los martes te cuesta más"

### 8.6 Settings (`/settings`)
- Configurar perfil
- Permisos (exact alarm, notificaciones, cámara, microphone)
- Tono default
- Vibración on/off
- Backup / Restore
- Acerca de

---

## 9. Desafíos — Detalle de Implementación

### MATH
```
Dificultad 1: a + b = ? (a,b 1-10)
Dificultad 2: a × b = ? (a,b 1-12)
Dificultad 3: a × b + c = ? (orden de operaciones)
Dificultad 4: a × b - c ÷ d = ? (paréntesis)
Dificultad 5: ecuación de segundo grado simple (x² = a)
Tiempo límite: 30 segundos
```

### SHAKE
```
Dificultad 1: 10 sacudidas
Dificultad 2: 25 sacudidas
Dificultad 3: 50 sacudidas + mantener arriba al final
Dificultad 4: 75 sacudidas en 20 segundos
Dificultad 5: patrón: shake 3x, pause, shake 3x, pause, shake 3x
Detección: AccelerometerListener con threshold ~12 m/s²
```

### PHOTO
```
Usuario registra objeto (foto) al configurar alarma
IA compara labels de la foto registrada vs la foto tomada
Dificultad 1: objeto grande, bien iluminado
Dificultad 2: objeto pequeño, fondo complejo
Dificultad 3: escena (no objeto único) — "baño", "cocina"
Validación: ML Kit Image Labeling, mínimo 2 labels en común con confidence > 0.7
```

### VOICE
```
Frase fija registrada por usuario al configurar alarma
Dificultad 1: frase corta (3-5 palabras)
Dificultad 2: frase media (6-10 palabras)
Dificultad 3: frase larga + contexto ("Buenos días, voy a levantarme")
Dificultad 4: decir tu nombre + día de la semana
Dificultad 5: math verbal ("Doce más siete, dividido dos")
Validación: SpeechRecognizer API, fuzzy matching con threshold 0.8
```

### MEMORY (v1.5)
```
Secuencia de 4-9 números en pantalla por 2 segundos
Usuario repite la secuencia en orden
Dificultad aumenta con longitud
```

---

## 10. Roadmap Tentativo

```
Mes 1-2:   Proto MVP
            - Alarm engine (confiable)
            - 4 desafíos base
            - Streak básico
            - Home + Editor

Mes 3:     Beta cerrada
            - 50-100 beta testers
            - Feedback loop
            - Fix confiabilidad Android 12+
            - Stats screen

Mes 4:     v1.0 Release
            - Play Store listing
            - Blog post / Reddit launch
            - Twitter/X presence
            - ASO optimization

Mes 5-6:   v1.5 (engagement)
            - 10 tipos de desafíos
            - Insights semanales
            - Health Connect
            - Idiomas (ES, PT, EN)

Mes 7-8:   v2.0 (social + AI)
            - Buddy alarms
            - AI Challenge Generator (LLM)
            - Leaderboards
            - Widget home screen
```

---

## 11. Métricas de Éxito (v1.0)

| Métrica | Target |
|---|---|
| Instalaciones totales | 10K en 30 días post-launch |
| Retención D1 | > 40% |
| Retención D7 | > 20% |
| Streak completion rate | > 60% |
| Avg time to complete challenge | 45-90 segundos |
| Crash-free sessions | > 99.5% |
| Store rating | > 4.3 |
| Reviews en 30 días | > 50 |

---

*Documento vivo — actualizar conforme evolucione el desarrollo.*
*Creado: 2026-04-01*
