package com.bxthre3.design.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextField
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors
import kotlinx.coroutines.delay

/**
 * BX3PredictiveInput — Intent-aware text input with AI suggestions
 * 
 * PATENT-PENDING: Predictive intent detection based on UI context,
 * historical user patterns, and semantic field analysis.
 * 
 * @param contextTags Current UI context for prediction (e.g., ["email", "contact-form"])
 * @param onIntentPredicted Callback when intent is predicted with confidence
 */
@Composable
fun BX3PredictiveInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    contextTags: List<String>,
    intentEngine: IntentPredictionEngine,
    onIntentPredicted: (predictedIntent: PredictedIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val predictions = remember { mutableStateListOf<Prediction>() }
    val isPredicting = remember { mutableStateOf(false) }
    val confidenceScore = remember { mutableStateOf(0f) }
    
    // Trigger prediction engine
    LaunchedEffect(value, contextTags) {
        if (value.length >= 3) {
            isPredicting.value = true
            delay(150) // Debounce
            
            val result = intentEngine.predict(value, contextTags)
            predictions.clear()
            predictions.addAll(result.suggestions)
            confidenceScore.value = result.confidence
            
            onIntentPredicted(PredictedIntent(
                intent = result.primaryIntent,
                confidence = result.confidence,
                suggestions = result.suggestions.map { it.completion }
            ))
            
            isPredicting.value = false
        } else {
            predictions.clear()
        }
    }
    
    Column(modifier = modifier.fillMaxWidth()) {
        BX3TextField(
            value = value,
            onValueChange = onValueChange,
            hint = hint,
            trailingIcon = if (isPredicting.value) {
                { /* Loading spinner */ }
            } else null
        )
        
        // Prediction suggestions
        AnimatedVisibility(
            visible = predictions.isNotEmpty() && confidenceScore.value > 0.6f,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .background(BX3Colors.surfaceVariant, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = BX3Colors.aiThinking,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BX3Text(
                        text = "Did you mean...",
                        style = BX3TextStyle.LabelLarge,
                        color = BX3Colors.aiThinking
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                predictions.take(3).forEach { prediction ->
                    PredictionChip(
                        prediction = prediction,
                        onClick = { 
                            onValueChange(prediction.completion)
                            predictions.clear()
                        }
                    )
                }
            }
        }
        
        // Intent indicator
        AnimatedVisibility(visible = confidenceScore.value > 0.8f) {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BX3Text(
                    text = "Detected: ${predictions.firstOrNull()?.intent ?: ""}",
                    style = BX3TextStyle.LabelSmall,
                    color = BX3Colors.success
                )
            }
        }
    }
}

@Composable
private fun PredictionChip(
    prediction: Prediction,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BX3Text(
            text = prediction.completion,
            style = BX3TextStyle.BodyMedium,
            color = BX3Colors.primary,
            modifier = Modifier.weight(1f)
        )
        BX3Text(
            text = "${(prediction.confidence * 100).toInt()}%",
            style = BX3TextStyle.LabelSmall,
            color = BX3Colors.onSurfaceVariant
        )
    }
}

data class Prediction(
    val completion: String,
    val intent: String,
    val confidence: Float
)

data class PredictedIntent(
    val intent: String,
    val confidence: Float,
    val suggestions: List<String>
)

interface IntentPredictionEngine {
    suspend fun predict(input: String, context: List<String>): PredictionResult
}

data class PredictionResult(
    val primaryIntent: String,
    val confidence: Float,
    val suggestions: List<Prediction>
)