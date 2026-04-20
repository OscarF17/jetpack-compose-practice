package com.example.todoapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.TodoItem

@Composable
fun EntryList(
    todoItems: Map<Int, TodoItem>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onSelectEntry: (Int) -> Unit,
    onRowLongClick: (Int) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
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
                        onClick = {
                            onSelectEntry(entry.id)
                            println("clicked") },
                        onLongClick = {
                            onRowLongClick(entry.id)
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
                        onSelectEntry(id)
                    },
                )
                Text(
                    text = entry.text,
                    fontSize = 24.sp,
                )
            }
        }
    }
}
