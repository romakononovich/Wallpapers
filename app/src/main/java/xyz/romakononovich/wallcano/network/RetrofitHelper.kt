package xyz.romakononovich.wallcano.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import xyz.romakononovich.wallcano.Config

/**
 * Created by romank on 04.02.18.
 */
class RetrofitHelper {
    private fun createOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()
            val url = originalHttpUrl.newBuilder()
                    .build()
            val requestBuilder = original.newBuilder()
                    .url(url)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return httpClient.build()
    }
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Config.urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build()
    }
    fun getNetworkService(): NetworkInterface {
        val retrofit = createRetrofit()
        return retrofit.create(NetworkInterface::class.java)
    }
}