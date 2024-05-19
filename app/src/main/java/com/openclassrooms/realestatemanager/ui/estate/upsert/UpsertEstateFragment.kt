package com.openclassrooms.realestatemanager.ui.estate.upsert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import com.openclassrooms.realestatemanager.databinding.FragmentCreateEstateBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.utils.afterTextChanged


class UpsertEstateFragment : Fragment() {

    private val viewModel: UpsertEstateViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var binding: FragmentCreateEstateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentCreateEstateBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTopAppBar()
        initPropertyTypeSelect()
        initAgentSelect()
        initPointsOfInterestChip()
        initListeners()

        viewModel.getErrorsViewState().observe(viewLifecycleOwner) { viewState ->
            renderErrors(viewState)
        }
        viewModel.getCurrentData().observe(viewLifecycleOwner) { currentData ->

            if (currentData != null) {
                if (currentData.type != null) {
                    binding.estateTypeInput.setText(currentData.type.name, false)
                }
                binding.estatePriceInput.setText(currentData.price)
                binding.estateSurfaceInput.setText(currentData.surface)
                binding.estateNumberBathroomsInput.setText(currentData.numberOfBathroom)
                binding.estateNumberBedroomsInput.setText(currentData.numberOfBedroom)
                binding.estateDescriptionInput.setText(currentData.description)
                binding.estateAddressInput.setText(currentData.address)
                binding.estateAdditionalAddressInput.setText(currentData.additionalAddress)
                binding.estateZipcodeInput.setText(currentData.zipcode)
                binding.estateCityInput.setText(currentData.city)
                binding.estateCountryInput.setText(currentData.country)
                if (currentData.agent != null) {
                    binding.estateAgentInput.setText(currentData.agent.displayName, false)
                }
                currentData.pointsOfInterest.forEach { pointOfInterest ->
                    binding.estatePointsInterestChip.check(
                        pointOfInterest.id
                    )
                }
                binding.estateAvailableSwitch.isChecked = currentData.available
            }

        }

