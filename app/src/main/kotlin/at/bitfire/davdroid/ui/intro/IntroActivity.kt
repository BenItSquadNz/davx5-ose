/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.ui.intro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import at.bitfire.davdroid.ui.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    companion object {
        /** Optional: index of the page to start at. Defaults to 0. */
        const val EXTRA_INITIAL_PAGE = "initialPage"
    }

    val model by viewModels<IntroModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pages = model.pages
        val initialPage = intent.getIntExtra(EXTRA_INITIAL_PAGE, 0).coerceIn(0, (pages.size - 1).coerceAtLeast(0))

        setContent {
            AppTheme {
                val scope = rememberCoroutineScope()
                val pagerState = rememberPagerState(initialPage = initialPage) { pages.size }

                BackHandler {
                    if (pagerState.settledPage == 0) {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    } else scope.launch {
                        pagerState.animateScrollToPage(pagerState.settledPage - 1)
                    }
                }

                IntroScreen(
                    pages = pages,
                    pagerState = pagerState,
                    onDonePressed = {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                )
            }
        }
    }


    /**
     * For launching the [IntroActivity]. Input is the initial page index to start at (0 = first page).
     * Result is `true` when the user cancelled the intro.
     */
    object Contract: ActivityResultContract<Int, Boolean>() {
        override fun createIntent(context: Context, input: Int): Intent =
            Intent(context, IntroActivity::class.java).apply {
                putExtra(EXTRA_INITIAL_PAGE, input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_CANCELED
        }
    }

}