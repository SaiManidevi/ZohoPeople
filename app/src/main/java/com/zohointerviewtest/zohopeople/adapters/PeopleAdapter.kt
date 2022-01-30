package com.zohointerviewtest.zohopeople.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zohointerviewtest.zohopeople.R
import com.zohointerviewtest.zohopeople.databinding.ItemPersonBinding
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.submodels.Name

class PeopleAdapter :
    PagingDataAdapter<PeopleResult, PeopleAdapter.PeopleAdapterViewHolder>(PeopleResultsDiffCallback()) {

    override fun onBindViewHolder(holder: PeopleAdapterViewHolder, position: Int) {
        val currentPerson = getItem(position)
        if (currentPerson == null) holder.bindPlaceholder(holder.itemView.context)
        else holder.bind(currentPerson, holder.itemView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleAdapterViewHolder {
        return PeopleAdapterViewHolder.from(parent)
    }

    class PeopleAdapterViewHolder(private val binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentPerson: PeopleResult, context: Context) {
            binding.itemTvNameFirst.text = getFirstName(currentPerson.name)
            binding.itemTvNameLast.text = currentPerson.name.last
            val currentPersonImageUrl = currentPerson.picture.thumbnail
            Glide.with(context)
                .load(currentPersonImageUrl)
                .placeholder(R.drawable.placeholder_male)
                .error(R.drawable.placeholder_male)
                .into(binding.ivPersonImage)
        }

        private fun getFirstName(currentPersonName: Name): String {
            return currentPersonName.title.plus(" ")
                .plus(currentPersonName.first)
        }

        fun bindPlaceholder(context: Context) {
            binding.itemTvNameFirst.text = context.getString(R.string.placeholder_text)
        }

        companion object {
            fun from(parent: ViewGroup): PeopleAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPersonBinding.inflate(layoutInflater, parent, false)
                return PeopleAdapterViewHolder(binding)
            }
        }

    }
}

private class PeopleResultsDiffCallback : DiffUtil.ItemCallback<PeopleResult>() {
    override fun areItemsTheSame(oldItem: PeopleResult, newItem: PeopleResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PeopleResult, newItem: PeopleResult): Boolean {
        return oldItem == newItem
    }

}