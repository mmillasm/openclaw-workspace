package com.wakeup.ai.ui.screens.alarmeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wakeup.ai.domain.model.Alarm
import com.wakeup.ai.domain.model.ChallengeType
import com.wakeup.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmEditorScreen(
    alarmId: String?,
    onNavigateBack: () -> Unit,
    viewModel: AlarmEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditing = alarmId != null

    LaunchedEffect(alarmId) {
        alarmId?.let { viewModel.loadAlarm(it) }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Editar alarma" else "Nueva alarma",
                        color = OnSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Atrás", tint = OnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Time Picker
            TimePickerCard(
                hour = uiState.hour,
                minute = uiState.minute,
                onTimeChange = viewModel::setTime
            )

            // Days of week
            DaysOfWeekCard(
                selectedDays = uiState.selectedDays,
                onDayToggle = viewModel::toggleDay
            )

            // Challenge Type
            ChallengeTypeCard(
                selectedType = uiState.challengeType,
                onTypeSelect = viewModel::setChallengeType
            )

            // Label
            OutlinedTextField(
                value = uiState.label,
                onValueChange = viewModel::setLabel,
                label = { Text("Etiqueta (opcional)") },
                placeholder = { Text("ej: Trabajar, Gimnasio...") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = SurfaceVariant,
                    focusedLabelColor = Primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Save button
            Button(
                onClick = {
                    viewModel.saveAlarm()
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Save, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isEditing) "Guardar cambios" else "Crear alarma",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TimePickerCard(
    hour: Int,
    minute: Int,
    onTimeChange: (Int, Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Hour
                NumberPicker(
                    value = hour,
                    range = 0..23,
                    onValueChange = { onTimeChange(it, minute) }
                )

                Text(
                    ":",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Minute
                NumberPicker(
                    value = minute,
                    range = 0..59,
                    onValueChange = { onTimeChange(hour, it) }
                )
            }
        }
    }
}

@Composable
fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = {
                val new = if (value >= range.last) range.first else value + 1
                onValueChange(new)
            }
        ) {
            Icon(Icons.Default.KeyboardArrowUp, "Incrementar", tint = OnSurface)
        }

        Text(
            String.format("%02d", value),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = Primary,
            fontSize = 56.sp
        )

        IconButton(
            onClick = {
                val new = if (value <= range.first) range.last else value - 1
                onValueChange(new)
            }
        ) {
            Icon(Icons.Default.KeyboardArrowDown, "Decrementar", tint = OnSurface)
        }
    }
}

@Composable
fun DaysOfWeekCard(
    selectedDays: Set<Int>,
    onDayToggle: (Int) -> Unit
) {
    val days = listOf(
        1 to "D", 2 to "L", 3 to "M", 4 to "X", 5 to "J", 6 to "V", 7 to "S"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Repetir",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEach { (num, label) ->
                    val isSelected = selectedDays.contains(num)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Primary else SurfaceVariant)
                            .clickable { onDayToggle(num) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            color = if (isSelected) OnPrimary else TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengeTypeCard(
    selectedType: ChallengeType,
    onTypeSelect: (ChallengeType) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Tipo de desafío",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ChallengeType.entries) { type ->
                    val isSelected = type == selectedType
                    SuggestionChip(
                        onClick = { onTypeSelect(type) },
                        label = { Text(type.displayName) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = if (isSelected) Primary else SurfaceVariant,
                            labelColor = if (isSelected) OnPrimary else TextSecondary
                        ),
                        border = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                selectedType.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
