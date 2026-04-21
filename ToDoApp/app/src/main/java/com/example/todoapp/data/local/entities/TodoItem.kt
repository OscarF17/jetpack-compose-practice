package com.example.todoapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Let the database handle the ID generation for you!
    val text: String,
    val state: Boolean = false
)