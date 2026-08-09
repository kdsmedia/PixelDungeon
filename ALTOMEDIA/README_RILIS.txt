================================================================================
PAKET RILIS PLAY CONSOLE — Darkest Pixel Dungeon
================================================================================
Aplikasi : Darkest Pixel Dungeon
Package  : com.altomedia.pixeldungeon
Versi    : 0.4.1 (versionCode 34)
Developer: ALTOMEDIA
Tanggal  : 8 Agustus 2026

Paket lengkap siap unggah ke Google Play Console.
================================================================================


--------------------------------------------------------------------------------
STRUKTUR FOLDER
--------------------------------------------------------------------------------

ALTOMEDIA/
├── README_RILIS.txt              ← file ini (ringkasan paket rilis)
├── KEYSTORE_INFO.txt             ← informasi keystore signing
│
├── build/                        ← artifact build (siap upload)
│   ├── darkestpixeldungeon-release.apk   (6.7 MB, untuk testing internal)
│   └── darkestpixeldungeon-release.aab   (6.5 MB, untuk upload Play Console)
│
├── graphics/                     ← aset grafis Store Listing
│   ├── icon_512.png                       (512x512, app icon)
│   ├── feature_graphic_1024x500.png       (1024x500, feature graphic)
│   ├── screenshot_01_phone.png            (1080x1920, hero select)
│   ├── screenshot_02_phone.png            (1080x1920, items & loot)
│   ├── screenshot_03_phone.png            (1080x1920, perks & buffs)
│   └── screenshot_04_phone.png            (1080x1920, combat & effects)
│
├── legal/                        ← dokumen hukum
│   ├── PRIVACY_POLICY.txt                 (kebijakan privasi detail)
│   └── TERMS_OF_SERVICE.txt               (syarat & ketentuan detail)
│
└── guides/                       ← panduan & materi listing
    ├── UPLOAD_RILIS_GUIDE.txt              (panduan upload langkah demi langkah)
    ├── STORE_LISTING_GUIDE.txt             (panduan store listing + deskripsi)
    └── BLOG_ARTICLE.txt                    (artikel blog, 8325 karakter)


--------------------------------------------------------------------------------
RINGKASAN TEKNIS BUILD
--------------------------------------------------------------------------------
Toolchain:
  - Android Gradle Plugin (AGP): 8.7.3
  - Gradle:                       8.9
  - Kotlin:                       1.9.24
  - JDK:                          21

SDK:
  - compileSdkVersion:  35
  - targetSdkVersion:   35  (memenuhi persyaratan target API level Play Console 2026)
  - minSdkVersion:      21  (Android 5.0 Lollipop, >98% perangkat aktif)

Identitas:
  - applicationId:   com.altomedia.pixeldungeon
  - versionCode:     34
  - versionName:     0.4.1

Monetisasi & Privasi:
  - Tidak ada iklan (no AdMob / no ads SDK)
  - Tidak ada in-app purchase (no IAP / no billing)
  - Tidak ada login / akun
  - Tidak ada SDK analytics pihak ketiga
  - Hanya 1 permission: android.permission.VIBRATE (haptic feedback)
  - Fully offline setelah install

Signing:
  - Keystore:  pixeldungeon.jks (PKCS12, ada di root repo)
  - Alias:     pixeldungeon
  - Password:  Kdsmedia@123
  - Lihat KEYSTORE_INFO.txt untuk detail lengkap


--------------------------------------------------------------------------------
VERIFIKASI ARTIFACT
--------------------------------------------------------------------------------
APK  (darkestpixeldungeon-release.apk):
  - package:           com.altomedia.pixeldungeon
  - versionCode:       34
  - versionName:       0.4.1
  - minSdkVersion:     21
  - targetSdkVersion:  35
  - compileSdkVersion: 35
  - Signing:           verified (apksigner) — CN=pixeldungeon
  - Size:              6.7 MB

AAB  (darkestpixeldungeon-release.aab):
  - Signing:           verified (jarsigner) — CN=pixeldungeon
  - Size:              6.5 MB
  - Format:            Android App Bundle (wajib untuk app baru Play Console)


--------------------------------------------------------------------------------
CHECKLIST CEPAT RILIS
--------------------------------------------------------------------------------
[ x ] Build sesuai persyaratan Play Console (targetSdk 35)
[ x ] AAB ditandatangani
[ x ] APK ditandatangani (untuk testing)
[ x ] App icon 512x512
[ x ] Feature graphic 1024x500
[ x ] 4 screenshot phone 1080x1920
[ x ] Privacy Policy
[ x ] Terms of Service
[ x ] Panduan upload
[ x ] Panduan store listing
[ x ] Artikel blog
[   ] Upload AAB ke Play Console  (ikuti UPLOAD_RILIS_GUIDE.txt)
[   ] Isi Data safety form
[   ] Isi Content rating (IARC)
[   ] Rollout ke Production

================================================================================
AKHIR README RILIS
================================================================================
