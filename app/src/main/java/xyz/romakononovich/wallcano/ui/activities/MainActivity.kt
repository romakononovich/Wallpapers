package xyz.romakononovich.wallcano.ui.activities

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.orhanobut.dialogplus.*
import kotlinx.android.synthetic.main.toolbar.*
import xyz.romakononovich.wallcano.ui.adapter.DialogAdapter
import android.widget.AdapterView
import android.widget.Toast




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
    private lateinit var layoutManager: GridLayoutManager

    var previousTotal = 0
    var loading = true
    private val visibleThreshold = 24
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar(R.id.toolbar){
            setDisplayShowTitleEnabled(false)
        }
        initRV()
        initView()
        spinnerListener()
        loadMoreListener()
    }

    private fun spinnerListener() {
        spinner_nav.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.changeOrder(position)
            }

        }
    }


    private fun initRV() {
        layoutManager = GridLayoutManager(applicationContext,3)
        rv.layoutManager = layoutManager
        rvAdapter = RvAdapter()
        rv.adapter = rvAdapter
    }
    private fun initView(){
        iv_search.setOnClickListener(this)
        iv_category.setOnClickListener(this)
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
        val dialogPlus = DialogPlus.newDialog(this)
                .setAdapter(DialogAdapter(presenter.initCategory() ,this))
                .setHeader(R.layout.dialog_header)
                .setCancelable(true)
                .setOnItemClickListener { dialog, _, _, position ->
                    presenter.setIconDialog(position)
                    presenter.changeCategory(position)
                    dialog.dismiss()
                }
                .setContentBackgroundResource(R.color.colorPrimaryDark)
                .setExpanded(true)
                .create()
        dialogPlus.show()
    }

    override fun clearRV() {
        rvAdapter.clearAll()
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
            iv_category -> showDialogCategory()
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


    private fun loadMoreListener() {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView,
                                    dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = recyclerView.childCount
                totalItemCount = layoutManager.itemCount
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        //loading = false
                        previousTotal = totalItemCount
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loading = true
                    presenter.loadNextPage()
                }
            }
        })
    }

    override fun isLoading(isLoading: Boolean) {
        loading = isLoading
    }

    override fun showToast(text: String) {
       Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    override fun setIconDialog(iconResId: Int) {
        iv_category.setImageResource(iconResId)
    }

}

