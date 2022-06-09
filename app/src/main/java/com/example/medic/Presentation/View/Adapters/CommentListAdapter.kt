package com.example.medic.Presentation.View.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.Domain.Model.Comment
import com.example.medic.MainActivity
import com.example.medic.databinding.CommentListElementBinding

class CommentListAdapter: RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {
    private lateinit var localDataSet: List<Comment?>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CommentListElementBinding = CommentListElementBinding
            .inflate(LayoutInflater.from(parent.getContext()), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.user.setText(localDataSet.get(position)!!.userName)
        holder.binding.body.setText(localDataSet.get(position)!!.body)
        holder.binding.date.setText(localDataSet.get(position)!!.date!!.subSequence(0, 10))
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }

    class ViewHolder constructor(val binding: CommentListElementBinding) : RecyclerView.ViewHolder(
        binding.getRoot()
    )

    fun setDataAndActivity(localDataSet: List<Comment?>?) {
        this.localDataSet = localDataSet!!
    }
}