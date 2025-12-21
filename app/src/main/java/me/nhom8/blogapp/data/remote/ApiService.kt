package me.nhom8.blogapp.data.remote

import me.nhom8.blogapp.data.remote.model.body.auth.LoginUserBody
import me.nhom8.blogapp.data.remote.model.body.auth.RegisterUserBody
import me.nhom8.blogapp.data.remote.model.response.BaseResponse
import me.nhom8.blogapp.data.remote.model.response.auth.LoginUserResponse
import me.nhom8.blogapp.data.remote.model.response.blog.BlogResponse
import me.nhom8.blogapp.data.remote.model.response.user.UserResponse
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import me.nhom8.blogapp.data.remote.model.body.image.SaveImageBody
import me.nhom8.blogapp.data.remote.model.response.image.SaveImageResponse


interface ApiService {
    @Headers("@: NoAuth")
    @POST("api/auth/login")
    suspend fun login(
        @Body body: LoginUserBody,
    ): BaseResponse<LoginUserResponse>

    @Headers("@: NoAuth")
    @POST("api/auth/register")
    suspend fun register(
        @Body body: RegisterUserBody,
    ): BaseResponse<Any>

    @GET("api/blogs")
    suspend fun getBlogs(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): BaseResponse<List<BlogResponse>>

    @GET("api/users/{id}/profiles")
    suspend fun getUserById(
        @Path("id") id: String,
    ): BaseResponse<UserResponse>

    @POST("api/images")
    suspend fun saveImageUrl(
        @Body body: SaveImageBody
    ): BaseResponse<Any>


    companion object Factory {
        operator fun invoke(retrofit: Retrofit): ApiService = retrofit.create()
    }
}
