package com.example.gugelmaps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var mMap  : GoogleMap
    lateinit var mLocationRequest: LocationRequest
    var mLastLocation: Location? = null
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
//    internal var mFusedLocationClients: FusedLocationProviderClient? = null
    var mCurrentLocationMarker: Marker? = null





    internal var mLocationCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?){
            val locationList = locationResult?.locations
            if(locationList!!.isNotEmpty()){
                val location = locationList.last()
                Log.i("cekcek",location.toString())
                mLastLocation = location
                if(mCurrentLocationMarker != null){
                    mCurrentLocationMarker?.remove()
                }
                val latLng = LatLng(location.latitude,location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title("Posisi sekarang "+location.latitude+","+location.longitude.toString())
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                mCurrentLocationMarker = mMap.addMarker(markerOptions)
               mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0F))
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Location Now
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap : GoogleMap){
//        val sydney = LatLng(-33.852, 151.211)
//        val bandung = LatLng(-6.914744, 107.609810)
//        val bojongsoang = LatLng(-6.980857076885206, 107.63616396313509)
//        val baleendah = LatLng(-7.006386835208272, 107.63101554344338)
//        val margaasih = LatLng(-6.957747596096629, 107.56012851868851)
//
//
//        googleMap.addMarker(
//            MarkerOptions().position(bandung).title("Ini in Bandung")
//        )
//        googleMap.addMarker(
//            MarkerOptions().position(bojongsoang).title("Ini in Bojongsoang")
//        )
//        googleMap.addMarker(
//            MarkerOptions().position(baleendah).title("Ini in Baleendah")
//        )
//        googleMap.addMarker(
//            MarkerOptions().position(margaasih).title("Ini in Margaasih")
//        )
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bandung,10f))
        mMap = googleMap
        mMap.mapType= GoogleMap.MAP_TYPE_HYBRID
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 20000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return Toast.makeText(this,"Izinkan Akses Location",Toast.LENGTH_LONG).show()
        }
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
        googleMap.isMyLocationEnabled = true
    }
}

