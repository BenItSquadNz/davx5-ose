/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.ui.setup

import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import at.bitfire.davdroid.ui.setup.LoginTypesProvider.LoginAction
import javax.inject.Inject

class StandardLoginTypesProvider @Inject constructor() : LoginTypesProvider {

    companion object {
        val genericLoginTypes = listOf(
            UrlLogin,
            EmailLogin,
            AdvancedLogin
        )

        val specificLoginTypes = listOf(
            FastmailLogin,
            GoogleLogin,
            NextcloudLogin
        )
    }

    override val defaultLoginType = UrlLogin

    override fun intentToInitialLoginType(intent: Intent): LoginAction =
        intent.data?.normalizeScheme().let { uri ->
            when {
                intent.hasExtra(LoginActivity.EXTRA_LOGIN_FLOW) ->
                    LoginAction(NextcloudLogin, true)
                uri?.scheme == "mailto" ->
                    LoginAction(EmailLogin, true)
                listOf("caldavs", "carddavs", "davx5", "http", "https").any { uri?.scheme == it } ->
                    LoginAction(UrlLogin, true)
                else ->
                    LoginAction(defaultLoginType, true)
            }
        }

    @Composable
    override fun LoginTypePage(
        snackbarHostState: SnackbarHostState,
        selectedLoginType: LoginType,
        onSelectLoginType: (LoginType) -> Unit,
        setInitialLoginInfo: (LoginInfo) -> Unit,
        onContinue: () -> Unit
    ) {
        StandardLoginTypePage(
            selectedLoginType = selectedLoginType,
            onSelectLoginType = onSelectLoginType,
            setInitialLoginInfo = setInitialLoginInfo,
            onContinue = onContinue
        )
    }

}
