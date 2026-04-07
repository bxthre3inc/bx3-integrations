package com.bxthre3.design.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextIntent
import com.bxthre3.design.atoms.BX3TextSize
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3InputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    
    val borderColor = when {
        isError -> BX3Colors.error
        isFocused -> BX3Colors.primary
        else -> BX3Colors.border
    }
    
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            BX3Text(
                text = label,
                size = BX3TextSize.SMALL,
                intent = BX3TextIntent.SECONDARY,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .background(BX3Colors.surface, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            decorationBox = { innerTextField ->
                if (value.isEmpty() && placeholder != null) {
                    BX3Text(
                        text = placeholder,
                        intent = BX3TextIntent.SUBTLE
                    )
                }
                innerTextField()
            }
        )
        
        if (isError && errorMessage != null) {
            BX3Text(
                text = errorMessage,
                size = BX3TextSize.SMALL,
                intent = BX3TextIntent.ERROR,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
