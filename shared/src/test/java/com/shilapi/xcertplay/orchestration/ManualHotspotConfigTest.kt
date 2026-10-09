package com.shilapi.xcertplay.orchestration

import com.shilapi.xcertplay.transport.Iap2IdentificationConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ManualHotspotConfigTest {
    @Test
    fun wiredStartupDoesNotRequireManualHotspotCredentials() {
        assertEquals(CarPlayTransport.WIRED, manualConfig(CarPlayTransport.WIRED).transport)
    }

    @Test
    fun wirelessManualHotspotStillRequiresCredentials() {
        assertThrows(IllegalArgumentException::class.java) {
            manualConfig(CarPlayTransport.WIRELESS)
        }
    }

    private fun manualConfig(transport: CarPlayTransport) = CarPlayRuntimeConfig(
        mfiTarget = MfiTarget.REMOTE,
        remoteMfiServer = "https://mfi.example.test",
        transport = transport,
        wirelessHotspotMode = WirelessHotspotMode.MANUAL,
        identification = Iap2IdentificationConfig(
            name = "test", modelIdentifier = "test", manufacturer = "test", serialNumber = "test",
            firmwareVersion = "1", hardwareVersion = "1", carPlayUsbInterfaceNumber = 3,
        ),
    )

    @Test
    fun autoBandAcceptsBoth2GhzAnd5GhzChannels() {
        assertTrue(isManualHotspotChannelCompatible(ManualHotspotBand.AUTO, 6))
        assertTrue(isManualHotspotChannelCompatible(ManualHotspotBand.AUTO, 36))
    }

    @Test
    fun explicitBandRejectsChannelsFromTheOtherBand() {
        assertTrue(isManualHotspotChannelCompatible(ManualHotspotBand.GHZ_2_4, 11))
        assertFalse(isManualHotspotChannelCompatible(ManualHotspotBand.GHZ_2_4, 36))
        assertTrue(isManualHotspotChannelCompatible(ManualHotspotBand.GHZ_5, 149))
        assertFalse(isManualHotspotChannelCompatible(ManualHotspotBand.GHZ_5, 11))
    }
}
