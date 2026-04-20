package com.example.todoapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.TodoItem
import com.example.todoapp.ui.theme.RedBrand
import kotlin.collections.set

@Composable
fun InputRow(
    textBox: String,
    addElement: () -> Unit,
    updateTextBox: (newVal: String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        // Arrangement doesn't work if row doesn't take up whole width
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 10.dp, end = 10.dp)
    ){
        OutlinedTextField(
            value = textBox,
            onValueChange = { newVal ->
                updateTextBox(newVal)
            },
            placeholder = { Text(text = "Write something...") },
            singleLine = true,
            modifier = Modifier.onKeyEvent { event ->
                if(event.key == Key.Enter && event.type == KeyEventType.KeyDown && textBox != "") {
                    addElement()
                    true
                }
                false
            }
        )
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            colors = buttonColors(containerColor = RedBrand),
            onClick = {
                if(textBox != ""){
                    addElement()
                }
            },
        ){
            Text(
                text = "Add",
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun InputRowPreview() {
    InputRow(
        textBox = "Hello",
        addElement = {},
        updateTextBox = {}
    )
}