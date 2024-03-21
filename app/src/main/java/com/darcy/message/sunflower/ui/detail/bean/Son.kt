package com.darcy.message.sunflower.ui.detail.bean

import com.darcy.message.lib_db.tables.Item
import javax.inject.Inject

class Son @Inject constructor() : Parent() {
    var id: Int = -1
    override var name: String = "Son"

    fun generate(dataEntity: Item): Son {
        this.id = dataEntity.id
        this.name = dataEntity.itemName
        return this
    }

    override fun toString(): String {
        return "{id=$id name=$name}"
    }
}
