package com.openclassrooms.realestatemanager.ui.estate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEstateBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.ui.estate.upsert.UpsertEstateFragment
import com.openclassrooms.realestatemanager.ui.estate.detail.EstateDetailFragment
import com.openclassrooms.realestatemanager.ui.estate.filter.FilterEstateFragment
import com.openclassrooms.realestatemanager.utils.Utils

class EstateFragment : Fragment(), OnEstateSelectedListener {

    private val viewModel: EstateViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var binding: FragmentEstateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentEstateBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTopAppBar()

        val adapter = EstateViewPagerAdapter(this)
        binding.estateViewPager.adapter = adapter
        binding.estateViewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.estateTabLayout, binding.estateViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setText(R.string.tab_name_list)
                    tab.setIcon(R.drawable.baseline_menu_24)
                }
                1 -> {
                    tab.setText(R.string.tab_name_map)
                    tab.setIcon(R.drawable.baseline_map_24)
                }
            }
        }.attach()
    }

    private fun initTopAppBar() {
        this.binding.topAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.app_bar_add_button -> {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.main_container, UpsertEstateFragment.newInstance())
                            .addToBackStack("estate_add")
                            .commit()
                    }
                }

                R.id.app_bar_search_button -> {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.main_container, FilterEstateFragment.newInstance())
                            .addToBackStack("estate_filter")
                            .commit()
                    }
                }

                R.id.app_bar_edit_button -> {
                    val selectedId = viewModel.getSelectedEstate()
                    if (selectedId == -1L) {
                        Snackbar.make(
                            requireView(),
                            R.string.error_select_estate,
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        parentFragmentManager.beginTransaction().apply {
                            replace(
                                R.id.main_container,
                                UpsertEstateFragment.newInstance(selectedId)
                            )
                                .addToBackStack("estate_edit")
                                .commit()
                        }
                    }
                }
            }
            true
        }
    }

    companion object {
        fun newInstance(): EstateFragment {
            return EstateFragment()
        }
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

    override fun onResume() {
        super.onResume()
        clearSelection()
    }

    override fun onSelection(estateId: Long) {
        viewModel.onSelectedEstate(estateId)
        showDetails(estateId)
    }

    override fun clearSelection() {
        viewModel.clearSelection()
        val fragment = childFragmentManager.findFragmentById(R.id.detail_container)
        fragment?.let {
            childFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(it)
                .commitAllowingStateLoss()
        }
    }
}