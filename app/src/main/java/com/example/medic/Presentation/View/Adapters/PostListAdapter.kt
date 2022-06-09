package com.example.medic.Presentation.View.Adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.Post
import com.example.medic.MainActivity
import com.example.medic.R
import com.example.medic.databinding.PostListElementBinding

class PostListAdapter() : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {
    private lateinit var localDataSet: List<Post?>
    var text: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostListElementBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.postCard.setOnClickListener { v ->
            val bundle = Bundle()
            val json: String = ServiceLocator.gson!!.toJson(
                localDataSet[holder.adapterPosition]
            )
            bundle.putString("Post", json)
            Navigation.findNavController(v).navigate(R.id.action_postList_to_postFragment, bundle)
        }
        holder.binding.title.text = localDataSet.get(position)!!.title
        if (localDataSet[position]!!.body!!.length >= 40) {
            text = localDataSet[position]!!.body!!.subSequence(0, 41).toString() + "..."
            holder.binding.body.text = text
        } else {
            holder.binding.body.text = localDataSet.get(position)!!.body
        }
        if (localDataSet[position]!!.date != null) {
            holder.binding.date.text = localDataSet.get(position)!!.date!!.subSequence(0, 10)
        } else {
            holder.binding.date.text = "Sometime in the past"
        }
        holder.binding.tags.text = localDataSet.get(position)!!.tags
        if (localDataSet[position]!!.user != null) {
            holder.binding.nameOfAuthor.text = localDataSet.get(position)!!.user!!.firstName
        } else {
            holder.binding.nameOfAuthor.text = "Anon"
        }

        /*if (localDataSet.get(position).getImages() != null && !localDataSet.get(position).getImages().isEmpty()){
            holder.binding.imageSlider
                    .setVisibility(View.VISIBLE);
            holder.binding.imageSlider.setAdapter(new ImageSliderAdapter(localDataSet.get(position).getImages(),
                    false,
                    mainActivity));
        } else {
            holder.binding.imageSlider
                    .setVisibility(View.GONE);
        }*/
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }

    class ViewHolder(val binding: PostListElementBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    fun setDataAndActivity(localDataSet: List<Post?>?) {
        this.localDataSet = localDataSet!!
    }
}