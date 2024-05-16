package com.openclassrooms.realestatemanager.ui.estate.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.ui.estate.create.CreateEstateFragment
import com.openclassrooms.realestatemanager.ui.estate.detail.EstateDetailFragment
import com.openclassrooms.realestatemanager.ui.estate.filter.FilterEstateFragment
import com.openclassrooms.realestatemanager.utils.Utils

class EstateFragment : Fragment() {

    private val viewModel: EstateViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var binding: FragmentEstateListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentEstateListBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listener: EstateRecyclerViewAdapter.OnItemClickListener =
            object : EstateRecyclerViewAdapter.OnItemClickListener {
                override fun onClick(estateId: Long) {
                    viewModel.onSelectedEstate(estateId)
                    showDetails(estateId)
                }
            }
        val adapter = EstateRecyclerViewAdapter(listener)
        viewModel.getEstates().observe(viewLifecycleOwner) { list -> adapter.submitList(list) }
        this.binding.estateList.adapter = adapter
        initTopAppBar()
    }

    private fun initTopAppBar() {
        this.binding.topAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.app_bar_add_button -> {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.main_container, CreateEstateFragment.newInstance())
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
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.main_container, CreateEstateFragment.newInstance())
                            .addToBackStack("estate_edit")
                            .commit()
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

    fun showDetails(estateId: Long) {

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
        viewModel.clearSelection()
    }
}