package com.fetch.matthewomalley

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// declare the two different views
private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ITEM = 1

// adapter class for recyclerview that handles displaying headers and item views
class ListAdapter(private val items: List<ListItemDisplay>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // header view holder
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerTitle: TextView = view.findViewById(R.id.headerTitle)
    }

    // item view holder
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainId: TextView = view.findViewById(R.id.mainId)
        val listId: TextView = view.findViewById(R.id.listId)
        val listItemName: TextView = view.findViewById(R.id.listItemName)
    }

    // determine type of view needed for each item at a position (header if listId is null, defined in ListActivity)
    override fun getItemViewType(position: Int): Int {
        return if (items[position].listId != null) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // inflate the appropriate layout based on the view type
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemDisplay = items[position]
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> {
                (holder as HeaderViewHolder).headerTitle.text = "List ID: ${itemDisplay.listId}"
            }
            VIEW_TYPE_ITEM -> {
                holder as ItemViewHolder
                holder.mainId.text = itemDisplay.item?.id.toString()
                holder.listItemName.text = itemDisplay.item?.name
            }
        }
    }

    override fun getItemCount() = items.size
}
