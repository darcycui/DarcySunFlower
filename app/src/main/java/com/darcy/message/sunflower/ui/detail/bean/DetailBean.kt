package com.darcy.message.sunflower.ui.detail.bean

import com.darcy.message.lib_common.entity.IUIBean
import com.darcy.message.lib_db.tables.Item

data class DetailBean(
    var id: Int = -1,
    var name: String = "",
    var price: Double = 0.0,
    var quantity: Int = -1
) : IUIBean<Item> {

    override fun generate(dataBean: Item): DetailBean {
        this.id = dataBean.id
        this.name = dataBean.itemName
        this.price = dataBean.itemPrice
        this.quantity = dataBean.quantityInStock
        return this
    }
}
