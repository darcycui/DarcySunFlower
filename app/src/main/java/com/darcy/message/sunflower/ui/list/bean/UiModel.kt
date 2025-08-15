package com.darcy.message.sunflower.ui.list.bean

import com.darcy.message.lib_common.entity.IUIBean
import com.darcy.message.lib_db.tables.Repo

data class UiModel(
    var id: Int = -1,
    var name: String = "",
    var description: String = "",
    var startCount: Int = -1
) : IUIBean<Repo> {

    override fun generate(dataEntity: Repo): UiModel {
        this.id = dataEntity.id
        this.name = dataEntity.name
        this.description = dataEntity.description
        this.startCount = dataEntity.starCount
        return this
    }
}
