package com.darcy.message.lib_umeng.agent.umeng

import com.darcy.message.lib_umeng.agent.IUserAgent
import com.umeng.analytics.MobclickAgent

class UmengUserAgentImpl : IUserAgent{
    override fun signIn(userID: String) {
        MobclickAgent.onProfileSignIn(userID)
    }

    override fun signIn(provider: String, userID: String) {
        MobclickAgent.onProfileSignIn(provider, userID)
    }

    override fun signOut(userID: String) {
        MobclickAgent.onProfileSignOff()
    }

    override fun signOut(provider: String, userID: String) {
        MobclickAgent.onProfileSignOff()
    }
}