package com.shilapi.xcertplay.transport

/** Preserve stream order across the pre-Android-9 16 KiB limit and short USB writes. */
internal fun writeUsbBulkChunks(
    size: Int,
    maxChunkBytes: Int,
    transfer: (offset: Int, length: Int) -> Int,
) {
    require(size >= 0 && maxChunkBytes > 0)
    var offset = 0
    while (offset < size) {
        val length = minOf(maxChunkBytes, size - offset)
        val written = transfer(offset, length)
        if (written <= 0 || written > length) {
            throw IphoneUsbException.DeviceUnavailable(
                "USBMUX write transferred $written of $length bytes at offset $offset",
            )
        }
        offset += written
    }
}
