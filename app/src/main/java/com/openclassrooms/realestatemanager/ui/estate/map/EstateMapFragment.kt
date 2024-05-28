package com.openclassrooms.realestatemanager.ui.estate.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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
import com.openclassrooms.realestatemanager.ui.estate.detail.EstateDetailFragment
import com.openclassrooms.realestatemanager.utils.Utils

class EstateMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val viewModel: MapViewModel by viewModels {
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

    private fun addMarker(estate: EstateMapViewState, googleMap: GoogleMap) {

        if (estate.location != null) {
            val markerOptions = MarkerOptions()
            markerOptions.position(estate.location).title(resources.getString(estate.type))
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
            viewModel.onSelectedEstate(estateId)
            showDetails(id)
        }
        return false
    }

    private fun showDetails(estateId: Long) {

        if (Utils.isTablet(resources)) {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.detail_container, EstateDetailFragment.newInstance(estateId))
                    .addToBackStack("estate_detail")
                    .commit()
            }
        } else {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.main_container, EstateDetailFragment.newInstance(estateId))
                    .addToBackStack("estate_detail")
                    .commit()
            }
        }
    }

    companion object {
        fun newInstance(): EstateMapFragment {
            return EstateMapFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        val fragment = childFragmentManager.findFragmentById(R.id.detail_container)
        fragment?.let {
            childFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(it)
                .commitAllowingStateLoss()
        }
        viewModel.clearSelection()
    }

}