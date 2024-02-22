package com.example.solutionchallenge.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.solutionchallenge.datamodel.Exercise

class ExerciseDiffCallback(private val oldList: List<Exercise>,
                           private val newList: List<Exercise>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList == newList
    }
}
