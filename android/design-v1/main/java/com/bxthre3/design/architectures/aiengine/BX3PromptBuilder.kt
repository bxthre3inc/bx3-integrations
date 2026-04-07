package com.bxthre3.design.architectures.aiengine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Button
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.molecules.BX3InputField
import com.bxthre3.design.organisms.BX3AIWidget
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3PromptBuilder(
    contextSnapshot: String,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val prompt = remember { mutableStateOf("") }
    
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        BX3Text(
            text = "AI Assistant",
            style = BX3TextStyle.HeadlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        BX3AIWidget(
            contextSnapshot = contextSnapshot,
            onPrompt = { onSubmit(prompt.value) },
            suggestedPrompts = listOf("Explain this", "Refactor", "Add tests"),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        BX3InputField(
            value = prompt.value,
            onValueChange = { prompt.value = it },
            hint = "Ask anything...",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        BX3Button(
            text = "Submit",
            onClick = { onSubmit(prompt.value) },
            variant = BX3ButtonVariant.PRIMARY
        )
    }
}
