package com.darcy.message.lib_db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darcy.message.lib_common.entity.IDataEntity

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    var itemName: String,
    @ColumnInfo(name = "price")
    var itemPrice: Double,
    @ColumnInfo(name = "quantity")
    var quantityInStock: Int
) : IDataEntity