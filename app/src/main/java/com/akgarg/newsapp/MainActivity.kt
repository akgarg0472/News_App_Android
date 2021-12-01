package com.akgarg.newsapp

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: NewsListAdapter
    private lateinit var mainProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainProgressBar = findViewById(R.id.main_progress_bar)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = NewsListAdapter(this)
        generateData()
        recyclerView.adapter = mAdapter
    }

    private fun generateData() {
        val apiUrl =
            "https://saurav.tech/NewsAPI/top-headlines/category/technology/in.json"

        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, apiUrl, null, {
                try {
                    val newsJsonArray = it.getJSONArray("articles")
                    val newsArray = ArrayList<News>()

                    for (i in 0 until newsJsonArray.length()) {
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                            newsJsonObject.getString("title"),
                            newsJsonObject.getString("author"),
                            newsJsonObject.getString("url"),
                            newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }
                    mainProgressBar.visibility = View.GONE
                    mAdapter.updateNews(newsArray)
                } catch (e: Exception) {
                    mainProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Some internal error occurred", Toast.LENGTH_SHORT).show()
                }
            },
                {
                    mainProgressBar.visibility = View.GONE
                    if (it.networkResponse == null) {
                        Toast.makeText(this, "Your device is offline", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Error fetching news", Toast.LENGTH_LONG).show()
                    }
                })

        VolleySingletonClass.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val customTabIntentBuilder = CustomTabsIntent.Builder()
        val customTabIntent = customTabIntentBuilder.build()
        customTabIntent.launchUrl(this, Uri.parse(item.url))
    }
}