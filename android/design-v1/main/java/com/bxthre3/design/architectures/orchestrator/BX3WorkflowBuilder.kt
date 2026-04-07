package com.bxthre3.design.architectures.orchestrator

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Button
import com.bxthre3.design.atoms.BX3ButtonVariant
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.molecules.BX3Stepper

data class WorkflowStep(val id: String, val label: String, val content: @Composable () -> Unit)

@Composable
fun BX3WorkflowBuilder(
    steps: List<WorkflowStep>,
    currentStep: Int,
    onStepChange: (Int) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        BX3Stepper(
            steps = steps.map { it.label },
            currentStep = currentStep,
            onStepClick = { /* readonly */ }
        )
        
        AnimatedContent(
            targetState = currentStep,
            modifier = Modifier.padding(vertical = 24.dp)
        ) { step ->
            steps.getOrNull(step)?.content?.invoke()
        }
        
        Row(modifier = Modifier.fillMaxWidth()) {
            if (currentStep > 0) {
                BX3Button(
                    text = "Back",
                    onClick = { onStepChange(currentStep - 1) },
                    variant = BX3ButtonVariant.SECONDARY
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (currentStep < steps.size - 1) {
                BX3Button(
                    text = "Next",
                    onClick = { onStepChange(currentStep + 1) }
                )
            } else {
                BX3Button(
                    text = "Complete",
                    onClick = onComplete
                )
            }
        }
    }
}
