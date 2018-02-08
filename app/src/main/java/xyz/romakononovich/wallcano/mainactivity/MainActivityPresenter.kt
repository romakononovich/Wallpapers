package xyz.romakononovich.wallcano.mainactivity

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import xyz.romakononovich.wallcano.App
import xyz.romakononovich.wallcano.mainactivity.model.HitsItem
import xyz.romakononovich.wallcano.network.NetworkInterface

/**
 * Created by romank on 04.02.18.
 */
class MainActivityPresenter {
    private var view: MainActivity? = null
    private var networkInterface: NetworkInterface? = null
    private var compositeDisposable: CompositeDisposable? = null
    private var listWallpapers = ArrayList<HitsItem>()

    fun attachView(mainActivity: MainActivity){
        view = mainActivity
        networkInterface = App.networkInterface
        //requestTest()
    }
    fun requestTest() {
        compositeDisposable = CompositeDisposable()
        compositeDisposable!!
                .add(networkInterface?.queryEvents("7777255-2f5dd39fd3ed727de44fde30f",
                        "vertical","photo")?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
                    wallpapers -> view!!.addData(wallpapers.hits)
                listWallpapers.addAll(wallpapers.hits)}, {
                    throwable -> Log.d("TAG", "Exception while querying events : " + throwable.message) })!!)

    }
    fun detachView() {
        view = null
        compositeDisposable?.clear()

    }
    fun getListWallpapers(): ArrayList<HitsItem> {
     return listWallpapers
    }
    fun setListWallpapers(list:ArrayList<HitsItem>){
        listWallpapers = list
        view!!.addData(listWallpapers)
    }


}