package com.ajax.ajaxtestassignment.ui.contactslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajax.ajaxtestassignment.databinding.FragmentContactsListBinding

open class ContactsListFragment : Fragment() {
    private lateinit var contactAdapter: ContactAdapter

    private var binding: FragmentContactsListBinding? = null
    lateinit var vmFactory: ContactsViewModel.Factory
    lateinit var viewModel: ContactsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        vmFactory = ContactsViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contactAdapter = ContactAdapter(listOf())

        // Creates a vertical Layout Manager
        return FragmentContactsListBinding.inflate(layoutInflater, container, false)
            .apply {
                contactList.layoutManager = LinearLayoutManager(context)
                contactList.adapter = contactAdapter
            }
            .also {
                binding = it
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, vmFactory)[ContactsViewModel::class.java]
        binding?.swipeRefreshLayout?.isRefreshing = true
        getContactsData()
        binding?.swipeRefreshLayout?.setOnRefreshListener { getContactsData() }
    }

    private fun getContactsData() {
        viewModel.getContacts().observe(this as LifecycleOwner, {
            it.data?.let { data ->
                contactAdapter.items = data
                contactAdapter.notifyDataSetChanged()
            }
            it.error?.printStackTrace()
            binding?.swipeRefreshLayout?.isRefreshing = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}