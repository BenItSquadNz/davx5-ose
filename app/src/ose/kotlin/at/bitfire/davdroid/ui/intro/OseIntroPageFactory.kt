/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.ui.intro

import javax.inject.Inject

class OseIntroPageFactory @Inject constructor(
    batteryOptimizationsPage: BatteryOptimizationsPage,
    permissionsIntroPage: PermissionsIntroPage
): IntroPageFactory {

    override val introPages = arrayOf(
        WelcomePage(),
        permissionsIntroPage,
        batteryOptimizationsPage
    )

}