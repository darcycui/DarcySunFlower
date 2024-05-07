package com.darcy.message.lib_ui.mvi.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_im.R
import com.darcy.message.lib_im.databinding.LibUiActivityTestBinding
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.mvi.ui.fragment.TestFragmentA
import com.darcy.message.lib_ui.mvi.ui.fragment.TestFragmentB

class TestFragmentActivity : BaseActivity<LibUiActivityTestBinding>() {

    private val FRAGMENT_A = "A"
    private val FRAGMENT_B = "B"
    private var fragmentA: Fragment? = null
    private var fragmentB: Fragment? = null
    private var currentFragment: Fragment? = null
    private var currentTag: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        if (savedInstanceState == null) {
            if (fragmentA == null) {
                fragmentA = TestFragmentA.newInstance()
            }
            if (fragmentB == null) {
                fragmentB = TestFragmentB.newInstance()
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.container, fragmentA!!, FRAGMENT_A)
                .add(R.id.container, fragmentB!!, FRAGMENT_B)
                .hide(fragmentA!!)
                .hide(fragmentB!!)
                .commitNow()
            showFragment(FRAGMENT_A)
        } else {
            if (fragmentA == null) {
                fragmentA = supportFragmentManager.findFragmentByTag(FRAGMENT_A)
            }
            if (fragmentB == null) {
                fragmentB = supportFragmentManager.findFragmentByTag(FRAGMENT_B)
            }
            currentTag = savedInstanceState.getString("currentTag", "")
            currentFragment = if (currentTag == FRAGMENT_A) {
                fragmentA
            } else {
                fragmentB
            }
            showFragment(currentTag)
        }
    }

    private fun initView() {
        binding.tvA.setOnClickListener {
            showFragment(FRAGMENT_A)
        }

        binding.tvB.setOnClickListener {
            showFragment(FRAGMENT_B)
        }
    }

    private fun showFragment(tag: String) {
        val fragment = if (supportFragmentManager.findFragmentByTag(tag) != null) {
            supportFragmentManager.findFragmentByTag(tag)!!
        } else {
            TestFragmentA.newInstance().also {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, it, tag)
                    .commitNow()
            }
        }
        supportFragmentManager.beginTransaction().also {
            if (currentFragment == null) {
                it.show(fragment)
            } else {
                it.hide(currentFragment!!)
                it.show(fragment)
            }
        }.commitNow()
        currentFragment = fragment
        currentTag = tag
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (currentFragment != null) {
            outState.putString("currentTag", currentTag)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentTag = savedInstanceState.getString("currentTag") ?: ""
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logV(message = "onConfigurationChanged")
    }
}