/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.ui.setup

import android.accounts.Account
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.bitfire.davdroid.di.DefaultDispatcher
import at.bitfire.davdroid.repository.AccountRepository
import at.bitfire.davdroid.servicedetection.DavResourceFinder
import at.bitfire.vcard4android.GroupMethod
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = LoginScreenModel.Factory::class)
class LoginScreenModel @AssistedInject constructor(
    @Assisted val initialLoginType: LoginType,
    @Assisted val skipLoginTypePage: Boolean,
    @Assisted val initialLoginInfo: LoginInfo,
    private val accountRepository: AccountRepository,
    @ApplicationContext val context: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    val loginTypesProvider: LoginTypesProvider,
    private val resourceFinderFactory: DavResourceFinder.Factory
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            initialLoginType: LoginType,
            skipLoginTypePage: Boolean,
            initialLoginInfo: LoginInfo
        ): LoginScreenModel
    }

    enum class Page {
        LoginType,
        LoginDetails,
        DetectResources,
        AccountDetails
    }

    private val startPage = if (skipLoginTypePage)
        Page.LoginDetails
    else
        Page.LoginType

    var page by mutableStateOf(startPage)
        private set

    var finish by mutableStateOf(false)
        private set


    // navigation events

    fun navToNextPage() {
        when (page) {
            Page.LoginType -> {
                // continue to login details
                loginDetailsUiState = loginDetailsUiState.copy(
                    loginType = loginTypeUiState.loginType
                )
                page = Page.LoginDetails
            }

            Page.LoginDetails -> {
                // continue to resource detection
                loginInfo = loginDetailsUiState.loginInfo
                page = Page.DetectResources

                detectResources()
            }

            Page.DetectResources -> {
                // continue to account details
                val emails = foundConfig?.calDAV?.emails.orEmpty().toSet()
                val initialAccountName = emails.firstOrNull()
                    ?: loginInfo.suggestedAccountName
                    ?: loginInfo.credentials?.username
                    ?: loginInfo.baseUri?.host
                    ?: ""
                updateAccountNameAndEmails(initialAccountName, emails)
                page = Page.AccountDetails
            }

            Page.AccountDetails -> {
                // last page
            }
        }
    }

    fun navBack() {
        when (page) {
            Page.LoginType ->
                finish = true

            Page.LoginDetails ->
                if (loginTypesProvider.maybeNonInteractive)
                    finish = true
                else
                    page = Page.LoginType

            Page.DetectResources -> {
                cancelResourceDetection()
                page = Page.LoginDetails
            }

            Page.AccountDetails ->
                page = Page.LoginDetails
        }
    }


    // UI element state – first page: login type

    data class LoginTypeUiState(
        val loginType: LoginType
    )

    var loginTypeUiState by mutableStateOf(LoginTypeUiState(loginType = initialLoginType))
        private set

    fun selectLoginType(loginType: LoginType) {
        loginTypeUiState = loginTypeUiState.copy(loginType = loginType)
        loginDetailsUiState = loginDetailsUiState.copy(loginType = loginType)
    }


    // UI element state – second page: login details

    // base URI and credentials
    private var loginInfo: LoginInfo = initialLoginInfo

    data class LoginDetailsUiState(
        val loginType: LoginType,
        val loginInfo: LoginInfo
    )

    var loginDetailsUiState by mutableStateOf(LoginDetailsUiState(
        loginType = initialLoginType,
        loginInfo = loginInfo
    ))
        private set

    fun updateLoginInfo(loginInfo: LoginInfo) {
        loginDetailsUiState = loginDetailsUiState.copy(loginInfo = loginInfo)
    }


    // UI element state – third page: detect resources

    data class DetectResourcesUiState(
        val loading: Boolean = false,
        val foundNothing: Boolean = false,
        val encountered401: Boolean = false,
        val logs: String? = null
    )

    var detectResourcesUiState by mutableStateOf(DetectResourcesUiState())
        private set

    private var foundConfig: DavResourceFinder.Configuration? = null
    private var detectResourcesJob: Job? = null

    private fun detectResources() {
        detectResourcesUiState = detectResourcesUiState.copy(loading = true)
        detectResourcesJob = viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                 runInterruptible {
                     val finder = resourceFinderFactory.create(loginInfo.baseUri!!, loginInfo.credentials)
                     finder.findInitialConfiguration()
                 }
            }

            if (result.calDAV != null || result.cardDAV != null) {
                foundConfig = result
                navToNextPage()

            } else {
                foundConfig = null
                detectResourcesUiState = detectResourcesUiState.copy(
                    loading = false,
                    foundNothing = true,
                    encountered401 = result.encountered401,
                    logs = result.logs
                )
            }
        }
    }

    private fun cancelResourceDetection() {
        detectResourcesJob?.cancel()
    }


    // UI element state – last page: account details

    data class AccountDetailsUiState(
        val accountName: String = "",
        val suggestedAccountNames: Set<String> = emptySet(),
        val accountNameExists: Boolean = false,
        val creatingAccount: Boolean = false,
        val createdAccount: Account? = null,
        val couldNotCreateAccount: Boolean = false
    ) {
        val showApostropheWarning = accountName.contains('\'') || accountName.contains('"')
    }

    private val _accountDetailsUiState = MutableStateFlow(AccountDetailsUiState())
    val accountDetailsUiState = _accountDetailsUiState.asStateFlow()

    fun updateAccountName(accountName: String) {
        _accountDetailsUiState.update { currentState ->
            currentState.copy(
                accountName = accountName,
                accountNameExists = accountRepository.exists(accountName)
            )
        }
    }

    fun updateAccountNameAndEmails(accountName: String, emails: Set<String>) {
        _accountDetailsUiState.update { currentState ->
            currentState.copy(
                accountName = accountName,
                accountNameExists = accountRepository.exists(accountName),
                suggestedAccountNames = emails
            )
        }
    }

    fun resetCouldNotCreateAccount() {
        _accountDetailsUiState.update { currentState ->
            currentState.copy(couldNotCreateAccount = false)
        }
    }

    fun createAccount() {
        _accountDetailsUiState.update { currentState ->
            currentState.copy(creatingAccount = true)
        }

        viewModelScope.launch {
            val account = withContext(defaultDispatcher) {
                accountRepository.createBlocking(
                    accountDetailsUiState.value.accountName,
                    loginInfo.credentials,
                    foundConfig!!,
                    GroupMethod.CATEGORIES
                )
            }

            _accountDetailsUiState.update { currentState ->
                if (account != null)
                    currentState.copy(createdAccount = account)
                else
                    currentState.copy(
                        creatingAccount = false,
                        couldNotCreateAccount = true
                    )
            }
        }
    }

}
