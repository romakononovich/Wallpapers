package xyz.romakononovich.wallcano.ui.activities

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import xyz.romakononovich.wallcano.Constants
import xyz.romakononovich.wallcano.R
import xyz.romakononovich.wallcano.ui.adapter.RvAdapter
import xyz.romakononovich.wallcano.mvp.model.HitsItem
import xyz.romakononovich.wallcano.mvp.presenters.MainPresenter
import xyz.romakononovich.wallcano.mvp.views.MainView
import xyz.romakononovich.wallcano.util.setupActionBar
import java.util.ArrayList

/**
 * Created by romank on 10.02.18.
 */
class MainActivity: MvpAppCompatActivity(), MainView,
        MenuItem.OnActionExpandListener,
        View.OnClickListener{


    @InjectPresenter
    lateinit var presenter: MainPresenter
    private lateinit var rvAdapter: RvAdapter
    private lateinit var menuSearch: MenuItem
    private lateinit var menuItem : MenuItem
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar(R.id.toolbar){
            setDisplayShowTitleEnabled(false)
        }
        initRV()
        initView()
    }


    private fun initRV() {
        rv.layoutManager = GridLayoutManager(applicationContext,2)
        rvAdapter = RvAdapter()
        rv.adapter = rvAdapter
    }
    private fun initView(){
        iv_search.setOnClickListener(this)
    }
    override fun onWallpapersLoaded(list: ArrayList<HitsItem>) {
        rvAdapter.addData(list)
        updateView()
    }

    override fun updateView() {
        rvAdapter.notifyDataSetChanged()
    }

    override fun showProgressBar() {
        fl_progressbar.visibility = View.VISIBLE
        rotateloading.start()
    }

    override fun hideProgressBar() {
        fl_progressbar.visibility = View.GONE
        rotateloading.stop()
    }

    override fun showDialogCategory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideDialogCategory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menuItem = menu.findItem(R.id.action_filter)
        menuSearch = menu.findItem(R.id.action_search)
        searchView = menuSearch.actionView as SearchView
        searchView.queryHint = Constants.SEARCH_VIEW_HINT
        menuItem.setOnMenuItemClickListener {
            onBackPressed()
            true
        }
        menuSearch.setOnActionExpandListener(this)
        return true

    }

    //clickListener
    override fun onClick(v: View?) {
        when (v){
           iv_search ->  {
               menuItem.isVisible = false
               menuSearch.isVisible = true
               searchView.setIconifiedByDefault(true)
               searchView.isFocusable = true
               searchView.isIconified = false
               menuSearch.expandActionView()
           }
        }
    }

    //searchView open
    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
    }

    //searchView close
    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        menuItem.isVisible = true
        menuSearch.isVisible = false
        return true
    }

}

