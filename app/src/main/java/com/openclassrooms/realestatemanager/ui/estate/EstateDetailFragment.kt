package com.openclassrooms.realestatemanager.ui.estate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.databinding.FragmentEstateDetailBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.ui.estate.viewstate.EstateDetailViewState

class EstateDetailFragment : Fragment() {

    private val viewModel: EstateDetailViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var binding: FragmentEstateDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentEstateDetailBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}