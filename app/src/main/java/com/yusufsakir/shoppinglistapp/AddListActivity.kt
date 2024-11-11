package com.yusufsakir.shoppinglistapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yusufsakir.shoppinglistapp.databinding.ActivityAddListBinding

class AddListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddProduct.setOnClickListener {
            val listName = binding.editTextListName.text.toString()
            if (listName.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("LIST_NAME", listName)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                binding.editTextListName.error = "Liste ismi boş olamaz"
            }
        }
    }
}
