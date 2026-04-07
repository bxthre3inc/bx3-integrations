package com.bxthre3.design.templates.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Button
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.molecules.BX3Pagination

data class OnboardingStep(val title: String, val description: String, val icon: @Composable () -> Unit)

@Composable
fun BX3OnboardingTemplate(
    steps: List<OnboardingStep>,
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentStep = remember { mutableIntStateOf(0) }
    
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Crossfade(targetState = currentStep.intValue) { step ->
            val stepData = steps[step]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                stepData.icon()
                Spacer(modifier = Modifier.height(24.dp))
                BX3Text(
                    text = stepData.title,
                    style = BX3TextStyle.DisplayMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                BX3Text(
                    text = stepData.description,
                    style = BX3TextStyle.BodyLarge
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        BX3Pagination(
            totalPages = steps.size,
            currentPage = currentStep.intValue,
            onPageChange = { currentStep.intValue = it }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (currentStep.intValue == steps.size - 1) {
            BX3Button(
                text = "Get Started",
                onClick = onComplete
            )
        } else {
            BX3Button(
                text = "Skip",
                onClick = onSkip,
                variant = com.bxthre3.design.atoms.BX3ButtonVariant.GHOST
            )
        }
    }
}
