package com.example.full_screen_notification.utils

import android.os.Bundle
import java.io.Serializable

object BundleHelper {
    fun Map<String, Any>.toBundle(): Bundle {
        val bundle = Bundle()
        forEach { (key, value) ->
            when (value) {
                is Int -> bundle.putInt(key, value)
                is String -> bundle.putString(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Float -> bundle.putFloat(key, value)
                is Serializable -> bundle.putSerializable(key, value)
                // Add other types as necessary
                else -> {
                    // Optionally handle unsupported types or log a warning.
                }
            }
        }
        return bundle
    }
}