package com.hmndev.folioreader.core

import android.util.Log

object RustReaderCore {
    private const val TAG = "RustReaderCore"
    private var isNativeLoaded = false

    init {
        try {
            System.loadLibrary("rust_reader_core")
            isNativeLoaded = true
            Log.i(TAG, "Native Rust engine successfully loaded: ${nativeVersion()}")
        } catch (e: UnsatisfiedLinkError) {
            Log.w(TAG, "Native library 'rust_reader_core' not found on device, falling back to pure Kotlin engine.", e)
            isNativeLoaded = false
        } catch (e: Throwable) {
            Log.w(TAG, "Unexpected error loading native library, falling back to pure Kotlin engine.", e)
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
