package com.yusufsakir.shoppinglistapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufsakir.shoppinglistapp.databinding.ActivityItemListBinding

class ItemListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemListBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var itemAdapter: ItemAdapter
    private val ADD_PRODUCT_REQUEST_CODE = 2

    private var listId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        listId = intent.getIntExtra("listId", -1)
        if (listId == -1) {
            finish()
            return
        }

        val items = dbHelper.getItemsForList(listId).toMutableList()

        itemAdapter = ItemAdapter(items)
        binding.itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ItemListActivity)
            adapter = itemAdapter
        }

        binding.buttonAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PRODUCT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val productName = data?.getStringExtra("PRODUCT_NAME") ?: return
            val newProduct = ShoppingItem(0, productName, 1) // Varsayılan olarak miktar 1

            dbHelper.addShoppingItem(newProduct.name, newProduct.quantity, listId)
            itemAdapter.addItem(newProduct)
        }
    }
}
