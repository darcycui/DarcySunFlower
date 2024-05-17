package com.darcy.message.sunflower.ui.list.bean

import com.darcy.message.lib_common.entity.IUIBean
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.lib_db.tables.Repo

data class ListBean(
    var id: Int = -1,
    var name: String = "",
    var time: Double = 0.0,
    var quantity: Int = -1
) : IUIBean<Item> {

    override fun generate(dataEntity: Item): ListBean {
        this.id = dataEntity.id
        this.name = dataEntity.itemName
        this.time = dataEntity.itemPrice
        this.quantity = dataEntity.quantityInStock
        return this
    }

    fun generate(dataEntity: Repo): ListBean {
        this.id = dataEntity.id.toInt()
        this.name = dataEntity.name
        this.time = dataEntity.stargazers_count.toDouble()
        this.quantity = dataEntity.forks_count
        return this
    }
}
