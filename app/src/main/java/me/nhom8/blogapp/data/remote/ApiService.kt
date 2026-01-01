package me.nhom8.blogapp.data.remote

import me.nhom8.blogapp.data.remote.model.body.auth.LoginUserBody
import me.nhom8.blogapp.data.remote.model.body.auth.RegisterUserBody
import me.nhom8.blogapp.data.remote.model.body.blog.UpsertBlogBody
import me.nhom8.blogapp.data.remote.model.body.image.SaveImageBody
import me.nhom8.blogapp.data.remote.model.response.BaseResponse
import me.nhom8.blogapp.data.remote.model.response.auth.LoginUserResponse
import me.nhom8.blogapp.data.remote.model.response.blog.BlogResponse
import me.nhom8.blogapp.data.remote.model.response.user.UserResponse
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @POST("api/blogs")
    suspend fun createBlog(
        @Body body: UpsertBlogBody,
    ): BaseResponse<BlogResponse>

    @PUT("api/blogs/{id}")
    suspend fun updateBlog(
        @Path("id") id: String,
        @Body body: UpsertBlogBody,
    ): BaseResponse<BlogResponse>

    @DELETE("api/blogs/{id}")
    suspend fun deleteBlog(
        @Path("id") id: String,
    ): BaseResponse<Any>

    @GET("api/users/{id}/profiles")
    suspend fun getUserById(
        @Path("id") id: String,
    ): BaseResponse<UserResponse>

    // Giữ lại API này (nếu backend có) - nhưng luồng tạo blog hiện tại sẽ gửi image_url cùng payload blog.
    @POST("api/images")
    suspend fun saveImageUrl(
        @Body body: SaveImageBody,
    ): BaseResponse<Any>

    companion object Factory {
        operator fun invoke(retrofit: Retrofit): ApiService = retrofit.create()
    }
}
