package xyz.romakononovich.wallcano.network

import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.romakononovich.wallcano.mvp.model.Wallpapers

/**
 * Created by romank on 04.02.18.
 */
interface NetworkInterface{

    @GET("api/")
    abstract fun queryEvents(
            @Query("key") accessToken: String,
            @Query("orientation") orientation: String,
            @Query("order") order: String,
            @Query("page") page: Int,
            @Query("safesearch") safesearch: Boolean,
            @Query("image_type") type: String): Single<Wallpapers>

    @GET("api/")
    fun queryWallpapers(
            @Query("key") accessToken: String,
            @Query("orientation") orientation: String,
            @Query("order") order: String,
            @Query("page") page: Int,
            @Query("safesearch") safesearch: Boolean,
            @Query("image_type") type: String): Flowable<Wallpapers>
}