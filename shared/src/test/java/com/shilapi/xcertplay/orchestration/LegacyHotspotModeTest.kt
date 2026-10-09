package com.shilapi.xcertplay.orchestration

import org.junit.Assert.assertEquals
import org.junit.Test

class LegacyHotspotModeTest {
    @Test fun android6And7NeverSelectUnsupportedHotspotApis() {
        for (sdk in 23..25) {
            for (mode in WirelessHotspotMode.entries) {
                assertEquals(WirelessHotspotMode.MANUAL, compatibleHotspotMode(mode, sdk))
            }
        }
    }

    @Test fun android8And9RetainManualAndLocalHotspot() {
        for (sdk in 26..28) {
            assertEquals(WirelessHotspotMode.MANUAL,
                compatibleHotspotMode(WirelessHotspotMode.MANUAL, sdk))
            assertEquals(WirelessHotspotMode.LOCAL_ONLY_HOTSPOT,
                compatibleHotspotMode(WirelessHotspotMode.WIFI_P2P, sdk))
            assertEquals(WirelessHotspotMode.LOCAL_ONLY_HOTSPOT,
                compatibleHotspotMode(WirelessHotspotMode.LOCAL_ONLY_HOTSPOT, sdk))
        }
    }

    @Test fun modernAndroidPreservesTheSelectedMode() {
        for (mode in WirelessHotspotMode.entries) {
            assertEquals(mode, compatibleHotspotMode(mode, 29))
        }
    }
}
