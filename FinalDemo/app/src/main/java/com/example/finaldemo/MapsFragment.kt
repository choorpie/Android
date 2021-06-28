package com.example.finaldemo

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.finaldemo.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

class MapsFragment : Fragment(), OnMapReadyCallback{

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var args: MapsFragmentArgs
    val TAG = "MapsFragment"
    // Initialize array of place name
    var placeNameList = "ATM,Bank,Hospital,Movie Theater,Restaurant".split(",").toTypedArray()

    private var latitude: Double=0.toDouble()
    private var longitude: Double=0.toDouble()

    private lateinit var mLastLocation: Location
    private var mMarker: Marker?=null

    // Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialize view
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        // retrieve the passed argument (hotel'name and hotel's address)
        args = MapsFragmentArgs.fromBundle(requireArguments())

        // Initialize map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        // Async map
        mapFragment?.getMapAsync(this)

        // Set adapter on spinner
        val adapter = activity?.let { ArrayAdapter<String>(it, R.layout.support_simple_spinner_dropdown_item, placeNameList) }
        binding.spType.adapter = adapter

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        var addressList: List<Address>

        // get the Google map object
        mMap = googleMap
        // use the Geocoder the translate the address into the location
        val geoCoder = Geocoder(context)
        addressList = geoCoder.getFromLocationName(args.address, 1)

        // Add a marker and move the camera
        if (!addressList.isNullOrEmpty()) {
            mMap.isMyLocationEnabled = true

            val location = LatLng(addressList[0].latitude, addressList[0].longitude)
            mMap.addMarker(MarkerOptions().position(location).title(args.name))
            // zoom = 15f
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

            // NDHU
            val local_location = LatLng(23.897532369370133, 121.54138184279184)
            mMap.addMarker(MarkerOptions().position(local_location).title("NDHU"))

            val URL = getDirectionURL(local_location, location)
            Log.i("past", URL)
            GetDirection(URL).execute()

        }
    }

    fun getDirectionURL(origin:LatLng,dest:LatLng): String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&key=AIzaSyAafNXUFM-tN-D5pIRjYCIeeKxyjahpepw"
    }

    inner class GetDirection(val url: String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg p0: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body?.string()
//            Log.i("now", data)
            val result = ArrayList<List<LatLng>>()
            try {
//                Log.i("now", "try")
                val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)
//                Log.i("now", "end try")
                val path = ArrayList<LatLng>()

                for (i in 0..(respObj.routes[0].legs[0].steps.size-1)){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e: Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
//            Log.i("now", "indices" + result.indices)

            mMap.addPolyline(lineoption)
        }
    }
    public fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len){
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while(b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do{
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            }while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}