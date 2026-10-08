# Squad Sync 4.5.9-rc.7 Android release build

This source is the build input for the branded OSE APK distributed by IT Squad.
The application ID is `nz.itsquad.condex` and the Gradle version is
`4.5.9-rc.7-ose` (`405090009`). The branded resources are in
`app/src/ose/res/` and `app/src/ose/ic_launcher-web.png`.

Build with JDK 21 and Android SDK 36. The release build used
`ANDROID_HOMEPAGE_URL=https://itsquad.nz/` and the `:app:assembleOseRelease`
Gradle task. The Gradle signing variables are `ANDROID_KEYSTORE`,
`ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, and `ANDROID_KEY_PASSWORD`.
The official private signing key is not included in the source archive. A
different key will produce an installable APK with the same code and app ID,
but it cannot update an installed copy signed with the official key.

The corresponding unbranded source archive contains the same functional code
and application ID, omitting only the optional branding overlay and this
branded release note.
