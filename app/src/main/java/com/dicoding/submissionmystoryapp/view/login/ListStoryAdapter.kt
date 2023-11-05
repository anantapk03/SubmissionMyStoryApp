package com.dicoding.submissionmystoryapp.view.login

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionmystoryapp.data.remote.response.ListStoryItem
import com.dicoding.submissionmystoryapp.databinding.ItemRowBinding
import com.dicoding.submissionmystoryapp.view.main.DetailStoryActivity


class ListStoryAdapter : ListAdapter<ListStoryItem, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder (val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story : ListStoryItem){
            binding.titleStory.text = story.name.toString()
            binding.previewStory.text = story.description.toString()
            Glide.with(itemView.context)
                .load(story.photoUrl.toString())
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intentDetail.putExtra("ID", data.id)
            val optionsCompat : ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.imageView, "fotoStory"),
                    Pair(holder.binding.titleStory, "tittle"),
                    Pair(holder.binding.previewStory, "description")
                )
            holder.itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}