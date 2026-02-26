/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.ui.setup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import at.bitfire.davdroid.R
import at.bitfire.davdroid.ui.ExternalUris
import at.bitfire.davdroid.ui.ExternalUris.withStatParams
import at.bitfire.davdroid.ui.UiUtils.toAnnotatedString
import at.bitfire.davdroid.ui.composable.Assistant

@Composable
fun StandardLoginTypePage(
    selectedLoginType: LoginType,
    onSelectLoginType: (LoginType) -> Unit,

    @Suppress("unused")   // for build variants
    setInitialLoginInfo: (LoginInfo) -> Unit,
    
    onContinue: () -> Unit = {}
) {
    var showMoreOptions by remember { mutableStateOf(false) }

    Assistant(
        nextLabel = stringResource(R.string.login_continue),
        nextEnabled = true,
        onNext = onContinue
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(
                stringResource(R.string.login_generic_login),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Show URL login prominently — it's the primary login method
            LoginTypeSelector(
                title = stringResource(UrlLogin.title),
                selected = UrlLogin == selectedLoginType,
                onSelect = { onSelectLoginType(UrlLogin) }
            )

            // Collapsible section for other login types
            TextButton(
                onClick = { showMoreOptions = !showMoreOptions },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = if (showMoreOptions) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = if (showMoreOptions) "Less options" else "More options",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            AnimatedVisibility(visible = showMoreOptions) {
                Column {
                    // Other generic login types (Email, Advanced)
                    for (type in StandardLoginTypesProvider.genericLoginTypes.drop(1))
                        LoginTypeSelector(
                            title = stringResource(type.title),
                            selected = type == selectedLoginType,
                            onSelect = { onSelectLoginType(type) }
                        )

                    Text(
                        stringResource(R.string.login_provider_login),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    for (type in StandardLoginTypesProvider.specificLoginTypes)
                        LoginTypeSelector(
                            title = stringResource(type.title),
                            selected = type == selectedLoginType,
                            onSelect = { onSelectLoginType(type) }
                        )
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            val privacyPolicy = ExternalUris.Homepage.baseUrl.buildUpon()
                .appendPath(ExternalUris.Homepage.PATH_PRIVACY)
                .withStatParams("StandardLoginTypePage")
                .build().toString()
            val privacy = HtmlCompat.fromHtml(
                stringResource(R.string.login_privacy_hint, stringResource(R.string.app_name), privacyPolicy),
                HtmlCompat.FROM_HTML_MODE_COMPACT)
            Text(
                text = privacy.toAnnotatedString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun LoginTypeSelector(
    title: String,
    selected: Boolean,
    onSelect: () -> Unit = {}
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onSelect)
                .padding(bottom = 4.dp)
        ) {
            RadioButton(
                selected = selected,
                onClick = onSelect
            )
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
@Preview
fun StandardLoginTypePage_Preview() {
    StandardLoginTypePage(
        selectedLoginType = StandardLoginTypesProvider.genericLoginTypes.first(),
        onSelectLoginType = {},
        setInitialLoginInfo = {},
        onContinue = {}
    )
}