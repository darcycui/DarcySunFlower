package com.darcy.message.sunflower.ui.list.bean

import com.darcy.message.lib_common.entity.IUIBean
import com.darcy.message.lib_db.tables.Item

data class DetailBean(
    var id: Int = -1,
    var name: String = "",
    var price: Double = 0.0,
    var quantity: Int = -1
) : IUIBean<Item> {

    override fun generate(dataEntity: Item): DetailBean {
        this.id = dataEntity.id
        this.name = dataEntity.itemName
        this.price = dataEntity.itemPrice
        this.quantity = dataEntity.quantityInStock
        return this
    }
}
