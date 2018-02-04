package xyz.romakononovich.wallcano.mainactivity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import xyz.romakononovich.wallcano.BaseActivity
import xyz.romakononovich.wallcano.R
import xyz.romakononovich.wallcano.mainactivity.adapter.RvAdapter
import java.util.ArrayList

class MainActivity : BaseActivity() {
    private lateinit var rvAdapter: RvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initRV()

    }

    private fun initRV() {
        rv.layoutManager = GridLayoutManager(applicationContext,3)
        rvAdapter = RvAdapter(object : ArrayList<String>() {

        })
        rv.adapter = rvAdapter
    }

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.action_filter)
        menuItem.setOnMenuItemClickListener {
            onBackPressed()
            true
        }
        return true

    }

}
