package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.todoapp.components.AlertDialogPopUp
import com.example.todoapp.components.EntryList
import com.example.todoapp.components.InputRow
import com.example.todoapp.components.TopBar
import com.example.todoapp.data.local.TodoDatabase
import com.example.todoapp.data.local.entities.TodoItem
import com.example.todoapp.ui.theme.ToDoAppTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    // We use 'lateinit' because we can't initialize it until onCreate runs
    private lateinit var database: TodoDatabase

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = TodoDatabase.getDatabase(applicationContext)
        val dao = database.todoDao()

        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            ToDoAppTheme {
                Scaffold(
                    topBar = { TopBar("ToDo App") },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val items by dao.getAllItems().collectAsState(initial = emptyList())

                    var textBox by remember { mutableStateOf<String>("") }

                    var isInitialized by remember { mutableStateOf(false) }
                    val listState = rememberLazyListState()
                    LaunchedEffect(items.size) {
                        if(isInitialized && items.isNotEmpty()) {
                            listState.animateScrollToItem(items.size - 1)
                        }
                        isInitialized = true
                    }

                    var showDialog by remember { mutableStateOf(false) }
                    var selectedEntryIdx by remember { mutableIntStateOf(-1) }

                    if(showDialog) {
                        AlertDialogPopUp(
                            title = "Delete entry?",
                            text = "Deleted entries cannot be recovered.",
                            onConfirm= {
                                scope.launch {
                                    dao.delete(items[selectedEntryIdx])
                                }
                                showDialog = false
                                isInitialized = false
                            },
                            confirmButtonText = "Delete",
                            onDismiss = {
                                showDialog = false
                            },
                            dismissButtonText = "Cancel"
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                    ) {
                        EntryList(
                            todoItems = items,
                            listState = listState,
                            // Toggle checkbox
                            onSelectEntry = { index ->
                                val currentItem = items[index]
                                val currentState = currentItem.state
                                val newItem = currentItem.copy(state = !currentState)
                                scope.launch {
                                    dao.update(newItem)
                                }
                            },
                            // Select to delete
                            onRowLongClick = { id ->
                                showDialog = true
                                selectedEntryIdx = id
                            },
                            modifier = Modifier.weight(1f)
                        )
                        InputRow(
                            textBox = textBox,
                            addElement = {
                                val newItem = TodoItem(text = textBox)
                                scope.launch {
                                    dao.insert(newItem)
                                }
                                textBox = ""
                            },
                            updateTextBox = { newVal ->
                                textBox = newVal
                            }
                        )
                    }
                }
            }
        }
    }
}