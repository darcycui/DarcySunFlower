package com.darcy.message.sunflower.ui.detail.di

import com.darcy.message.sunflower.ui.detail.bean.IWork
import com.darcy.message.sunflower.ui.detail.bean.WorkA
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Module for [IWork]
 * InstallIn [SingletonComponent]
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class WorkModule {

    /**
     * Binds the implementation of [IWork] to [WorkA].
     */
    @Binds
    abstract fun bindWork(impl: WorkA): IWork
}