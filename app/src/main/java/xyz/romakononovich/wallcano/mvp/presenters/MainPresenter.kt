package xyz.romakononovich.wallcano.mvp.presenters

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import xyz.romakononovich.wallcano.App
import xyz.romakononovich.wallcano.mvp.model.HitsItem
import xyz.romakononovich.wallcano.mvp.views.MainView
import xyz.romakononovich.wallcano.network.NetworkInterface

/**
 * Created by romank on 10.02.18.
 */

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {
    private lateinit var compositeDisposable: CompositeDisposable
    private var listWallpapers = ArrayList<HitsItem>()
    private var networkInterface: NetworkInterface? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        networkInterface = App.networkInterface
        viewState.showProgressBar()
        requestTest()
    }


    private fun requestTest() {
        compositeDisposable = CompositeDisposable()
        compositeDisposable
                .add(networkInterface?.queryEvents("7777255-2f5dd39fd3ed727de44fde30f",
                        "vertical","photo")?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
                    wallpapers -> viewState.onWallpapersLoaded(wallpapers.hits)
                    viewState.hideProgressBar()
                    listWallpapers.addAll(wallpapers.hits)}, {
                    throwable -> Log.d("TAG", "Exception while querying events : " + throwable.message) })!!)

    }
}