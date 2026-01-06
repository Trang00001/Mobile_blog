package me.nhom8.blogapp.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import me.nhom8.blogapp.BuildConfig
import me.nhom8.blogapp.data.local.LocalUserDataSource
import me.nhom8.blogapp.data.remote.ApiService
import me.nhom8.blogapp.data.remote.adapter.CategoryEnumAdapter
import me.nhom8.blogapp.data.remote.adapter.LocalDateTimeAdapter
import me.nhom8.blogapp.data.remote.interceptor.AuthInterceptor
import me.nhom8.blogapp.repositories.AuthenticationRepository
import me.nhom8.blogapp.repositories.BlogRepository
import me.nhom8.blogapp.repositories.UserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
internal annotation class GoodBlogUrl

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @GoodBlogUrl
    fun goodClientUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideSharedPreference(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideLocalUserDataSource(sharedPreferences: SharedPreferences): LocalUserDataSource {
        return LocalUserDataSource(sharedPreferences)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .add(CategoryEnumAdapter())
            .add(LocalDateTimeAdapter())
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @GoodBlogUrl baseUrl: String,
        moshi: Moshi,
        client: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(baseUrl)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = ApiService(retrofit)

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        apiService: ApiService,
        localUserDataSource: LocalUserDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): AuthenticationRepository {
        return AuthenticationRepository(
            apiService = apiService,
            localDataSource = localUserDataSource,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideBlogRepository(
        apiService: ApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): BlogRepository {
        return BlogRepository(
            apiService = apiService,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService,
        localUserDataSource: LocalUserDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): UserRepository {
        return UserRepository(
            apiService = apiService,
            localUserDataSource = localUserDataSource,
            ioDispatcher = ioDispatcher,
        )
    }
}
