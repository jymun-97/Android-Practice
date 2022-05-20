package com.mjy.todolist.memo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoItem(
    @PrimaryKey (autoGenerate = true)
    val id: Int?,
    val memo: String,
    var isChecked: Boolean = false
)