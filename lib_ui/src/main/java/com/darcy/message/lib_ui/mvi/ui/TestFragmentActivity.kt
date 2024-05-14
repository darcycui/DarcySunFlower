package com.darcy.message.lib_ui.mvi.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_ui.R
import com.darcy.message.lib_ui.databinding.LibUiActivityTestBinding
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
            currentTag = savedInstanceState.getString("currentTag", "")
            logI(TAG, "$TAG onCreate currentTag=$currentTag")
            restoreFragment(currentTag)
        }
    }

    /**
     * persistent storage after reboot
     */
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        currentTag = savedInstanceState?.getString("currentTag", "") ?: ""
        if (currentTag.isNotEmpty()) {
            logI(message = "no need onCreate2: currentTag=$currentTag")
            return
        }
        currentTag = persistentState?.getString("currentTagPersistent", "") ?: ""
        logI(TAG, "$TAG onCreate2 currentTagPersistent=$currentTag")
        restoreFragment(currentTag)
    }

    private fun restoreFragment(tag: String) {
        if (fragmentA == null) {
            fragmentA = supportFragmentManager.findFragmentByTag(FRAGMENT_A)
        }
        if (fragmentB == null) {
            fragmentB = supportFragmentManager.findFragmentByTag(FRAGMENT_B)
        }
        showFragment(tag)
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
                logE(message = "currentFragment is null-->no need hide")
                it.show(fragment)
                logE(message = "fragment is $fragment-->show")
            } else {
                it.hide(currentFragment!!)
                logE(message = "currentFragment is $currentFragment-->need hide")
                it.show(fragment)
                logE(message = "fragment is $fragment-->show")
            }
        }.commitNow()
        currentFragment = fragment
        currentTag = tag
     }

    private fun initView() {
        binding.tvA.setOnClickListener {
            showFragment(FRAGMENT_A)
        }

        binding.tvB.setOnClickListener {
            showFragment(FRAGMENT_B)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (currentTag.isNotEmpty() && currentFragment != null) {
            outState.putString("currentTag", currentTag)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * persistent storage after reboot
     */
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        if (currentTag.isNotEmpty() && currentFragment != null) {
            outState.putString("currentTag", currentTag)
            outPersistentState.putString("currentTagPersistent", currentTag)
            logD(message = "save currentTag and currentTagPersistent+++")
            logD(message = "currentTag is $currentTag")
            logD(message = "currentTagPersistent is $currentTag")
        }
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentTag = savedInstanceState.getString("currentTag") ?: ""
    }

    /**
     * persistent storage after reboot
     */
    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        logD(message = "restore currentTag and currentTagPersistent---")
        currentTag = (savedInstanceState?.getString("currentTag") ?: "").also {
            logD(message = "currentTag is $it")
        }
        currentTag = (persistentState?.getString("currentTagPersistent") ?: "").also {
            logD(message = "currentTagPersistent is $it")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logV(message = "onConfigurationChanged")
    }
}