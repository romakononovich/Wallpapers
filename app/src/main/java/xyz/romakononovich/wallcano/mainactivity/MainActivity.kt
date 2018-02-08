package xyz.romakononovich.wallcano.mainactivity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import xyz.romakononovich.wallcano.BaseActivity
import xyz.romakononovich.wallcano.R
import xyz.romakononovich.wallcano.mainactivity.adapter.RvAdapter
import xyz.romakononovich.wallcano.mainactivity.model.HitsItem
import xyz.romakononovich.wallcano.util.setupActionBar

class MainActivity : BaseActivity() {
    private lateinit var rvAdapter: RvAdapter
    private lateinit var presenter: MainActivityPresenter

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
        rv.layoutManager = GridLayoutManager(applicationContext,3)
        rvAdapter = RvAdapter()
        rv.adapter = rvAdapter
    }


    internal fun addData(list: ArrayList<HitsItem>){
        rvAdapter.addData(list)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.action_filter)
        val menuSearch = menu.findItem(R.id.action_search)
        menuItem.setOnMenuItemClickListener {
            onBackPressed()
            true
        }
        iv_search.setOnClickListener {
            menuSearch.isEnabled = true
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

    override fun onPause() {
        super.onPause()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }
}


