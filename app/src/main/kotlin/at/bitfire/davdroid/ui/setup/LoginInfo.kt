/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.ui.setup

import at.bitfire.davdroid.settings.Credentials
import java.net.URI

data class LoginInfo(
    val baseUri: URI? = null,
    val credentials: Credentials? = null,

    val suggestedAccountName: String? = null
)
