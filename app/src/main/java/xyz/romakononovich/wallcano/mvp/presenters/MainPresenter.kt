package xyz.romakononovich.wallcano.mvp.presenters

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import xyz.romakononovich.wallcano.App
import xyz.romakononovich.wallcano.Config
import xyz.romakononovich.wallcano.Constants
import xyz.romakononovich.wallcano.R
import xyz.romakononovich.wallcano.mvp.model.CategoryDialog
import xyz.romakononovich.wallcano.mvp.model.HitsItem
import xyz.romakononovich.wallcano.mvp.model.Wallpapers
import xyz.romakononovich.wallcano.mvp.views.MainView
import xyz.romakononovich.wallcano.network.NetworkInterface

/**
 * Created by romank on 10.02.18.
 */

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {
    private lateinit var compositeDisposable: CompositeDisposable
    private var listWallpapers = ArrayList<HitsItem>()
    private lateinit var networkInterface: NetworkInterface
    private var paginator = PublishProcessor.create<Int>()
    private var pageNumber = 1
    private var orderPosition = 0



    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        networkInterface = App.networkInterface!!
        viewState.showProgressBar()
        compositeDisposable = CompositeDisposable()
      //  requestTest()
       // subscribeForData(Constants.ORDER_POPULAR)
    }


//    private fun requestTest() {
//        viewState.showProgressBar()
//        compositeDisposable
//                .add(networkInterface.queryEvents(Config.ApiKey,
//                        Constants.ORIENTATION_VERTICAL,Constants.ORDER_LATEST,
//                        1,true,Constants.TYPE_PHOTO).subscribeOn(Schedulers.io())
//                        ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
//                    wallpapers -> viewState.onWallpapersLoaded(wallpapers.hits)
//                    viewState.hideProgressBar()
//                    listWallpapers.addAll(wallpapers.hits)}, {
//                    throwable -> Log.d("TAG", "Exception while querying events : " + throwable.message) })!!)
//
//    }
//
//    fun requestTest(page: Int) {
//        compositeDisposable
//                .add(networkInterface.queryEvents(Config.ApiKey,
//                        Constants.ORIENTATION_VERTICAL,Constants.ORDER_LATEST,
//                        page,true,Constants.TYPE_PHOTO).subscribeOn(Schedulers.io())
//                        ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
//                    wallpapers -> viewState.onWallpapersLoaded(wallpapers.hits)
//                    viewState.hideProgressBar()
//                    listWallpapers.addAll(wallpapers.hits)}, {
//                    throwable -> Log.d("TAG", "Exception while querying events : " + throwable.message) })!!)
//    }



    fun changeOrder(position: Int) {
        var order = Constants.ORDER_POPULAR
            viewState.clearRV()
            viewState.showProgressBar()
            orderPosition = position
            when (position) {
                0 -> order = Constants.ORDER_POPULAR
                1 -> order = Constants.ORDER_UPCOMING
                2 -> order = Constants.ORDER_LATEST
                3 -> order = Constants.ORDER_EDITORS_CHOICE
            }
            subscribeForData(order)
    }

    private fun subscribeForData(order: String) {
//        viewState.clearRV()
//        compositeDisposable.clear()
//        pageNumber = 1
//        paginator = null
//        paginator = PublishProcessor.create<Int>()
        val disposable = paginator
                .onBackpressureDrop()
                .concatMap { t ->
                    loadMoreData(t,order)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ wallpapers ->
                    viewState.onWallpapersLoaded(wallpapers.hits)
                    viewState.isLoading(false)
                    viewState.hideProgressBar()
                }, {throwable ->viewState.showToast(throwable.message.toString())})
        compositeDisposable.add(disposable)
        paginator.onNext(pageNumber)
    }
    fun loadNextPage(){
        pageNumber++
        paginator.onNext(pageNumber)
    }
    private fun loadMoreData(page: Int, order: String): Flowable<Wallpapers> {
        viewState.showToast("Order:"+order+"Page: "+page)
        return networkInterface.queryWallpapers(Config.ApiKey, Constants.ORIENTATION_VERTICAL,
                order, page, true, Constants.TYPE_PHOTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    internal fun initCategory() : ArrayList<CategoryDialog> {
        val list = ArrayList<CategoryDialog>()
        list.add(CategoryDialog(R.drawable.ic_category_animals, "Animals"))
        list.add(CategoryDialog(R.drawable.ic_category_backgrounds, "Backgrounds/Textures"))
        list.add(CategoryDialog(R.drawable.ic_category_buildings, "Architecture/Buildings"))
        list.add(CategoryDialog(R.drawable.ic_category_business, "Business/Finance"))
        list.add(CategoryDialog(R.drawable.ic_category_computer, "Computer/Communication"))
        list.add(CategoryDialog(R.drawable.ic_category_education, "Education"))
        list.add(CategoryDialog(R.drawable.ic_category_fashion, "Beauty/Fashion"))
        list.add(CategoryDialog(R.drawable.ic_category_feelings, "Emotions"))
        list.add(CategoryDialog(R.drawable.ic_category_food, "Food/Drink"))
        list.add(CategoryDialog(R.drawable.ic_category_health, "Health/Medical"))
        list.add(CategoryDialog(R.drawable.ic_category_industry, "Industry/Craft"))
        list.add(CategoryDialog(R.drawable.ic_category_music, "Music"))
        list.add(CategoryDialog(R.drawable.ic_category_nature, "Nature/Landscapes"))
        list.add(CategoryDialog(R.drawable.ic_category_people, "People"))
        list.add(CategoryDialog(R.drawable.ic_category_places, "Places/Monuments"))
        list.add(CategoryDialog(R.drawable.ic_category_religion, "Religion"))
        list.add(CategoryDialog(R.drawable.ic_category_science, "Science/Technology"))
        list.add(CategoryDialog(R.drawable.ic_category_transportation, "Transportation/Traffic"))
        list.add(CategoryDialog(R.drawable.ic_category_travel, "Travel/Vacation"))

        return list
    }
}