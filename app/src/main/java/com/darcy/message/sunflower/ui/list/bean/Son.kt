package com.darcy.message.sunflower.ui.list.bean

import com.darcy.message.lib_db.tables.Repo
import com.github.megatronking.stringfog.annotation.StringFogIgnore
import javax.inject.Inject

@StringFogIgnore
class Son @Inject constructor() : Parent() {
    var id: Int = -1
    override var name: String = "Son"

    fun generate(dataEntity: Repo): Son {
        this.id = dataEntity.id
        this.name = dataEntity.name
        return this
    }

    override fun toString(): String {
        return "{id=$id name=$name}"
    }
}
