package com.fetch.matthewomalley

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

class ListActivity : AppCompatActivity() {

    // create instance of request library
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity) // set layout for ListActivity (list_activity.xml)

        fetchListData() // start the data fetching
    }

    // group and sort of the list of items
    private fun groupAndSort(rawItems: List<ListItem>): List<ListItemDisplay> {
        return rawItems

            // filter out null names
            .filter { !it.name.isNullOrBlank() }

            // sort listItems by their listId, then for each items with the same listId, sort by name number
            .sortedWith(compareBy<ListItem> { it.listId }.thenBy { it.name?.substringAfter("Item ")?.toInt() })

            // turn the sorted list into a map (listId : ListItem) then turn each group into a list
            .groupBy { it.listId }
            .flatMap { (listId, items) ->
                listOf(ListItemDisplay(listId, null)) + // header
                        items.map { ListItemDisplay(null, it) } // each item
            }
    }

    // function to retrieve the JSON from the source
    private fun fetchListData() {

        // build a request
        val request = Request.Builder()
            .url(BASE_URL)
            .build()

        // enqueue the request to be done asynchronously with callback
        client.newCall(request).enqueue(object : Callback {

            // if the request fails, log an alert
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ListActivity", "Error fetching JSON data", e)
            }

            // if we get a response, parse the JSON from the body into an array of Item objects
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val rawData = Gson().fromJson(response.body?.string(), Array<ListItem>::class.java).toList()
                    val preparedData = groupAndSort(rawData)

                    // Update the UI on main thread
                    runOnUiThread {
                        setupRecyclerView(preparedData)
                    }
                }
            }
        })
    }

    // Get a reference to the RecyclerView, initialize adapter, & set layout manager
    private fun setupRecyclerView(data: List<ListItemDisplay>) {
        val recyclerView: RecyclerView = findViewById(R.id.recycView)
        recyclerView.adapter = ListAdapter(data) // You will also need to update your adapter to accept List<ListItemDisplay>
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}