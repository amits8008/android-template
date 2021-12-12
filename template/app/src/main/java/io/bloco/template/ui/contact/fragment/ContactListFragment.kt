package io.bloco.template.ui.contact.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.bloco.template.R
import io.bloco.template.databinding.FragmentContactListBinding
import io.bloco.template.domain.models.BaseResponse
import io.bloco.template.domain.models.Contact
import io.bloco.template.shared.Resource
import io.bloco.template.shared.Status
import io.bloco.template.ui.contact.ContactActivity
import io.bloco.template.ui.contact.ContactViewModel
import io.bloco.template.ui.contact.fragment.adapter.ContactAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactListFragment : Fragment() {

    private var binding: FragmentContactListBinding? = null
    private val viewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setObserver()

        //Initiated page source for the recycler view
        lifecycleScope.launch {
            viewModel.fetchContactList().distinctUntilChanged().collectLatest {
                (binding?.rvContacts?.adapter as ContactAdapter).submitData(it)
            }
        }
    }

    fun setAdapter() {
        binding?.rvContacts?.adapter = ContactAdapter { heartClicked, position, contact ->
            if (heartClicked) {
                contact?.id?.let {
                    viewModel.starContact(
                        contactId = it,
                        isStared = contact.isStarred ?: 0 != 1
                    ).observe(viewLifecycleOwner, Observer { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                binding?.progressBar?.visibility = View.GONE
                                resource.data?.let { response ->
                                    val pair: Pair<Int, Contact> =
                                        Pair(position, response.content) as Pair<Int, Contact>
                                    viewModel.changesInContact.postValue(pair)
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
            } else {
                contact.let {
                    val pair: Pair<Int, Contact> = Pair(position, it) as Pair<Int, Contact>
                    viewModel.changesInContact.postValue(pair)
                }

                activity?.supportFragmentManager?.beginTransaction()
                    ?.add(R.id.root_container, ContactDetailFragment())
                    ?.addToBackStack(ContactDetailFragment::class.java.name)
                    ?.commitAllowingStateLoss()

            }
        }

        //Showing progress bar with paging adapter
        viewLifecycleOwner.lifecycleScope.launch {
            (binding?.rvContacts?.adapter as ContactAdapter).loadStateFlow.collectLatest {
                binding?.progressBar?.isVisible = it.refresh is LoadState.Loading
            }
        }
    }

    fun setObserver() {
        viewModel.changesInContact.observe(viewLifecycleOwner, Observer {
            (binding?.rvContacts?.adapter as ContactAdapter).snapshot().get(it.first)?.isStarred =
                it.second.isStarred
            binding?.rvContacts?.adapter?.notifyItemChanged(it.first)
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}