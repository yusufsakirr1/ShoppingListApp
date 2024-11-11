package com.yusufsakir.shoppinglistapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yusufsakir.shoppinglistapp.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddProduct.setOnClickListener {
            val productName = binding.editTextProductName.text.toString()
            if (productName.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("PRODUCT_NAME", productName)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                binding.editTextProductName.error = "Ürün adı boş olamaz"
            }
        }
    }
}
