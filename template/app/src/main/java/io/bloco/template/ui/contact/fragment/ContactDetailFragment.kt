package io.bloco.template.ui.contact.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.bloco.template.R
import io.bloco.template.databinding.FragmentContactDetailBinding
import io.bloco.template.domain.models.Contact
import io.bloco.template.shared.Status
import io.bloco.template.ui.contact.ContactActivity
import io.bloco.template.ui.contact.ContactViewModel

@AndroidEntryPoint
class ContactDetailFragment : Fragment() {
    private var binding: FragmentContactDetailBinding? = null
    private val viewModel: ContactViewModel by activityViewModels()
    private var contact: Contact? = null
    private var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setObserver()
    }

    fun setUpUI() {

        binding?.contactHeart?.setOnClickListener {
            contact?.id?.let {
                viewModel.starContact(
                    contactId = it,
                    isStared = contact?.isStarred ?: 0 != 1
                ).observe(viewLifecycleOwner, Observer { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding?.progressBar?.visibility = View.GONE
                            resource.data?.let { response ->
                                response?.content.let {
                                    val pair: Pair<Int, Contact> =
                                        Pair(position, it) as Pair<Int, Contact>
                                    viewModel.changesInContact.postValue(pair)
                                }
                            }
                        }

                        Status.LOADING -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }

                        Status.ERROR -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        }

        binding?.toolbar?.setOnClickListener {
            (requireActivity() as ContactActivity).onBackPressed()
        }
    }

    fun setObserver() {
        viewModel.changesInContact.observe(viewLifecycleOwner, Observer {
            position = it.first
            setDataOnView(it.second)
        })
    }

    fun setDataOnView(contact: Contact) {
        if (this.contact == null) {
            binding?.contactImage?.let {
                if (contact.thumbnail !== null) {
                    Glide.with(this)
                        .load("https://iie-service-dev.workingllama.com" + contact.thumbnail)
                        .into(it)
                } else {
                    binding?.contactImage?.setImageResource(R.drawable.ic_account)
                }
            }

            binding?.contactName?.text = contact.name
            binding?.contactEmail?.text = contact.email
            binding?.contactPhone?.text = contact.phone
        }
        this.contact = contact

        binding?.contactHeart?.setImageResource(
            if (contact.isStarred != 0) R.drawable.ic_fav_filled else R.drawable.ic_fav_unfilled
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}