        viewModel.snackBar.observe(viewLifecycleOwner) { event ->

            when (event) {
                is UpsertEstateViewModel.Event.SaveSuccess -> {
                    Snackbar.make(view, R.string.save_success, Snackbar.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }

                is UpsertEstateViewModel.Event.Error -> {
                    Snackbar.make(
                        view,
                        event.message.ifEmpty { getString(R.string.save_error) },
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun renderErrors(viewState: UpsertEstateViewState) {
        binding.estateTypeLayout.error = viewState.errorType
        binding.estatePriceLayout.error = viewState.errorPrice
        binding.estateSurfaceLayout.error = viewState.errorSurface
        binding.estateNumberBathroomsLayout.error = viewState.errorNumberOfBathroom
        binding.estateNumberBedroomsLayout.error = viewState.errorNumberOfBedroom
        binding.estateDescriptionLayout.error = viewState.errorDescription
        binding.estateAddressLayout.error = viewState.errorAddress
        binding.estateAdditionalAddressLayout.error = viewState.errorAdditionalAddress
        binding.estateZipcodeLayout.error = viewState.errorZipcode
        binding.estateCityLayout.error = viewState.errorCity
        binding.estateCountryLayout.error = viewState.errorCountry
        binding.estateAgentLayout.error = viewState.errorAgent
    }

    private fun initTopAppBar() {
        binding.upsertTopAppBar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        binding.upsertTopAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.app_bar_save_button -> {
                    viewModel.save()
                }
            }
            true
        }

        if (requireArguments().getLong(ARG_ESTATE_ID, -1) == -1L) {
            binding.upsertTopAppBar.setTitle(R.string.app_bar_add)
        } else {
            binding.upsertTopAppBar.setTitle(R.string.app_bar_edit)
        }
    }

    private fun initPointsOfInterestChip() {
        val pointOfInterestChip = binding.estatePointsInterestChip

        PointOfInterest.entries.forEach { pointOfInterestEnum ->
            val chip = Chip(requireContext())
            chip.text = getString(pointOfInterestEnum.labelResId)
            chip.isCheckable = true
            chip.id = pointOfInterestEnum.id
            chip.setOnCheckedChangeListener { _, isSelected ->
                viewModel.onPointOfInterestSelected(pointOfInterestEnum, isSelected)
            }
            pointOfInterestChip.addView(chip)
        }
    }

    private fun initPropertyTypeSelect() {
        val propertyTypeList: List<PropertyType> = PropertyType.entries.toList()
        val adapterPropertyType =
            PropertyTypeArrayAdapter(requireContext(), propertyTypeList)
        (binding.estateTypeLayout.editText as AutoCompleteTextView).setAdapter(adapterPropertyType)

        binding.estateTypeInput.setOnItemClickListener { _, _, position, _ ->
            viewModel.onTypeChanged(adapterPropertyType.getItem(position))
        }
    }

    private fun initAgentSelect() {
        viewModel.getRealEstateAgent().observe(viewLifecycleOwner) { agentList ->
            val agentAdapter = RealEstateAgentArrayAdapter(requireContext(), agentList)
            (binding.estateAgentLayout.editText as AutoCompleteTextView).setAdapter(agentAdapter)
            binding.estateAgentInput.setOnItemClickListener { _, _, position, _ ->
                viewModel.onAgentChanged(agentAdapter.getItem(position))
            }
        }
    }

    private fun initListeners() {
        binding.estatePriceInput.afterTextChanged { price -> viewModel.onPriceChanged(price) }
        binding.estateSurfaceInput.afterTextChanged { surface -> viewModel.onSurfaceChanged(surface) }
        binding.estateNumberBathroomsInput.afterTextChanged { numberBathrooms ->
            viewModel.onNumberOfBathroomChanged(numberBathrooms)
        }
        binding.estateNumberBedroomsInput.afterTextChanged { numberBedrooms ->
            viewModel.onNumberOfBedroomChanged(numberBedrooms)
        }
        binding.estateDescriptionInput.afterTextChanged { description ->
            viewModel.onDescriptionChanged(description)
        }

        binding.estateAddressInput.afterTextChanged { address -> viewModel.onAddressChanged(address) }
        binding.estateAdditionalAddressInput.afterTextChanged { additionalAddress ->
            viewModel.onAdditionalAddressChanged(additionalAddress)
        }
        binding.estateZipcodeInput.afterTextChanged { zipcode -> viewModel.onZipcodeChanged(zipcode) }
        binding.estateCityInput.afterTextChanged { city -> viewModel.onCityChanged(city) }
        binding.estateCountryInput.afterTextChanged { country -> viewModel.onCountryChanged(country) }
        binding.estateAvailableSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onAvailableChange(isChecked)
        }
    }

    companion object {
        const val ARG_ESTATE_ID = "estateId"

        fun newInstance(estateId: Long? = null): UpsertEstateFragment {
            val fragment = UpsertEstateFragment()
            val args = Bundle()
            if (estateId != null) {
                args.putLong(ARG_ESTATE_ID, estateId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    class PropertyTypeArrayAdapter(context: Context, propertyTypes: List<PropertyType>) :
        ArrayAdapter<PropertyType>(context, R.layout.list_item, propertyTypes) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent) as TextView
            view.setText(getItem(position)?.labelResId ?: R.string.value_unknown)

            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getView(position, convertView, parent)
        }
    }

    class RealEstateAgentArrayAdapter(context: Context, realEstateAgents: List<RealEstateAgent>) :
        ArrayAdapter<RealEstateAgent>(context, R.layout.list_item, realEstateAgents) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent) as TextView
            val agent = getItem(position)
            if (agent != null) view.text = agent.displayName

            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getView(position, convertView, parent)
        }
    }

}