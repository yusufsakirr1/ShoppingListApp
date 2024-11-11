package com.yusufsakir.shoppinglistapp
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufsakir.shoppinglistapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private val ADD_LIST_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        val shoppingLists = dbHelper.getAllShoppingLists().toMutableList()

        shoppingListAdapter = ShoppingListAdapter(shoppingLists) { shoppingList ->
            val intent = Intent(this, ItemListActivity::class.java)
            intent.putExtra("listId", shoppingList.listId)
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = shoppingListAdapter
        }

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddListActivity::class.java)
            startActivityForResult(intent, ADD_LIST_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_LIST_REQUEST_CODE && resultCode == RESULT_OK) {
            val listName = data?.getStringExtra("LIST_NAME") ?: return
            val newListId = dbHelper.addShoppingList(listName)
            val newList = ShoppingList(newListId.toInt(), listName)
            shoppingListAdapter.addShoppingList(newList)
        }
    }
}
