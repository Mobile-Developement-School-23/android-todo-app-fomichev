package com.example.mytodoapp.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

//interface TodoService {
//
//
//    @GET("list")
//    suspend fun getTodoList(): TodoListResponse
//
//    @GET("list/{id}")
//    suspend fun getTodoItem(@Path("id") todoId: String
//    ): TodoResponse
//
//
//    @POST("list")
//    suspend fun saveTodoItem(@Body todoBody: TodoBody,
//                             @Header("X-Last-Known-Revision") revision: Int
//    ): TodoResponse
//
//
//
//    @PUT("list/{id}")
//    suspend fun editTodoItem(
//    @Path("id") todoId: String,
//    @Body todoBody: TodoBody,
//    @Header("X-Last-Known-Revision") revision: Int
//    ): TodoResponse
//
//    @DELETE("list/{id}")
//    suspend fun deleteTodoItem(
//        @Path("id") todoId: String,
//        @Header("X-Last-Known-Revision") revision: Int
//    ): TodoResponse
//}