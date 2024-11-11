package com.yusufsakir.shoppinglistapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "shoppingList.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_LIST = "shopping_list"
        private const val COLUMN_LIST_ID = "list_id"
        private const val COLUMN_LIST_TITLE = "title"

        private const val TABLE_ITEMS = "shopping_items"
        private const val COLUMN_ITEM_ID = "item_id"
        private const val COLUMN_ITEM_NAME = "name"
        private const val COLUMN_ITEM_QUANTITY = "quantity"
        private const val COLUMN_ITEM_LIST_ID = "list_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createListTable = """
            CREATE TABLE $TABLE_LIST (
                $COLUMN_LIST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_LIST_TITLE TEXT
            )
        """
        db.execSQL(createListTable)

        val createItemsTable = """
            CREATE TABLE $TABLE_ITEMS (
                $COLUMN_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ITEM_NAME TEXT,
                $COLUMN_ITEM_QUANTITY INTEGER,
                $COLUMN_ITEM_LIST_ID INTEGER,
                FOREIGN KEY($COLUMN_ITEM_LIST_ID) REFERENCES $TABLE_LIST($COLUMN_LIST_ID)
            )
        """
        db.execSQL(createItemsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ITEMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LIST")
        onCreate(db)
    }


    fun addShoppingList(title: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LIST_TITLE, title)
        }
        return db.insert(TABLE_LIST, null, values)
    }


    fun getItemsForList(listId: Int): List<ShoppingItem> {
        val items = mutableListOf<ShoppingItem>()
        val db = readableDatabase
        val selection = "$COLUMN_ITEM_LIST_ID = ?"
        val selectionArgs = arrayOf(listId.toString())
        val cursor = db.query(TABLE_ITEMS, null, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val itemId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_QUANTITY))
                items.add(ShoppingItem(itemId, name, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return items
    }


    fun getAllShoppingLists(): List<ShoppingList> {
        val shoppingLists = mutableListOf<ShoppingList>()
        val db = readableDatabase
        val cursor = db.query(TABLE_LIST, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val listId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LIST_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LIST_TITLE))
                shoppingLists.add(ShoppingList(listId, title))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return shoppingLists
    }

    fun addShoppingItem(name: String, quantity: Int, listId: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ITEM_NAME, name)
            put(COLUMN_ITEM_QUANTITY, quantity)
            put(COLUMN_ITEM_LIST_ID, listId)
        }
        return db.insert(TABLE_ITEMS, null, values)
    }

    fun deleteShoppingList(listId: Int): Int {
        val db = writableDatabase
        db.delete(TABLE_ITEMS, "$COLUMN_ITEM_LIST_ID = ?", arrayOf(listId.toString()))
        return db.delete(TABLE_LIST, "$COLUMN_LIST_ID = ?", arrayOf(listId.toString()))
    }

    fun deleteShoppingItem(itemId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_ITEMS, "$COLUMN_ITEM_ID = ?", arrayOf(itemId.toString()))
    }


}
