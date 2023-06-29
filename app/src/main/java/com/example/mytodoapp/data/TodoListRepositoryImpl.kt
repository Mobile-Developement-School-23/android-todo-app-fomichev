package com.example.mytodoapp.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mytodoapp.data.network.Common
import com.example.mytodoapp.data.network.NetworkAccess
import com.example.mytodoapp.data.network.SharedPreferencesHelper
import com.example.mytodoapp.data.network.TodoItemDbModel
import com.example.mytodoapp.data.network.response.PatchListApiRequest
import com.example.mytodoapp.data.network.response.PostItemApiRequest
import com.example.mytodoapp.data.network.response.PostItemApiResponse
import com.example.mytodoapp.data.network.response.TodoItemResponse
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoListRepositoryImpl(
    application: Application
) : TodoItemsRepository {
    private val service = Common.retrofitService
    private val todoListDao = AppDataBase.getInstance(application).todoListDao()
    private val mapper = TodoListMapper()
    private val sharedPreferencesHelper = SharedPreferencesHelper(application)
    private val db= AppDataBase
    private val dao = db.getInstance(application).todoListDao()
    override suspend fun addTodoItem(todoItem: TodoItem) {
        todoListDao.addShopItem(mapper.mapEntityToDbModel(todoItem))
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoListDao.deleteShopItem(todoItem.id)
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        todoListDao.addShopItem(mapper.mapEntityToDbModel(todoItem))
    }

    override suspend fun getTodoItem(todoItemId: String): TodoItem {
        val dbModel = todoListDao.getShopItem(todoItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getTodoList(): LiveData<List<TodoItem>> = MediatorLiveData<List<TodoItem>>().apply {
        addSource(todoListDao.getShopList()) {
            value = mapper.mapListDbModelToListEntity(it)
        }
    }



    suspend fun postNetworkItem(
        lastRevision: Int,
        newItem: TodoItem
    ): NetworkAccess<PostItemApiResponse> {
        val postResponse = service.postElement(
            lastRevision,
            PostItemApiRequest(TodoItemResponse.fromItem(newItem))
        )

        if (postResponse.isSuccessful) {
            val responseBody = postResponse.body()
            if (responseBody != null) {
                return NetworkAccess.Success(responseBody)
            }
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
            if (responseBody != null) {
                return NetworkAccess.Success(responseBody)
            }
        }
        return NetworkAccess.Error(postResponse)
    }

    suspend fun updateNetworkItem(
        lastRevision: Int,
        item: TodoItem
    ) = withContext(Dispatchers.IO) {

        val updateItemResponse = service.updateElement(
            item.id, lastRevision, PostItemApiRequest(
                TodoItemResponse.fromItem(item)
            )
        )
        if (updateItemResponse.isSuccessful) {
            val body = updateItemResponse.body()
            if (body != null) {
                sharedPreferencesHelper.putRevision(body.revision)
            }
        }
    }

    suspend fun getNetworkData() {
        val networkListResponse = service.getList()


        if (networkListResponse.isSuccessful) {
            val body = networkListResponse.body()
            if (body != null) {
                val networkList = body.list
                val currentList = dao.getAll().map { TodoItemResponse.fromItem(it.toItem()) }
                val mergedList = HashMap<String, TodoItemResponse>()

                for(item in networkList){
                    mergedList[item.id] = item
                    Log.d("1", "${item.id} ${item.dateChanged}")
                }
                for (item in currentList) {
                    if (mergedList.containsKey(item.id)) {
                        val item1 = mergedList[item.id]
                        if (item.dateChanged > item1!!.dateChanged) {
                            mergedList[item.id] = item
                        }else{
                            mergedList[item.id] = item1
                        }
                    }else{
                        mergedList[item.id] = item
                    }
                }

                updateNetworkList(mergedList.values.toList())
            }
        }
    }

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
        val list = response.map { it.toItem() }
        dao.addList(list.map { TodoItemDbModel.fromItem(it) })
    }
}

