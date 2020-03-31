package com.corona.awareness.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.corona.awareness.R
import com.corona.awareness.databinding.ActivityNearestCenterBinding
import com.corona.awareness.helper.java.CustomMarkerInfoWindowView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_layout.*


class NearestCenterActivity : BaseActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var bindingView: ActivityNearestCenterBinding
    val markersList: MutableList<Marker> = mutableListOf()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_nearest_center)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomSheetBehavior = BottomSheetBehavior.from<ConstraintLayout>(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val karachi = LatLng(24.86, 67.0011)
        val akuLocation = LatLng(24.8923019,67.0687973)
        val dowLocation = LatLng(24.9445138,67.1388251)
        val civilLocation = LatLng(24.859466,67.0106766)


        var mk = mMap.addMarker(MarkerOptions().position(akuLocation).title("Aga Khan Hospital"))
        var mk1 = mMap.addMarker(MarkerOptions().position(dowLocation).title("Dow Hospital"))
        var mk2 = mMap.addMarker(MarkerOptions().position(civilLocation).title("Civil Hospital"))

        markersList.add(mk)
        markersList.add(mk1)
        markersList.add(mk2)


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(karachi, 11.2F))
        var customInfoWindow = CustomMarkerInfoWindowView(this)
        mMap.setInfoWindowAdapter(customInfoWindow)

        mMap.setOnMarkerClickListener { marker ->
            slideUpDownBottomSheet()
            true
        }

      //  showOrHideInfoWindows()



    }

    private fun slideUpDownBottomSheet() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED;

        }
    }


    private fun showOrHideInfoWindows() {
        Log.e("qq ",""+markersList.size)
        for (marker in markersList) {
            if (marker.isInfoWindowShown.not()){
                Log.e("qq ",""+marker.title)
                marker.showInfoWindow()
            }else{
                Log.e("qq 1",""+marker.title)
                marker.hideInfoWindow()
            }
        }
    }
}
