package com.example.mytodoapp.data

import android.util.Log
import kotlinx.coroutines.withContext
import com.example.mytodoapp.data.network.Token
import com.example.mytodoapp.data.network.NetworkAccess
import com.example.mytodoapp.data.api.PatchListApiRequest
import com.example.mytodoapp.data.api.PostItemApiRequest
import com.example.mytodoapp.data.api.PostItemApiResponse
import com.example.mytodoapp.data.api.TodoItemResponse
import com.example.mytodoapp.data.mappers.TodoItemResponseMapper
import com.example.mytodoapp.data.db.TodoLocalDbImpl
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * The TodoListRepositoryImpl class is responsible for managing the interactions
 * between the local database,
 * the SharedPreferencesHelper, and the remote server API for TodoList items.
 * It implements the TodoItemsRepository
 * interface and provides methods for adding, deleting, editing, and retrieving
 * TodoItems from the database.
 * The class ensures synchronization between the local and remote data sources.
 */
class TodoListRepositoryImpl @Inject constructor(
    private val todoLocalDbImpl: TodoLocalDbImpl,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val todoItemResponseMapper: TodoItemResponseMapper
) : TodoItemsRepository {

    private val service = Token.retrofitApi



    override suspend fun addTodoItem(todoItem: TodoItem) {
        Log.d("MyLog", "addTodoItem in TodoListRepositoryImpl")
        todoLocalDbImpl.addTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        Log.d("MyLog", "(deleteTodoItem in TodoListRepositoryImpl)")

        todoLocalDbImpl.deleteTodoItem(todoItem)
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        Log.d("MyLog", "editTodoItem in TodoListRepositoryImpl")
        todoLocalDbImpl.editTodoItem(todoItem)
    }

    override suspend fun getTodoItem(todoItemId: String): TodoItem {
        Log.d("MyLog", "getTodoItem in TodoListRepositoryImpl")
        return todoLocalDbImpl.getTodoItem(todoItemId)
    }

    fun getAllData(): Flow<List<TodoItem>> = todoLocalDbImpl.getAllData()
        .map { list -> list.map { it.toItem() } }




    suspend fun postNetworkItem(
        lastRevision: Int,
        newItem: TodoItem
    ): NetworkAccess<PostItemApiResponse> {
        val postResponse = service.postElement(
            lastRevision,
            PostItemApiRequest(todoItemResponseMapper.mapToTodoItemResponse(newItem))
        )
        if (postResponse.isSuccessful) {
            val responseBody = postResponse.body()
            if (responseBody != null) return NetworkAccess.Success(responseBody)
        }
        return NetworkAccess.Error(postResponse)
    }

    suspend fun deleteNetworkItem(
        lastRevision: Int,
        id: String
    ): NetworkAccess<PostItemApiResponse> {
            val postResponse = service.deleteElement(id, lastRevision)

            if (postResponse.isSuccessful) {
                val responseBody = postResponse.body()
                if (responseBody != null) return NetworkAccess.Success(responseBody)
            }
            return NetworkAccess.Error(postResponse)
        }


    suspend fun updateNetworkItem(
        lastRevision: Int,
        item: TodoItem
    ) = withContext(Dispatchers.IO) {
        val updateItemResponse = service.updateElement(
            item.id, lastRevision, PostItemApiRequest(
                todoItemResponseMapper.mapToTodoItemResponse(item)
            )
        )
        if (updateItemResponse.isSuccessful) {
            val body = updateItemResponse.body()
            if (body != null) sharedPreferencesHelper.putRevision(body.revision)
        }
    }

    suspend fun getNetworkData() {
        val networkListResponse = service.getList()
        if (networkListResponse.isSuccessful) {
            val body = networkListResponse.body()
            if (body != null) {
                val networkList = body.list
                val currentList = todoLocalDbImpl.getAll()
                    .map { todoItemResponseMapper.mapToTodoItemResponse(it.toItem()) }
                val mergedList = HashMap<String, TodoItemResponse>()
                for (item in networkList) mergedList[item.id] = item
                for (item in currentList) {
                    if (mergedList.containsKey(item.id)) {
                        val item1 = mergedList[item.id]
                        if (item.dateChanged > item1!!.dateChanged) mergedList[item.id] = item
                         else mergedList[item.id] = item1
                    } else mergedList[item.id] = item
                }
                updateNetworkList(mergedList.values.toList())
            }
        }
    }

    override suspend fun syncListOfTodo() = getNetworkData()
    private suspend fun updateNetworkList(mergedList: List<TodoItemResponse>) {

            val updateResponse = service.updateList(
                sharedPreferencesHelper.getLastRevision(),
                PatchListApiRequest(mergedList)
            )
            if (updateResponse.isSuccessful) {
                val responseBody = updateResponse.body()
                if (responseBody != null) {
                    sharedPreferencesHelper.putRevision(responseBody.revision)
                    updateRoom(responseBody.list)
                }
            }
        }


    private suspend fun updateRoom(response: List<TodoItemResponse>) {
        Log.d("MyLog", "updateRoom in TodoListRepositoryImpl")
        val list = response.map { it.toItem() }
        todoLocalDbImpl.addList(list)
    }
}

