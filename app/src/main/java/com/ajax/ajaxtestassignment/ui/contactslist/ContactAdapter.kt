package com.ajax.ajaxtestassignment.ui.contactslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajax.ajaxtestassignment.databinding.ItemContactListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

class ContactAdapter(var items: List<ContactEntity>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            ItemContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder(binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root) {
    val firstName = binding.firstName
    val lastName = binding.lastName
    val image = binding.image

    fun bind(contactEntity: ContactEntity) {
        firstName.text = contactEntity.firstName
        lastName.text = contactEntity.lastName
        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        Glide.with(image)
            .load(contactEntity.photo)
            .skipMemoryCache( false )
            .apply(RequestOptions().circleCrop())
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .placeholder(android.R.color.white)
            .into(image)
    }
}