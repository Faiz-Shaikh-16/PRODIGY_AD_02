package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {

//    @Query("SELECT * FROM TodoModel")
//    suspend fun getTasksDirectly(): List<TodoModel>

    @Insert()
    suspend fun insertTask(todoModel: TodoModel):Long

    @Query("""Select * from TodoModel""")
    fun getTask():LiveData<List<TodoModel>>

    @Query("Update TodoModel Set isFinished = 1 where id=:uid")
    fun finishTask(uid: Long)

    @Query("Delete from TodoModel where id=:uid")
    fun deleteTask(uid: Long)
}