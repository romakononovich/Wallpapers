package xyz.romakononovich.wallcano.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.romakononovich.wallcano.mainactivity.model.Wallpapers

/**
 * Created by romank on 04.02.18.
 */
interface NetworkInterface{

    @GET("api/")
    abstract fun queryEvents(
            @Query("key") accessToken: String,
            @Query("orientation") orientation: String,
            @Query("image_type") type: String): Single<Wallpapers>
}