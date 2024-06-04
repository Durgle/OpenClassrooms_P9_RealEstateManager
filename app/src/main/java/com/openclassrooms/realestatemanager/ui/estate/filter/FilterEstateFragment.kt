package com.openclassrooms.realestatemanager.ui.estate.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.databinding.FragmentFilterEstateBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.utils.afterTextChanged


class FilterEstateFragment : Fragment() {

    private val viewModel: FilterEstateViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    private lateinit var binding: FragmentFilterEstateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentFilterEstateBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTopAppBar()
        initRangeSlider()
        initTypeChip()
        initTextListener()
        initRadioListener()

        viewModel.snackBar.observe(viewLifecycleOwner) { event ->

            when (event) {
                is FilterEstateViewModel.Event.SaveSuccess -> {
                    Snackbar.make(view, R.string.filter_applied_success, Snackbar.LENGTH_SHORT)
                        .show()
                    parentFragmentManager.popBackStack()
                }

                is FilterEstateViewModel.Event.Error -> {
                    Snackbar.make(
                        view,
                        event.message.ifEmpty { getString(R.string.save_general_error) },
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.getCurrentFilter().observe(viewLifecycleOwner) { filterState ->
            val minPrice = filterState.minPrice
                ?: requireContext().resources.getIntArray(R.array.default_slider_values)[0].toFloat()
            val maxPrice = filterState.maxPrice
                ?: requireContext().resources.getIntArray(R.array.default_slider_values)[1].toFloat()
            binding.priceFilterRange.setValues(minPrice, maxPrice)

            binding.cityFilterInput.setText(filterState.city)

            binding.typeFilterChip.clearCheck()
            filterState.type.forEach { type -> binding.typeFilterChip.check(type.id) }

            when (filterState.available) {
                true -> binding.availableFilterYes.isChecked = true
                false -> binding.availableFilterNo.isChecked = true
                null -> binding.availableFilterAll.isChecked = true
            }
        }
    }

    private fun initTopAppBar() {
        binding.filterTopAppBar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        binding.filterTopAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.app_bar_save_button -> {
                    viewModel.save()
                }

                R.id.app_bar_clear_button -> {
                    viewModel.clearFilters()
                    Snackbar.make(requireView(), R.string.filter_cleared, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
            true
        }
    }

    private fun initRangeSlider() {
        binding.priceFilterRange.addOnChangeListener { slider, _, _ ->
            val minValue = slider.values[0].toInt().toString()
            val maxValue = slider.values[1].toInt().toString()
            viewModel.onPriceChanged(minValue, maxValue)
        }
    }

    private fun initTextListener() {
        binding.cityFilterInput.afterTextChanged { city -> viewModel.onCityChanged(city) }
    }

    private fun initRadioListener() {
        binding.availableFilterGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.available_filter_yes -> {
                    viewModel.onAvailableChanged(true)
                }

                R.id.available_filter_no -> {
                    viewModel.onAvailableChanged(false)
                }

                R.id.available_filter_all -> {
                    viewModel.onAvailableChanged(null)
                }
            }
        }
    }

    private fun initTypeChip() {
        val typeChipGroup = binding.typeFilterChip

        PropertyType.entries.forEach { typeEnum ->
            val chip = Chip(requireContext())
            chip.text = getString(typeEnum.labelResId)
            chip.isCheckable = true
            chip.id = typeEnum.id
            chip.setOnCheckedChangeListener { _, isSelected ->
                viewModel.onTypeSelected(typeEnum, isSelected)
            }
            typeChipGroup.addView(chip)
        }
    }

    companion object {
        fun newInstance(): FilterEstateFragment {
            return FilterEstateFragment()
        }
    }

}