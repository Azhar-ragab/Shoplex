package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentChatBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.viewmodel.ChatHeadVM

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatsVm: ChatHeadVM
    private lateinit var chatsAdapter: ChatHeadAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        chatsVm = ViewModelProvider(requireActivity()).get(ChatHeadVM::class.java)

        if (chatsVm.chatHeads.value == null)
            chatsVm.getChatHeads()

        requireActivity().title = getString(R.string.chat)

        chatsVm.chatHeads.observe(viewLifecycleOwner, { chatHeads ->
            chatsAdapter = ChatHeadAdapter(chatHeads)
            binding.rvChat.adapter = chatsAdapter
        })

        chatsVm.changedPosition.observe(viewLifecycleOwner, {
            binding.rvChat.adapter?.notifyItemChanged(it)
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    chatsAdapter.search("")
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                chatsAdapter.search(binding.searchView.query.toString())
                return false
            }
        })

        return binding.root
    }
}