package com.shilapi.xcertplay.transport

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream

class LegacyUsbBulkTransferTest {
    @Test fun largePacketsSurviveLegacyTransferLimits() {
        val source = ByteArray(65_536) { it.toByte() }
        val received = ByteArrayOutputStream()
        val lengths = mutableListOf<Int>()
        writeUsbBulkChunks(source.size, 16_384) { offset, length ->
            lengths.add(length)
            received.write(source, offset, length)
            length
        }
        assertEquals(listOf(16_384, 16_384, 16_384, 16_384), lengths)
        assertArrayEquals(source, received.toByteArray())
    }

    @Test fun shortWritesResumeAtTheFirstUnwrittenByte() {
        val source = ByteArray(17_000) { (it * 7).toByte() }
        val received = ByteArrayOutputStream()
        writeUsbBulkChunks(source.size, 16_384) { offset, length ->
            val actual = minOf(length, 997)
            received.write(source, offset, actual)
            actual
        }
        assertArrayEquals(source, received.toByteArray())
    }

    @Test(expected = IphoneUsbException.DeviceUnavailable::class)
    fun failedWriteStopsInsteadOfLoopingForever() {
        writeUsbBulkChunks(20_000, 16_384) { _, _ -> -1 }
    }

    @Test(expected = IphoneUsbException.DeviceUnavailable::class)
    fun zeroProgressStopsInsteadOfLoopingForever() {
        writeUsbBulkChunks(20_000, 16_384) { _, _ -> 0 }
    }
}
