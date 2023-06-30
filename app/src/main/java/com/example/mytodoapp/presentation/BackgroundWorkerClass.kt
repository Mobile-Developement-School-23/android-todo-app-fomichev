package com.example.mytodoapp.presentation

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mytodoapp.domain.TodoItemsRepository

class BackgroundWorkerClass constructor(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: TodoItemsRepository
) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            repository.syncLocalListOfTodo()
            Result.success()
        } catch (e: Exception){
            Result.retry()
        }
    }
}
