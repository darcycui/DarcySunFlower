package com.darcy.message.sunflower.ui.detail.bean

import javax.inject.Inject

open class Parent @Inject constructor() {
    open var name: String = "nameParent"
    override fun toString(): String {
        return "{name=$name}"
    }
}