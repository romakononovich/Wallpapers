package xyz.romakononovich.wallcano.mainactivity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import xyz.romakononovich.wallcano.BaseActivity
import xyz.romakononovich.wallcano.R
import xyz.romakononovich.wallcano.mainactivity.adapter.RvAdapter
import xyz.romakononovich.wallcano.mainactivity.model.HitsItem
import xyz.romakononovich.wallcano.util.setupActionBar
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : BaseActivity() {
    private lateinit var rvAdapter: RvAdapter
    private lateinit var presenter: MainActivityPresenter
    private lateinit var menuSearch: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar(R.id.toolbar){
            setDisplayShowTitleEnabled(false)
        }

        initRV()
        presenter = MainActivityPresenter()
        presenter.attachView(this)
        if (savedInstanceState != null
                && savedInstanceState.containsKey("key")){
            presenter.setListWallpapers(savedInstanceState.getSerializable("key") as ArrayList<HitsItem>)
        } else {
            presenter.requestTest()
        }

    }

    private fun initRV() {
        rv.layoutManager = GridLayoutManager(applicationContext,2)
        rvAdapter = RvAdapter()
        rv.adapter = rvAdapter
    }


    internal fun addData(list: ArrayList<HitsItem>){
        rvAdapter.addData(list)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.action_filter)
        menuSearch = menu.findItem(R.id.action_search)
        val searchView: SearchView = menuSearch.actionView as SearchView
        searchView.queryHint = "Search here"
        menuItem.setOnMenuItemClickListener {
            onBackPressed()
            true
        }
        iv_search.setOnClickListener {
            menuItem.isVisible = false
            spinner_nav.visibility = View.GONE
            menuSearch.isVisible = true
            searchView.setIconifiedByDefault(true)
            searchView.isFocusable = true
            searchView.isIconified = false
        }

        searchView.setOnCloseListener {
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            menuItem.isVisible = true
            menuSearch.isVisible = false
            spinner_nav.visibility = View.VISIBLE
            true
        }
        return true

    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putSerializable("key",presenter.getListWallpapers())
        })
    }
}


