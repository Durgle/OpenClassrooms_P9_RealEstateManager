package com.openclassrooms.realestatemanager.ui.estate.list

import android.Manifest.permission
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEstateMapBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.ui.estate.OnEstateSelectedListener

class EstateMapFragment(private val listener: OnEstateSelectedListener) : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val viewModel: EstateListViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var binding: FragmentEstateMapBinding
    private lateinit var googleMap: GoogleMap

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                enableCurrentLocation()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstateMapBinding.inflate(inflater, container, false)
        val mapFragment =
            (childFragmentManager.findFragmentById(R.id.estate_map_container) as SupportMapFragment)
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        checkLocationPermission()
        viewModel.getCurrentLocation().observe(viewLifecycleOwner, object : Observer<Location?> {
            override fun onChanged(value: Location?) {
                if (value != null) {
                    val position = LatLng(value.latitude, value.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12.0f))
                    viewModel.getCurrentLocation().removeObserver(this)
                }
            }
        })
        viewModel.getEstates().observe(viewLifecycleOwner) { estateList ->
            googleMap.clear()
            estateList.forEach { estate ->
                addMarker(estate, googleMap)
            }
        }
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMarkerClickListener(this)

    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableCurrentLocation()
        } else {
            requestPermissionLauncher.launch(permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun enableCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
    }

    private fun addMarker(estate: EstateViewState, googleMap: GoogleMap) {

        if (estate.location != null) {
            val markerOptions = MarkerOptions()
            markerOptions.position(estate.location)
                .title(resources.getString(estate.propertyType.labelResId))
            if (estate.selected) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            val marker = googleMap.addMarker(markerOptions)
            if (marker != null) {
                marker.tag = estate.id
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val estateId = marker.tag as? Long
        estateId?.let { id ->
            listener.onSelection(id)
        }
        return false
    }

    companion object {
        fun newInstance(listener: OnEstateSelectedListener): EstateMapFragment {
            return EstateMapFragment(listener)
        }
    }

    override fun onResume() {
        super.onResume()
        val granted = ContextCompat.checkSelfPermission(requireActivity(),permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        viewModel.refreshLocation(granted)
    }

}