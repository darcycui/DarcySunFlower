package com.darcy.message.lib_umeng.agent

interface IUserAgent {
    /**
     * [userID] is the user ID of self app
     */
    fun signIn(userID: String)

    /**
     * [provider] is the name of the third party
     * [userID] is the user ID of self app
     */
    fun signIn(provider: String, userID: String)

    /**
     * [userID] is the user ID of self app
     */
    fun signOut(userID: String)

    /**
     * [provider] is the name of the third party
     * [userID] is the user ID of self app
     */
    fun signOut(provider: String, userID: String)

}