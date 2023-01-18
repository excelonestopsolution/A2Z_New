package com.a2z.app.service.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import com.a2z.app.util.extension.showToast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import java.util.*
import javax.inject.Inject


class LocationService @Inject constructor(
    private val context: Context,
) {

    private val mLocationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private var mLocationListener: LocationListener? = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            mListener?.onLocationChange(location)
            location.let {
                mLocationManager.removeUpdates(this)
            }
        }


        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    companion object {
        val locationState = mutableStateOf(false)
        private const val LOCATION_UPDATE_MIN_DISTANCE = 10f
        private const val LOCATION_UPDATE_MIN_TIME = 5000L

    }

    init { isEnable() }

    fun isEnable(): Boolean {
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

    private var mListener: MLocationListener? = null

    interface MLocationListener {
        fun onLocationChange(location: Location)
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

    fun setupListener(listener: MLocationListener?) {
        mListener = listener
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {

        val isGPSEnabled: Boolean =
            mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled: Boolean =
            mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        var location: Location? = null


        if (isNetworkEnabled) {
            mLocationListener?.let {
                mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME,
                    LOCATION_UPDATE_MIN_DISTANCE, it
                )
            }
            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        if (isGPSEnabled) {
            mLocationListener?.let {
                mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME,
                    LOCATION_UPDATE_MIN_DISTANCE, it
                )
            }
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

    }



    fun getAddress(latitude : String,longitude : String): Array<String> {
        try{

            val addresses: List<Address>?
            val geocoder = Geocoder(context, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                latitude.toDouble(),
                longitude.toDouble(),
                1
            )

            if(addresses == null)  return arrayOf("","","")

            val address1: String = addresses[0].featureName ?: ""
            val address2: String = addresses[0].locality ?: ""
            val address3: String = addresses[0].subLocality ?: ""
            val address4: String = addresses[0].adminArea?: ""
            val address5: String = addresses[0].subAdminArea?: ""
            val city: String = addresses[0].locality?: ""
            val state: String = addresses[0].adminArea?: ""
            // val country: String = addresses[0].getCountryName()
            val postalCode: String = addresses[0].postalCode?: ""
            //  val knownName: String = addresses[0].getFeatureName()

            return arrayOf("$address1 $address3", city, "$state - $postalCode")
        }catch (e : Exception){
            return arrayOf("","","")
        }
    }



}