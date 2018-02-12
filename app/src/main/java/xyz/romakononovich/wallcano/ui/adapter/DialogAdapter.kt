package xyz.romakononovich.wallcano.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.orhanobut.dialogplus.ViewHolder
import xyz.romakononovich.wallcano.R
import android.widget.TextView
import xyz.romakononovich.wallcano.mvp.model.CategoryDialog


/**
 * Created by romank on 11.02.18.
 */
class DialogAdapter(private val list: ArrayList<CategoryDialog>, context: Context) : BaseAdapter() {
    private val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view==null){
            view = layoutInflater.inflate(R.layout.dialog_item,parent,false)
            viewHolder = ViewHolder()
            viewHolder.imageView = view.findViewById(R.id.iv_dialog)
            viewHolder.textView = view.findViewById(R.id.tv_dialog)
            view.tag = viewHolder
        } else{
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.textView.text = list[position].name
        viewHolder.imageView.setImageResource(list[position].iconResId)
        return view!!
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    internal class ViewHolder {
        lateinit var textView: TextView
        lateinit var imageView: ImageView
    }
}