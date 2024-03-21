package com.darcy.message.sunflower.ui.detail.di

import com.darcy.message.sunflower.ui.detail.bean.IWork
import com.darcy.message.sunflower.ui.detail.bean.WorkA
import com.darcy.message.sunflower.ui.detail.bean.WorkB
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

/**
 * Module for [IWork]
 * InstallIn [SingletonComponent]
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class WorkModule {

    /**
     * Binds the implementation of [IWork] to [WorkA].
     * use [A] [B] Qualifier bind more than one impl at same time
     */
    @Binds
    @A
    abstract fun bindWorkA(impl: WorkA): IWork
    @Binds
    @B
    abstract fun bindWorkB(impl: WorkB): IWork

    /**
     * Qualifier and Retention for separate work for A and B.
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class A

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class B
}
