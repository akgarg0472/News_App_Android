package com.akgarg.newsapp

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class NewsListAdapter(private val clickListener: NewsItemClicked) :
    RecyclerView.Adapter<NewsViewHolder>() {
    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = NewsViewHolder(view)

        view.setOnClickListener {
            clickListener.onItemClicked(items[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title;
        holder.authorView.text = currentItem.author
        holder.progressBar.visibility = View.VISIBLE
        Glide.with(holder.itemView.context).load(currentItem.imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    holder.imageView.setImageResource(R.drawable.no_news_image_available)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false;
                }

            }).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNews(updatedItems: ArrayList<News>) {
        this.items.clear()
        this.items.addAll(updatedItems)

        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.newsImage)
    val titleView: TextView = itemView.findViewById(R.id.title)
    val authorView: TextView = itemView.findViewById(R.id.author)
    val progressBar: ProgressBar = itemView.findViewById(R.id.newsProgressBar)
}

interface NewsItemClicked {
    fun onItemClicked(item: News)
}