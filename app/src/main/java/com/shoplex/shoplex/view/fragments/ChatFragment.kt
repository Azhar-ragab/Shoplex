package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentChatBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.StoreHeadAdapter
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.viewmodel.ChatHeadVM

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatsVm: ChatHeadVM
    private lateinit var chatsAdapter: ChatHeadAdapter
    private lateinit var storeHeadsAdapter: StoreHeadAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        if(UserInfo.userID == null){
            Toast.makeText(context, getString(R.string.pleaseLogin),Toast.LENGTH_SHORT
            ).show()
            return binding.root
        }

        chatsVm = ViewModelProvider(requireActivity()).get(ChatHeadVM::class.java)

        if (chatsVm.chatHeads.value == null)
            chatsVm.getChatHeads()

        requireActivity().title = getString(R.string.chat)

        chatsVm.chatHeads.observe(viewLifecycleOwner, { chatHeads ->
            if (chatHeads.count()>0) {
                binding.noItem.visibility=View.INVISIBLE
            }
            else{
                binding.noItem.visibility=View.VISIBLE
            }
            chatsAdapter = ChatHeadAdapter(chatHeads)
            storeHeadsAdapter = StoreHeadAdapter(chatHeads)
            binding.rvChat.adapter = chatsAdapter
            binding.rvStore.adapter = storeHeadsAdapter
        })

        chatsVm.changedPosition.observe(viewLifecycleOwner, {
            binding.rvChat.adapter?.notifyItemChanged(it)
            binding.rvStore.adapter?.notifyItemChanged(it)
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    chatsAdapter.search("")
                    storeHeadsAdapter.search("")
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val search = binding.searchView.query.toString()
                chatsAdapter.search(search)
                storeHeadsAdapter.search(search)
                return false
            }
        })

        return binding.root
    }
}