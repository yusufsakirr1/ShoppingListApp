package com.yusufsakir.shoppinglistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShoppingListAdapter(
    private val shoppingLists: MutableList<ShoppingList>,
    private val onItemClick: (ShoppingList) -> Unit
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_row, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val shoppingList = shoppingLists[position]
        holder.titleTextView.text = shoppingList.title

        holder.itemView.setOnClickListener {
            onItemClick(shoppingList)
        }
    }

    override fun getItemCount(): Int = shoppingLists.size

    fun addShoppingList(newList: ShoppingList) {
        shoppingLists.add(newList)
        notifyItemInserted(shoppingLists.size - 1)
    }
}
