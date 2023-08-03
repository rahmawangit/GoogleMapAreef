package com.example.gugelmaps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.Html
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.util.Locale


class MainActivity2 : AppCompatActivity(), OnMapReadyCallback {
    lateinit var mMap  : GoogleMap
    lateinit var mLocationRequest: LocationRequest
    var mLastLocation: Location? = null
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    //    internal var mFusedLocationClients: FusedLocationProviderClient? = null
    var mCurrentLocationMarker: Marker? = null

    var cevestLocationMarker : Marker? = null
    var garisJarak : Polyline? = null


    internal var mLocationCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult){
            val locationList = locationResult?.locations
            if(locationList!!.isNotEmpty()){
                val location = locationList.last()
                Log.i("cekcek",location.toString())
                mLastLocation = location
                if(mCurrentLocationMarker != null){
                    mCurrentLocationMarker?.remove()
                }

                if(cevestLocationMarker !=null){
                    cevestLocationMarker?.remove()
                }

                if(garisJarak != null){
                    garisJarak?.remove()
                }




                val latLng = LatLng(location.latitude,location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title("Posisi sekarang "+location.latitude+","+location.longitude.toString())
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                mCurrentLocationMarker = mMap.addMarker(markerOptions)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0F))
                // =============================
                val cevestLocation = LatLng(-6.2347677,106.987864)

                cevestLocationMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(cevestLocation)
                        .title("BBPVP BEKASI")
                        .icon(
                            BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                        )
                )

                val circleOptions = CircleOptions()
                circleOptions.center(cevestLocation)

                circleOptions.radius(1000.0)
                circleOptions.strokeColor(Color.RED)
                mMap.addCircle(circleOptions)
                // ===============

                val distance = FloatArray(2)
                Location.distanceBetween(
                    cevestLocation.latitude,cevestLocation.longitude,location.latitude,location.longitude,distance
                )
                var status =""

                if (distance[0] > circleOptions.radius){
                    status = "Diluar Area Kantor"
                }else{
                    status = "Dalam Area Kantor"
                }
                // =============
                garisJarak = mMap.addPolyline(
                    PolylineOptions().add(cevestLocation,
                        LatLng(location.latitude, location.longitude))
                        .width(5.0F)
                        .color(Color.YELLOW)
                )


                //==============
                val geocoder: Geocoder
                val addresses: List<Address>?
                val latitude = location.latitude
                val longitude = location.longitude
                geocoder = Geocoder(this@MainActivity2, Locale.getDefault())

                addresses = geocoder.getFromLocation(latitude,longitude,1)


                val address =
                    addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                val citya = addresses[0].
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                val postalCode = addresses[0].postalCode
                val knownName = addresses[0].featureName // Only if available else return NULL

                var tvShow = findViewById<TextView>(R.id.tvMaps)

                val pesan = "<b>Posisi anda Saat ini</b> : $address " +
                        "</br> <b>Status</b> : $status"
                tvShow.text = Html.fromHtml(pesan)

//                tvShow.text = "Status $status Alamat = ${address.toString()} , Kota = ${city.toString()}, IDK = ${state.toString()}, Negara = ${country.toString()}" +
//                        ", Kode Pos = ${postalCode.toString()} "
//                getCircle(latitude,longitude)
//                tvShow.text = getAddress(location.latitude,location.longitude)
//                tvShow.text = latLng.toString()
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        // Location Now
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    fun getAddress(lat : Double,lng:Double) : String{
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat,lng,1)
        return list?.get(0)!!.getAddressLine(0)
    }

    fun getCircle(lat : Double,lng:Double) : Circle {

        // ... get a map.
        // Add a circle in Sydney
        // ... get a map.
        // Add a circle in Sydney
        val circle = mMap.addCircle(
            CircleOptions()
                .center(LatLng(lat, lng))
                .radius(500.0)
                .strokeColor(Color.BLUE)
//                .fillColor(Color.BLUE)
        )
        return circle
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
            return Toast.makeText(this,"Izinkan Akses Location", Toast.LENGTH_LONG).show()
        }
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
        googleMap.isMyLocationEnabled = true
    }
}