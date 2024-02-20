package com.example.solutionchallenge.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solutionchallenge.adapter.ExerciseAdapter
import com.example.solutionchallenge.databinding.FragmentBookmarkBinding
import com.example.solutionchallenge.datamodel.Exercise
import com.example.solutionchallenge.viewmodel.ExerciseViewModel


class BookmarkFragment(private val receivedAccessToken :String) : Fragment() {
    private var binding : FragmentBookmarkBinding? = null
    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.Factory(requireActivity().application)
    } // 뷰모델 연결
    private val adapter : ExerciseAdapter by lazy { ExerciseAdapter(exerciseViewModel, false) } // 어댑터 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰바인딩

        val exerciseList = arguments?.getParcelableArrayList<Exercise>("exerciseList")

        binding = FragmentBookmarkBinding.inflate(inflater,container,false)
        adapter.setHasStableIds(true)

        // 아이템을 가로로 하나씩 보여주고 어댑터 연결
        binding!!.BookmarkRecyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        binding!!.BookmarkRecyclerview.adapter = adapter

        exerciseList?.let {
            adapter.setData(it)
        }

        adapter.setAccessToken(receivedAccessToken)
        // exerciseViewModel.readBookmarkData.observe(viewLifecycleOwner, Observer {
        //  adapter.setData(it)
        // })

        return binding!!.root
    }

}