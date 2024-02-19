package com.example.solutionchallenge.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solutionchallenge.adapter.ExerciseAdapter
import com.example.solutionchallenge.databinding.FragmentRecommendListBinding
import com.example.solutionchallenge.datamodel.Exercise
import com.example.solutionchallenge.viewmodel.ExerciseViewModel


class RecommendListFragment(private val receivedAccessToken :String ) : Fragment() {
    private var binding: FragmentRecommendListBinding? = null
    private val recommendationViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.Factory(requireActivity().application)
    }

    private val adapter: ExerciseAdapter by lazy { ExerciseAdapter(recommendationViewModel, true) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRecommendListBinding.inflate(inflater, container, false)
        adapter.setHasStableIds(true)

        binding!!.PERRecyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding!!.PERRecyclerview.adapter = adapter

        val exerciseList = arguments?.getParcelableArrayList<Exercise>("exerciseList")

        adapter.setAccessToken(receivedAccessToken)

        exerciseList?.let {
            adapter.setData(it)
        }

        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}