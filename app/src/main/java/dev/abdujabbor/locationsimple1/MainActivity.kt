package dev.abdujabbor.locationsimple1


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import dev.abdujabbor.locationsimple1.models.User
import dev.abdujabbor.locationsimple1.utils.MyConstants
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
    GoogleMap.OnPolygonClickListener,GoogleMap.OnMapClickListener {
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private const val LOCATION_UPDATE_INTERVAL = 1000L // 5 seconds
        private const val LOCATION_UPDATE_DISTANCE = 1f // 10   meters
    }

    val polylineList = mutableListOf<Polyline>() // Initialize the list

    private lateinit var map: GoogleMap
    var zoom =38F
    private lateinit var locationManager: LocationManager
    lateinit var lat: LatLng
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Use the location
            val latitude = location.latitude
            val longitude = location.longitude

            // Add a marker to the map

//            map.addMarker(MarkerOptions().position(LatLng(latitude,longitude)))!!
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f))
            val user = LatLng(latitude, longitude)
            addPolylineSegmentToList(map, polylineList, lat, user)
            lat = user


        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lat = LatLng(40.338,71.4564)
        if (ContextCompat.checkSelfPermission(
                this, ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }

        // Get the location manager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        // Get the map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.setOnMapClickListener(this)
           map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 0f))
        // Check if the user has granted permission to access the device's location
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Enable location layer on the map
            map.isMyLocationEnabled = true
            map.isBuildingsEnabled = true
            map.isTrafficEnabled = true
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMapToolbarEnabled= true
            map.uiSettings.isCompassEnabled= true
            map.uiSettings.isIndoorLevelPickerEnabled = true

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat.latitude, lat.longitude), zoom))
            // Register the location listener
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_UPDATE_INTERVAL,
                LOCATION_UPDATE_DISTANCE,
                locationListener
            )
        } else {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    onMapReady(map)
                } else {
                    // Permission denied
                    Toast.makeText(
                        this,
                        "Location permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onPolylineClick(p0: Polyline) {

    }

    override fun onPolygonClick(p0: Polygon) {

    }

    fun addPolylineSegmentToList(googleMap: GoogleMap, polylineList: MutableList<Polyline>, previousLocation: LatLng?, currentLocation: LatLng) {
        if (previousLocation != null) {
            val polylineOptions = PolylineOptions()
            polylineOptions.add(previousLocation, currentLocation)
            polylineOptions.color(Color.RED)
            polylineOptions.width(10f)
            val polyline = googleMap.addPolyline(polylineOptions)
            polylineList.add(polyline)
        }
    }

    override fun onMapClick(p0: LatLng) {
        MyConstants.lattitude = p0
        startActivity(Intent(this,WeatherActivity::class.java))
    }

}


/**
If conditionals zero if + present simple ,present simple  this type use to exactly facts for example If you multiple two to three ,it is equal to six and
if you mix fresh water and black colour , this water colour is black. If you
 **/