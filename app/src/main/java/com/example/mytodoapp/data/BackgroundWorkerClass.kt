package com.example.mytodoapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mytodoapp.domain.TodoItemsRepository

/**
 * This class represents a background worker that performs a specific task related to syncing
 * a list of TodoItems.
 * The worker utilizes the TodoItemsRepository to perform the sync operation.
 */
class BackgroundWorkerClass constructor(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: TodoItemsRepository
) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            repository.syncListOfTodo()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
