package com.shoplex.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentChatBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.StoreHeadAdapter
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Chat
import com.shoplex.shoplex.model.pojo.ChatHead
import com.shoplex.shoplex.model.pojo.Product
import com.shoplex.shoplex.viewmodel.ChatHeadVM

class ChatFragment : Fragment() {
    private lateinit var  binding : FragmentChatBinding
    private lateinit var chatHeadAdapter: ChatHeadAdapter
    private lateinit var chatHeadVm: ChatHeadVM
    // private lateinit var storeHeadAdapter: StoreHeadAdapter
    // private var chatHeadList = arrayListOf<ChatHead>()
    // private var storeHeadList = arrayListOf<ChatHead>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        this.chatHeadVm = ChatHeadVM()
        // (activity as AppCompatActivity?)!!.supportActionBar!!.title = binding.root.context.getString(R.string.chat)
            // Inflate the layout for this fragment
        // setHasOptionsMenu(true)
            //getChatHeadsInfo()

        chatHeadVm.getChatHead()
        chatHeadVm.chatHead.observe(viewLifecycleOwner,  { chatHeads ->
            if (chatHeads != null) {
                chatHeadAdapter = ChatHeadAdapter(chatHeads)
                binding.rvChat.adapter = chatHeadAdapter
            }
        })

        return binding.root
    }
    /*
    private fun getChatHeadsInfo() {
        FirebaseReferences.chatRef.whereEqualTo("userID", UserInfo.userID).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.exists()) {
                        var chat: Chat = document.toObject()
                        var product = Product()
                        FirebaseReferences.productsRef
                            .document(chat.productIDs.last()).get()
                            .addOnSuccessListener { productDocument ->
                                if (productDocument.exists()) {
                                    product = productDocument.toObject<Product>()!!
                                    //Toast.makeText(context, product.category, Toast.LENGTH_LONG).show()
                                }
                                chatHeadList.add(
                                    ChatHead(
                                        chat.productIDs,
                                        product.storeID,
                                        chat.chatID,
                                        product.name,
                                        product.price,
                                        product.images.firstOrNull(),
                                        chat.userID,
                                        product.storeName,
                                        chat.unreadCustomerMessages
                                    )
                                )
                                if (document.equals(result.last())) {
                                    chatHeadAdapter = ChatHeadAdapter(chatHeadList)
                                    binding.rvChat.adapter = chatHeadAdapter
                                    storeHeadAdapter = StoreHeadAdapter(chatHeadList)
                                    binding.rvStore.adapter = storeHeadAdapter
                                }
                            }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, getString(R.string.Error), Toast.LENGTH_LONG).show()
            }
    }
    */
}