package com.example.mytodoapp.data.network

import com.example.mytodoapp.data.api.GetListApiResponse
import com.example.mytodoapp.data.api.PatchListApiRequest
import com.example.mytodoapp.data.api.PostItemApiRequest
import com.example.mytodoapp.data.api.PostItemApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitService {
    @GET("list")
    suspend fun getList(): Response<GetListApiResponse>

    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body list: PatchListApiRequest
    ): Response<GetListApiResponse>

    @PUT("list/{id}")
    suspend fun updateElement(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body item: PostItemApiRequest
    ): Response<PostItemApiResponse>
    @POST("list")
    suspend fun postElement(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body element: PostItemApiRequest
    ): Response<PostItemApiResponse>

    @DELETE("list/{id}")
    suspend fun deleteElement(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
    ): Response<PostItemApiResponse>

}