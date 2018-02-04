package xyz.romakononovich.wallcano.mainactivity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.romakononovich.wallcano.R

/**
 * Created by romank on 04.02.18.
 */
class RvAdapter(private val listNotes: ArrayList<String>) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_wallpaper, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener {
            //            val intent = Intent(view.context, EditNoteActivity::class.java)
//            intent.putExtra(TIMESTAMP, listNotes[holder.adapterPosition].timestamp)
//            view.context.startActivity(intent)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return 18
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems("test")
    }

    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun bindItems(str: String) {
//            v.tv_title.text = note.title
//            v.tv_note.text = note.note
//            v.tv_date.text = setDate(note.timestamp)
        }


    }
}