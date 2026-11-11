package me.kavishdevar.librepods.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RootlessSupportTest {
    @Test
    fun onePlusAndroid15RequiresXposedPath() {
        assertFalse(
            isRootlessSupported(
                sdkInt = 35,
                buildId = "UKQ1.230924.001",
                manufacturer = "OnePlus",
                bypassDeviceCheck = false
            )
        )
    }

    @Test
    fun oppoFamilyAndroid16IsRootlessEligible() {
        assertTrue(isRootlessSupported(36, "V1", "OnePlus", false))
        assertTrue(isRootlessSupported(36, "V1", "OPPO", false))
        assertTrue(isRootlessSupported(36, "V1", "realme", false))
    }

    @Test
    fun pixelAndroid16RequiresExpectedBuildFamily() {
        assertTrue(isRootlessSupported(36, "CP1A.240", "Google", false))
        assertFalse(isRootlessSupported(36, "BP1A.240", "Google", false))
    }

    @Test
    fun android17AndExplicitBypassRemainSupported() {
        assertTrue(isRootlessSupported(37, "ANY", "Unknown", false))
        assertTrue(isRootlessSupported(35, "ANY", "Unknown", true))
    }

    @Test
    fun unknownManufacturersAreNotAssumedRootlessCompatible() {
        assertFalse(isRootlessSupported(35, "ANY", "Unknown", false))
        assertFalse(isRootlessSupported(36, "ANY", "Unknown", false))
    }
}
