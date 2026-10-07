/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.ui.intro

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.bitfire.davdroid.R
import at.bitfire.davdroid.ui.AppTheme
import at.bitfire.davdroid.ui.M3ColorScheme

class WelcomePage: IntroPage() {

    override val customTopInsets: Boolean = true

    override fun getShowPolicy() = ShowPolicy.SHOW_ONLY_WITH_OTHERS

    @Composable
    override fun ComposePage() {
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
            ContentLandscape()
        else
            ContentPortrait()
    }


    @Composable
    private fun ContentPortrait() {
        // Full-screen dark navy gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E3A54),   // slightly lighter navy at top
                            M3ColorScheme.tertiaryLight,  // brand navy #2F5577
                            Color(0xFF1A2F45)    // deeper navy at bottom
                        )
                    )
                )
                .safeContentPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.8f))

                // The app wordmark supplied by the active resource set.
                Image(
                    painter = painterResource(R.drawable.brand_wordmark),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = stringResource(R.string.app_name),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Primary slogan — white, clean, large
                Text(
                    text = stringResource(R.string.intro_slogan1),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Light,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Secondary slogan — brand green, bold
                Text(
                    text = stringResource(R.string.intro_slogan2),
                    color = M3ColorScheme.secondaryLight,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1.2f))
            }
        }
    }

    @Composable
    @Preview(
        device = "id:3.7in WVGA (Nexus One)",
        showSystemUi = true
    )
    fun Preview_ContentPortrait_Light() {
        AppTheme(darkTheme = false) {
            ContentPortrait()
        }
    }

    @Composable
    @Preview(
        device = "id:3.7in WVGA (Nexus One)",
        showSystemUi = true
    )
    fun Preview_ContentPortrait_Dark() {
        AppTheme(darkTheme = true) {
            ContentPortrait()
        }
    }


    @Preview(
        showSystemUi = true,
        device = "id:medium_tablet"
    )
    @Composable
    private fun ContentLandscape() {
        AppTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF1E3A54),
                                M3ColorScheme.tertiaryLight,
                                Color(0xFF1A2F45)
                            )
                        )
                    )
                    .safeContentPadding(),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo on the left
                    Image(
                        painter = painterResource(R.drawable.brand_wordmark),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .weight(1f)
                    )

                    // Slogans on the right
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.intro_slogan1),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Light,
                                letterSpacing = 0.5.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = stringResource(R.string.intro_slogan2),
                            color = M3ColorScheme.secondaryLight,
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

}
