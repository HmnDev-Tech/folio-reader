package com.hmndev.folioreader.core

object RustReaderCore {
    private var isNativeLoaded = false

    init {
        try {
            System.loadLibrary("rust_reader_core")
            isNativeLoaded = true
        } catch (_: Throwable) {
            isNativeLoaded = false
        }
    }

    fun isAvailable(): Boolean = isNativeLoaded

    // Native JNI bindings implemented in rust-core/src/lib.rs
    @JvmStatic
    external fun nativeAnalyzeDocument(markdown: String): String

    @JvmStatic
    external fun nativePreprocessMarkdown(markdown: String): String

    @JvmStatic
    external fun nativeExtractFormulas(markdown: String): String

    @JvmStatic
    external fun nativeVersion(): String
}
