package com.shoplex.shoplex.model.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.directions.route.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import java.util.*
import kotlin.collections.ArrayList

class LocationManager: RoutingListener {
    val alexandria: Alexandria = Alexandria()
    private lateinit var marker: Marker

    //private lateinit var currentLocation: Location
    lateinit var selectedLocation: LatLng
    private val REQUEST_CODE = 101

    private lateinit var mMap: GoogleMap
    private val API_KEY = "AIzaSyAyj2_BzoXGMR432LsT4dpv6TV6SdNbtDg"

    private constructor(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private constructor()

    companion object {
        private var shared: LocationManager? = null
        private lateinit var context: Context

        fun getInstance(googleMap: GoogleMap, context: Context): LocationManager {
            this.context = context
            return if (shared == null)
                LocationManager(googleMap)
            else
                shared!!
        }

        fun getInstance(context: Context): LocationManager {
            this.context = context
            return if (shared == null)
                LocationManager()
            else
                shared!!
        }
    }

    fun addMarker(current: Location?) {
        // Add a marker in Sydney and move the camera
        var currentLocation = if (current == null) LatLng(
            alexandria.capital.latitude,
            alexandria.capital.longitude
        ) else LatLng(current.latitude, current.longitude)

        if (!PolyUtil.containsLocation(currentLocation, alexandria.coordinates, false)) {
            currentLocation = LatLng(alexandria.capital.latitude, alexandria.capital.longitude)
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16F))
        marker = mMap.addMarker(
            MarkerOptions().position(currentLocation).title(getAddress(currentLocation, context))
        )
        selectedLocation = currentLocation

        val polygon = mMap.addPolygon(
            PolygonOptions()
                .addAll(alexandria.coordinates)
        )
        polygon.strokeColor = 0xff0000ff.toInt()
        polygon.isClickable = false
        polygon.strokeWidth = 4f


        mMap.setOnMapClickListener {
            if (PolyUtil.containsLocation(it, alexandria.coordinates, false)) {
                marker.remove()
                marker = mMap.addMarker(MarkerOptions().position(it).title("Your Location"))

                selectedLocation = it
            } else {
                Toast.makeText(context, "OutSide", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addMarkers(locations: ArrayList<LatLng>) {
        for (location in locations) {
            mMap.addMarker(MarkerOptions().position(location).title("Your Location"))
        }
    }

    fun launchGoogleMaps(location: LatLng) {
        val gmmIntentUri =
            Uri.parse("google.navigation:q=" + location.latitude + "," + location.longitude)
        //Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destination);
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    fun findRoutes(start: LatLng, end: LatLng) {
        val routing = Routing.Builder()
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(this)
            .alternativeRoutes(true)
            .waypoints(start, end)
            .key(API_KEY)
            .build()
        routing.execute()
    }

    fun getAddress(location: LatLng, context: Context): String? {

        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return addresses?.get(0)?.getAddressLine(0)
    }

    override fun onRoutingFailure(p0: RouteException?) {

    }

    override fun onRoutingStart() {

    }

    override fun onRoutingSuccess(routes: ArrayList<Route>?, shortestRouteIndex: Int) {
        val polyOptions = PolylineOptions()
        var polylineStartLatLng: LatLng? = null
        var polylineEndLatLng: LatLng? = null

        for (i in routes!!.indices) {
            if (i == shortestRouteIndex) {
                val random = Random()
                val color = Color.rgb(random.nextInt(256), random.nextInt(128), random.nextInt(256))
                polyOptions.color(color)
                polyOptions.width(10f)
                polyOptions.addAll(routes.get(shortestRouteIndex).getPoints())
                val polyline: Polyline = mMap.addPolyline(polyOptions)
                polylineStartLatLng = polyline.points[0]
                polylineEndLatLng = polyline.points[polyline.points.size - 1]
            } else {
            }
        }

        val startMarker = MarkerOptions()
        startMarker.position(polylineStartLatLng)
        startMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        startMarker.title("Source")
        mMap.addMarker(startMarker)

        val endMarker = MarkerOptions()
        endMarker.position(polylineEndLatLng)
        endMarker.title("Destination")
        mMap.addMarker(endMarker)
    }

    override fun onRoutingCancelled() {

    }
}