/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import at.bitfire.davdroid.settings.SettingsManager
import at.bitfire.davdroid.ui.account.AccountActivity
import at.bitfire.davdroid.ui.intro.IntroActivity
import at.bitfire.davdroid.ui.setup.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AccountsActivity: AppCompatActivity() {

    @Inject
    lateinit var accountsDrawerHandler: AccountsDrawerHandler

    @Inject
    lateinit var settingsManager: SettingsManager

    private val introActivityLauncher = registerForActivityResult(IntroActivity.Contract) { cancelled ->
        if (cancelled)
            finish()
        else
            settingsManager.putBoolean(AccountsModel.HINT_INTRO_COMPLETED, true)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // handle "Sync all" intent from launcher shortcut
        val syncAccounts = intent.action == Intent.ACTION_SYNC

        setContent {
            AccountsScreen(
                initialSyncAccounts = syncAccounts,
                onShowAppIntro = { initialPage ->
                    introActivityLauncher.launch(initialPage)
                },
                accountsDrawerHandler = accountsDrawerHandler,
                onAddAccount = {
                    val intent = Intent(this, LoginActivity::class.java)
                    // start directly in URL login (skip the type selector)
                    intent.data = Uri.parse("https://")
                    startActivity(intent)
                },
                onShowAccount = { account ->
                    val intent = Intent(this, AccountActivity::class.java)
                    intent.putExtra(AccountActivity.EXTRA_ACCOUNT, account)
                    startActivity(intent)
                },
                onManagePermissions = {
                    startActivity(Intent(this, PermissionsActivity::class.java))
                }
            )
        }
    }

}