/*
    LibrePods - AirPods liberated from Apple’s ecosystem
    Copyright (C) 2025 LibrePods contributors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package me.kavishdevar.librepods.utils

import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.edit

fun isSupported(sharedPreferences: SharedPreferences): Boolean =
    isRootlessSupported(
        sdkInt = Build.VERSION.SDK_INT,
        buildId = Build.ID,
        manufacturer = Build.MANUFACTURER,
        bypassDeviceCheck = sharedPreferences.getBoolean("bypass_device_check.v2", false)
    )

/**
 * Pure device-policy check so the rootless allowlist can be unit tested.
 *
 * Android 15 (API 35) OnePlus devices deliberately remain outside the rootless
 * allowlist; they must use the documented Xposed/LSPosed Bluetooth workaround.
 */
internal fun isRootlessSupported(
    sdkInt: Int,
    buildId: String,
    manufacturer: String,
    bypassDeviceCheck: Boolean
): Boolean {
    if (sdkInt >= 37) return true
    if (bypassDeviceCheck) return true

    val normalizedManufacturer = manufacturer.lowercase()
    val isPixel = normalizedManufacturer == "google"
    val isOppoFamily = normalizedManufacturer in listOf("oneplus", "oppo", "realme")

    return when {
        isPixel && sdkInt == 36 -> buildId.startsWith("CP1A")
        isOppoFamily -> sdkInt >= 36
        else -> false
    }
}

fun bypassDeviceCheck(sharedPreferences: SharedPreferences) {
    sharedPreferences.edit{ putBoolean("bypass_device_check.v2", true) }
}

fun removeDeviceCheckBypass(sharedPreferences: SharedPreferences) {
    sharedPreferences.edit{ putBoolean("bypass_device_check.v2", false) }
}
