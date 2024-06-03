package com.darcy.message.lib_db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darcy.message.lib_common.entity.IDataEntity
import java.util.Comparator

@Entity
data class Item(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = "name")
    var itemName: String,
    @ColumnInfo(name = "price")
    var itemPrice: Double,
    @ColumnInfo(name = "quantity")
    var quantityInStock: Int = 100
) : IDataEntity {
    override fun equals(other: Any?): Boolean {
        return if (other is Item){
            other.id == this.id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}