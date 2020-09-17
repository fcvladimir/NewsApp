package com.test.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.domain.model.New
import kotlinx.android.synthetic.main.item_new.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(private val newsAdapterListener: NewsAdapterListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var newsList = mutableListOf<New>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindNewsComments(getItem(position), (holder as NewsViewHolder).itemView)
    }

    override fun getItemCount() = newsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NewsViewHolder(layoutInflater.inflate(R.layout.item_new, parent, false))
    }

    private fun bindNewsComments(new: New, itemView: View) {
        itemView.run {
            tvNewTitle.text = new.title
            tvNewSource.text = if (new.source?.id == null) {
                "N/A"
            } else {
                new.source?.name
            }
            tvNewDate.text = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new.publishedAt)
            onClick {
                newsAdapterListener?.onNewClick(new)
            }
        }
    }

    private fun getItem(position: Int) = newsList[position]

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface NewsAdapterListener {
        fun onNewClick(new: New)
    }
}