package xyz.romakononovich.wallcano.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import xyz.romakononovich.wallcano.mvp.model.HitsItem
import java.util.ArrayList

/**
 * Created by romank on 10.02.18.
 */
@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView: MvpView {

    fun onWallpapersLoaded(list: ArrayList<HitsItem>)

    fun updateView()

    fun showProgressBar()

    fun hideProgressBar()

    fun showDialogCategory()

    fun clearRV()

    fun isLoading(isLoading: Boolean)

    fun showToast(text: String)

    fun setIconDialog (iconResId: Int)

}