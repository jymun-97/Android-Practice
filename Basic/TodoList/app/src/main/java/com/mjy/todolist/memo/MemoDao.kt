package com.mjy.todolist.memo

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface MemoDao {
    @Insert(onConflict = REPLACE)
    fun insert(memoItem: MemoItem)

    @Query("SELECT * FROM memo")
    fun getAll(): List<MemoItem>

    @Delete
    fun delete(memoItem: MemoItem)

    @Update
    fun update(memoItem: MemoItem)
}