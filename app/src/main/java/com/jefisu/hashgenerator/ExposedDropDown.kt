package com.jefisu.hashgenerator

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jefisu.hashgenerator.ui.theme.Downriver
import com.jefisu.hashgenerator.ui.theme.Turquoise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropDown(
    algorithms: List<String>,
    selectedAlgorithm: String,
    onClickSelectedAlgorithm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            singleLine = true,
            value = selectedAlgorithm,
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Turquoise
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Downriver,
                textColor = Color.White,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.LightGray,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            algorithms.forEach { algorithm ->
                DropdownMenuItem(
                    text = { Text(algorithm) },
                    onClick = {
                        onClickSelectedAlgorithm(algorithm)
                        expanded = false
                    }
                )
            }
        }
    }
}