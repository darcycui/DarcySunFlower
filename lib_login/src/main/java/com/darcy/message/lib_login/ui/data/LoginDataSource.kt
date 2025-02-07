package com.darcy.message.lib_login.ui.data

import com.darcy.message.lib_login.ui.data.model.LoggedInUser
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            if (username.isEmpty() || password.isEmpty()) {
                return Result.Error(IOException("Empty username or password"))
            }
            if (checkUsernameAndPassword(username, password).not()) {
                return Result.Error(IOException("Wrong username or password"))
            }
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    private fun checkUsernameAndPassword(username: String, password: String) =
        username == "aaaaaa" && password == "aaaaaa"

    fun logout() {
        // TODO: revoke authentication
    }
}