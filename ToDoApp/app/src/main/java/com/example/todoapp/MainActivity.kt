package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.ui.theme.RedBrand
import com.example.todoapp.ui.theme.ToDoAppTheme

data class TodoItem(
    val id: Int,
    val text: String,
    var state: Boolean = false
)

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
                             colors = TopAppBarDefaults.topAppBarColors(containerColor = RedBrand),
                            modifier = Modifier.height(80.dp)
                        )
                    },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val x = 40
                    var textBox by remember { mutableStateOf<String>("") }
                    val todoItems = remember { mutableStateMapOf<Int, TodoItem>().apply {
                        for(i in 1..x) {
                            put(i, TodoItem(id = i, text = i.toString(), state = false))
                        }
                    } }
                    var isInitialized by remember { mutableStateOf(false) }
                    var nextId by remember { mutableIntStateOf(todoItems.size + 1) }
                    val listState = rememberLazyListState()
                    LaunchedEffect(todoItems.size) {
                        if(isInitialized && todoItems.isNotEmpty()) {
                            listState.animateScrollToItem(todoItems.size - 1)
                        }
                        isInitialized = true
                    }
                    var showDialog by remember { mutableStateOf(false) }
                    var selectedEntry by remember { mutableIntStateOf(-1) }
                    if(showDialog) {
                        AlertDialog(
                            onDismissRequest = {},
                            title = {Text(text = "Delete entry?", fontWeight = FontWeight.Bold)},
                            text = {Text("Deleted entries cannot be recovered.")},
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        todoItems.remove(selectedEntry)
                                        showDialog = false
                                        isInitialized = false
                                    },
                                ) {
                                    Text(text = "Execute", color = RedBrand)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {showDialog = false}) {
                                    Text(text = "Close", color = Color.DarkGray)
                                }
                            },
                            icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {

                            val sortedList = todoItems.toList().sortedBy { it.first}
                            items(items = sortedList, key = {it.first}) { (id, entry) ->
                                val isFirstOrLastItem = id == todoItems.size || id == 1
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = { println("small click on $entry") },
                                            onLongClick = {
                                                showDialog = true
                                                selectedEntry = entry.id
                                                println("Selected $entry")
                                            },
                                        )
                                        .then(
                                            if(!isFirstOrLastItem){
                                                Modifier.border(width = 0.1.dp, color = Color.LightGray)
                                            } else {
                                                Modifier
                                            }
                                        )
                                ) {
                                    Checkbox(
                                        checked = todoItems[id]?.state ?: false,
                                        onCheckedChange = {
                                            val currentItem = todoItems[id]
                                            if(currentItem != null) {
                                                todoItems[id] = currentItem.copy(state = !currentItem.state)
                                            }
                                        },
                                    )
                                    Text(
                                        text = entry.text,
                                        fontSize = 24.sp,
                                    )
                                }
                            }
                        }
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
                                    textBox = newVal
                                },
                                placeholder = { Text(text = "Write something...") },
                                singleLine = true,
                                modifier = Modifier.onKeyEvent { event ->
                                    if(event.key == Key.Enter && event.type == KeyEventType.KeyDown && textBox != "") {
                                        todoItems[nextId] = TodoItem(id = nextId, text = textBox)

                                        nextId++
                                        textBox = ""
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
                                           todoItems[nextId] = TodoItem(id = nextId, text = textBox)
                                           nextId++
                                           textBox = ""
                                       }
                                    },
                                ){
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