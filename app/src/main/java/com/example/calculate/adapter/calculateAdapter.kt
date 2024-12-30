package com.example.calculate.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculate.databinding.ItemRecyclerBinding
import com.example.calculate.model.calculate

class calculateAdapter() : RecyclerView.Adapter<calculateAdapter.MyViewHolder>() {

    val differ = mutableListOf<calculate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ[position])
    }

    inner class MyViewHolder(
        private val binding : ItemRecyclerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(calculate: calculate) {
            binding.tvExpression.text = calculate.expression
            binding.tvAnswer.text = calculate.answer
        }
    }
}