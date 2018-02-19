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
class MainPresenter : MvpPresenter<MainView>() {
    private lateinit var compositeDisposable: CompositeDisposable
    private var listWallpapers = ArrayList<HitsItem>()
    private lateinit var networkInterface: NetworkInterface
    private var paginator = PublishProcessor.create<Int>()
    private var pageNumber = 1
    private var orderPosition = 0
    private var categoryWallpapers = "all"


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        networkInterface = App.networkInterface!!
        viewState.showProgressBar()
        compositeDisposable = CompositeDisposable()
    }


    fun changeOrder(position: Int = orderPosition, category: String = categoryWallpapers) {
        var order = Constants.ORDER_POPULAR
        viewState.clearRV()
        viewState.showProgressBar()
        orderPosition = position
        categoryWallpapers = category
        when (position) {
            0 -> order = Constants.ORDER_POPULAR
            1 -> order = Constants.ORDER_UPCOMING
            2 -> order = Constants.ORDER_LATEST
            3 -> order = Constants.ORDER_EDITORS_CHOICE
        }
        subscribeForData(order, category)
    }

    private fun subscribeForData(order: String, category: String) {
        compositeDisposable.clear()
        pageNumber = 1
        paginator = null
        paginator = PublishProcessor.create<Int>()

        val disposable = paginator
                .onBackpressureDrop()
                .concatMap { t ->
                    loadMoreData(t, order, category)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ wallpapers ->
                    viewState.onWallpapersLoaded(wallpapers.hits)
                    viewState.isLoading(false)
                    viewState.hideProgressBar()
                }, { throwable -> viewState.showToast(throwable.message.toString()) })
        compositeDisposable.add(disposable)
        paginator.onNext(pageNumber)
    }

    fun loadNextPage() {
        viewState.isLoading(true)
        pageNumber++
        paginator.onNext(pageNumber)
    }

    private fun loadMoreData(page: Int, order: String, category: String): Flowable<Wallpapers> {
        viewState.showToast("Order:$order Page: $page Category: $category")
        return networkInterface.queryWallpapers(Config.ApiKey, Constants.ORIENTATION_VERTICAL,
                order, category, page, Constants.PER_PAGE, true, Constants.TYPE_PHOTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun setIconDialog(position: Int) {
        viewState.setIconDialog(initCategory()[position].iconResId)
    }

    fun changeCategory(position: Int) {
        changeOrder(category = initCategory()[position].category)
    }

    internal fun initCategory(): ArrayList<CategoryDialog> {
        val list = ArrayList<CategoryDialog>()
        list.add(CategoryDialog(R.drawable.ic_category_animals, "Animals", "animals"))
        list.add(CategoryDialog(R.drawable.ic_category_backgrounds, "Backgrounds/Textures", "backgrounds"))
        list.add(CategoryDialog(R.drawable.ic_category_buildings, "Architecture/Buildings", "buildings"))
        list.add(CategoryDialog(R.drawable.ic_category_business, "Business/Finance", "business"))
        list.add(CategoryDialog(R.drawable.ic_category_computer, "Computer/Communication", "computer"))
        list.add(CategoryDialog(R.drawable.ic_category_education, "Education", "education"))
        list.add(CategoryDialog(R.drawable.ic_category_fashion, "Beauty/Fashion", "fashion"))
        list.add(CategoryDialog(R.drawable.ic_category_feelings, "Emotions", "feelings"))
        list.add(CategoryDialog(R.drawable.ic_category_food, "Food/Drink", "food"))
        list.add(CategoryDialog(R.drawable.ic_category_health, "Health/Medical", "health"))
        list.add(CategoryDialog(R.drawable.ic_category_industry, "Industry/Craft", "industry"))
        list.add(CategoryDialog(R.drawable.ic_category_music, "Music", "music"))
        list.add(CategoryDialog(R.drawable.ic_category_nature, "Nature/Landscapes", "nature"))
        list.add(CategoryDialog(R.drawable.ic_category_people, "People", "people"))
        list.add(CategoryDialog(R.drawable.ic_category_places, "Places/Monuments", "places"))
        list.add(CategoryDialog(R.drawable.ic_category_religion, "Religion", "religion"))
        list.add(CategoryDialog(R.drawable.ic_category_science, "Science/Technology", "science"))
        list.add(CategoryDialog(R.drawable.ic_category_transportation, "Transportation/Traffic", "transportation"))
        list.add(CategoryDialog(R.drawable.ic_category_travel, "Travel/Vacation", "travel"))

        return list
    }
}