package xyz.romakononovich.wallcano

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDexApplication
import com.google.gson.Gson
import xyz.romakononovich.wallcano.network.NetworkInterface
import xyz.romakononovich.wallcano.network.RetrofitHelper

/**
 * Created by romank on 04.02.18.
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        gson = Gson()
        networkInterface = RetrofitHelper().getNetworkService()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set
        var gson: Gson? = null
            private set
        var networkInterface: NetworkInterface? = null
            private set
    }
}