package xyz.romakononovich.wallcano.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_wallpaper.view.*
import xyz.romakononovich.wallcano.R
import xyz.romakononovich.wallcano.mvp.model.HitsItem

/**
 * Created by romank on 04.02.18.
 */
class RvAdapter : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    private var listWallpapers = ArrayList<HitsItem>()
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_wallpaper, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener {
            //            val intent = Intent(view.context, EditNoteActivity::class.java)
//            intent.putExtra(TIMESTAMP, listWallpapers[holder.adapterPosition].timestamp)
//            view.context.startActivity(intent)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return listWallpapers.size
    }
    fun addData(list: ArrayList<HitsItem>){
        listWallpapers.addAll(list)
        notifyDataSetChanged()
    }
    fun clearAll(){
        listWallpapers.clear()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(listWallpapers[position])
    }

    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun bindItems(wallpapers: HitsItem) {
            Picasso.with(v.context)
                    .load(wallpapers.webformatURL)
                    .fit()
                    .centerCrop()
                    .into(v.iv_preview)
        }


    }
}