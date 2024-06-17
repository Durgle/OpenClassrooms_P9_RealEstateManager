package com.openclassrooms.realestatemanager.ui.estate.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEstateDetailBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.ui.estate.upsert.UpsertEstateFragment
import com.openclassrooms.realestatemanager.utils.Utils

class EstateDetailFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: EstateDetailViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var binding: FragmentEstateDetailBinding
    private lateinit var videoPlayer: VideoView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentEstateDetailBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoPlayer = binding.estateVideo
        initTopAppBar()
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getEstate().observe(viewLifecycleOwner) { estateDetailViewState ->
            updateUI(estateDetailViewState)
            initVideo(estateDetailViewState.videoUri)
        }
    }

    private fun initTopAppBar() {
        if (binding.topAppBar != null) {
            val appBar = binding.topAppBar as MaterialToolbar
            appBar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
            appBar.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.app_bar_edit_button -> {
                        parentFragmentManager.beginTransaction().apply {
                            replace(
                                R.id.main_container,
                                UpsertEstateFragment.newInstance(
                                    requireArguments().getLong(ARG_ESTATE_ID)
                                )
                            )
                                .addToBackStack("estate_edit")
                                .commit()
                        }
                    }
                }
                true
            }
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
        binding.estateAvailable.setText(estateDetailViewState.availability)
        if (estateDetailViewState.pointOfInterest.isEmpty()) {
            binding.estatePointsInterestTitle.visibility = View.GONE
            binding.estatePointsInterest.visibility = View.GONE
        } else {
            binding.estatePointsInterestTitle.visibility = View.VISIBLE
            binding.estatePointsInterest.visibility = View.VISIBLE
        }
        binding.estatePointsInterest.text =
            Utils.formatPointOfInterests(resources, estateDetailViewState.pointOfInterest)
    }

    private fun initVideo(videoUri: String?){
        if (!videoUri.isNullOrBlank()) {
            videoPlayer.setVideoURI(Uri.parse(videoUri))
            val mediaController = MediaController(requireContext())
            videoPlayer.setMediaController(mediaController)
            mediaController.setAnchorView(videoPlayer)
            videoPlayer.visibility = View.VISIBLE
        } else {
            videoPlayer.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.stopPlayback()
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
            googleMap.clear()
            if (estateDetailViewState.location != null) {
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        estateDetailViewState.location,
                        18.0f
                    )
                )
                googleMap.addMarker(
                    MarkerOptions()
                        .position(estateDetailViewState.location)
                )
            }
        }
    }
}