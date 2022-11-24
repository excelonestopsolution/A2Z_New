package com.a2z.app.service.location

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import com.a2z.app.util.extension.showToast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import javax.inject.Inject


class LocationService @Inject constructor(
    private val context: Context,
) {
    init { enable() }

    fun enable(): Boolean {
        val lm: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (_: Exception) {
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (_: Exception) {
        }
        val isEnable = gpsEnabled && networkEnabled
        locationState.value = isEnable
        return isEnable

    }

    fun enableSetting(activity: Activity, onResolutionRequired: (ResolvableApiException)-> Unit = {}) {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000 / 2

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(activity)

        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener(activity) {
            activity.showToast("service on")
        }


        task.addOnFailureListener(activity) {
            when (it) {
                is ResolvableApiException -> {
                    val resolvable = it as ResolvableApiException
                    onResolutionRequired.invoke(resolvable)
                }
                else -> {
                    val intent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    activity.startActivity(intent)
                }
            }
        }

    }

    companion object {
        val locationState = mutableStateOf(false)
    }


}