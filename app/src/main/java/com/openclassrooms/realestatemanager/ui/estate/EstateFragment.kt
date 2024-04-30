package com.openclassrooms.realestatemanager.ui.estate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory

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
                    showDetails(estateId)
                }
            }
        val adapter = EstateRecyclerViewAdapter(listener)
        viewModel.getEstates().observe(viewLifecycleOwner) { list -> adapter.submitList(list) }

        this.binding.estateList.adapter = adapter
    }

    companion object {
        fun newInstance(): EstateFragment {
            return EstateFragment()
        }
    }

    fun showDetails(estateId: Long) {

        if (resources.getBoolean(R.bool.isLargeLayout)) {
            parentFragmentManager.beginTransaction().apply {
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
}