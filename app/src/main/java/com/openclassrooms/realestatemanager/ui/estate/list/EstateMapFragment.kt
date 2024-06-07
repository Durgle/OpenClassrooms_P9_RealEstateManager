package com.openclassrooms.realestatemanager.ui.estate.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEstateMapBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.ui.estate.OnEstateSelectedListener
import com.openclassrooms.realestatemanager.utils.Utils

class EstateMapFragment(private val listener: OnEstateSelectedListener) : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val viewModel: EstateListViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var binding: FragmentEstateMapBinding

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
        if (Utils.isInternetAvailable(requireContext())) {
            viewModel.getEstates().observe(viewLifecycleOwner) { estateList ->
                googleMap.clear()
                estateList.forEach { estate ->
                    addMarker(estate, googleMap)
                }
            }
            googleMap.uiSettings.isZoomControlsEnabled = true;
            googleMap.setOnMarkerClickListener(this);
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

}