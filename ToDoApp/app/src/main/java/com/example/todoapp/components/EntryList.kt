package com.example.todoapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.data.local.entities.TodoItem

@Composable
fun EntryList(
    todoItems: List<TodoItem>,
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

        itemsIndexed(items = todoItems) { index, entry ->
            val isFirstOrLastItem = entry.id == todoItems.size || entry.id == 1
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            onSelectEntry(index)
                            println("clicked") },
                        onLongClick = {
                            onRowLongClick(index)
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
                    checked = todoItems[index].state,
                    onCheckedChange = {
                        onSelectEntry(index)
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
