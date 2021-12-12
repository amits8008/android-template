package io.bloco.template.ui.contact.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.ItemSnapshotList
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.bloco.template.R
import io.bloco.template.databinding.ItemContactBinding
import io.bloco.template.domain.models.Contact

class ContactAdapter(val clickListener: (Boolean, Int, Contact?) -> Unit) :
    PagingDataAdapter<Contact, ContactAdapter.ContactViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
                oldItem.phone == newItem.phone

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
                oldItem.phone == newItem.phone
        }
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        with(holder)
        {
            with(getItem(position))
            {
                binding.contactName.text = this?.name
                binding.contactPhone.text = this?.phone
                binding.contactEmail.text = this?.email
                binding.contactStarred.setImageResource(
                    if (this?.isStarred != 0) R.drawable.ic_fav_filled else R.drawable.ic_fav_unfilled
                )

                if (this?.thumbnail !== null) {
                    Glide.with(binding.contactImage.context)
                        .load("https://iie-service-dev.workingllama.com" + this.thumbnail)
                        .into(binding.contactImage)
                } else {
                    binding.contactImage.setImageResource(R.drawable.ic_account)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    inner class ContactViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                clickListener.invoke(
                    false,
                    bindingAdapterPosition,
                    getItem(bindingAdapterPosition)
                )
            }

            binding.contactStarred.setOnClickListener {
                clickListener.invoke(
                    true,
                    bindingAdapterPosition,
                    getItem(bindingAdapterPosition)
                )
            }
        }
    }


}