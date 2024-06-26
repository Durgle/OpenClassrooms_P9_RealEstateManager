package com.openclassrooms.realestatemanager.ui.estate.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.ui.estate.OnEstateSelectedListener

class EstateListFragment() : Fragment() {

    private val viewModel: EstateListViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var binding: FragmentEstateListBinding
    private lateinit var listener: OnEstateSelectedListener

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
                    listener.onSelection(estateId)
                }
            }
        val adapter = EstateRecyclerViewAdapter(listener)
        viewModel.getEstates().observe(viewLifecycleOwner) { list -> adapter.submitList(list) }
        this.binding.estateList.adapter = adapter
    }

    companion object {
        fun newInstance(): EstateListFragment {
            return EstateListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as OnEstateSelectedListener
    }

}