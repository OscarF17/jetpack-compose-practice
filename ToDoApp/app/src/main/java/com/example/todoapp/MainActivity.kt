package com.example.todoapp

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)

    fun initiateList(amount: Int): List<String> {
        var list = listOf<String>()
        for(i in 1..amount) {
            list = list + i.toString()
        }
        return list
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = "ToDo App",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 40.sp
                                )},
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Red),
                            modifier = Modifier.height(80.dp)
                        )
                    },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var textBox by remember { mutableStateOf<String>("") }
                    var entries by remember {mutableStateOf(initiateList(100))}
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            items(entries) { entry ->
                                Text(
                                    text = entry,
                                    modifier = Modifier
                                        .border(width = 1.dp, color = Color.Black)
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = {println("small click on $entry")},
                                            onLongClick = {println("long click on $entry")},
                                            onDoubleClick = {println("double click on $entry")}
                                        )
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            // Arrangement doesn't work if row doesn't take up whole width
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp, start = 10.dp, end = 10.dp)
                        ){
                            OutlinedTextField(
                                value = textBox,
                                onValueChange = { newVal ->
                                    textBox = newVal
                                },
                                placeholder = { Text(text = "Write something...") },
                                singleLine = true,
                                modifier = Modifier.onKeyEvent { event ->
                                    if(event.key == Key.Enter && event.type == KeyEventType.KeyDown && textBox != "") {
                                        entries = entries + textBox
                                        textBox = ""
                                        true
                                    }
                                    false
                                }
                            )
                            Button(
                                colors = buttonColors(containerColor = Color.Red),
                                onClick = {
                                    if(textBox != ""){
                                        entries = entries + textBox
                                        textBox = ""
                                    }
                                }) {
                                Text(
                                    text = "Click me",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}