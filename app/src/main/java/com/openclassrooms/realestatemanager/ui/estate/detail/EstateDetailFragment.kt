package com.openclassrooms.realestatemanager.ui.estate.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEstateDetailBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory

class EstateDetailFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: EstateDetailViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var binding: FragmentEstateDetailBinding
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentEstateDetailBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getEstate().observe(viewLifecycleOwner) { estateDetailViewState ->
            updateUI(estateDetailViewState)
        }
    }

    private fun updateUI(estateDetailViewState: EstateDetailViewState) {
        val adapter = CarouselViewAdapter()

        binding.estateSurface.text = estateDetailViewState.propertyArea
        binding.estateDescription.text = estateDetailViewState.description
        binding.estateNumberOfRooms.text = estateDetailViewState.numberOfRooms.toString()
        binding.estateNumberOfBathrooms.text = estateDetailViewState.numberOfBathrooms.toString()
        binding.estateNumberOfBedrooms.text = estateDetailViewState.numberOfBedrooms.toString()
        binding.estateAddress.text = estateDetailViewState.address
        adapter.submitList(estateDetailViewState.medias)
        binding.carouselRecyclerView.adapter = adapter
    }

    companion object {
        const val ARG_ESTATE_ID = "estateId"

        fun newInstance(estateId: Long): EstateDetailFragment {
            val fragment = EstateDetailFragment()
            val args = Bundle()
            args.putLong(ARG_ESTATE_ID, estateId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        viewModel.getEstate().observe(viewLifecycleOwner) { estateDetailViewState ->

            if(estateDetailViewState.location != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(estateDetailViewState.location, 18.0f))
                googleMap.addMarker(
                    MarkerOptions()
                        .position(estateDetailViewState.location)
                )
            }
        }
    }
}