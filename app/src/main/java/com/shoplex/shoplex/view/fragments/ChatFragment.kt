package eg.gov.iti.shoplex.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.Product
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentChatBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.StoreHeadAdapter
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Chat
import com.shoplex.shoplex.model.pojo.ChatHead
import kotlin.jvm.internal.FunctionReference


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var chatHeadAdapter: ChatHeadAdapter
    private lateinit var storeHeadAdapter: StoreHeadAdapter
    private lateinit var  binding : FragmentChatBinding
    private var chatHeadList = arrayListOf<ChatHead>()
    private var storeHeadList = arrayListOf<ChatHead>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
      binding = FragmentChatBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        getChatHeadsInfo()
        getStoreHeadsInfo()
//        //chat Recycler View
//
//        val chatHead = ArrayList<ChatHead>()
//        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))
//        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))
//        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))
//        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))
//        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))
//
//        chatHeadAdapter = ChatHeadAdapter(chatHead)
//        binding.rvChat.addItemDecoration(
//                DividerItemDecoration(
//                        binding.rvChat.getContext(),
//                        DividerItemDecoration.VERTICAL
//                )
//        )
//        binding.rvChat.adapter = chatHeadAdapter

//
//        //Store Recycler View
//        val storeHead= ArrayList<ChatHead>()
//        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
//        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
//        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
//        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
//        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
//        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
//
//        storeHeadAdapter= StoreHeadAdapter(storeHead)
//        binding.rvStore.adapter = storeHeadAdapter

        return binding.root
    }
    fun getChatHeadsInfo() {
        FirebaseReferences.chatRef.whereEqualTo(getString(R.string.userID), UserInfo.userID).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.exists()) {
                        var chat: Chat = document.toObject<Chat>()
                        var product = Product()
                        FirebaseReferences.productsRef
                            .document(chat.productIDs[chat.productIDs.size - 1]).get()
                            .addOnSuccessListener { productDocument ->
                                if (productDocument != null) {
                                    product = productDocument.toObject<Product>()!!
                                    //Toast.makeText(context, product.category, Toast.LENGTH_LONG).show()
                                }
                                chatHeadList.add(
                                    ChatHead(
                                        product.productID,
                                        product.storeID,
                                        chat.chatID,
                                        product.name,
                                        "",
                                        product.price,
                                        product.images[0],
                                        chat.userID,
                                        product.storeName,
                                        1
                                    )
                                )
                                if (document.equals(result.last())) {
                                    chatHeadAdapter = ChatHeadAdapter(chatHeadList)
                                    binding.rvChat.adapter = chatHeadAdapter
                                }
                            }

                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, getString(R.string.Error), Toast.LENGTH_LONG).show()
            }
    }
    fun getStoreHeadsInfo() {
        FirebaseReferences.chatRef.whereEqualTo(getString(R.string.userID), UserInfo.userID).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.exists()) {
                        var chat: Chat = document.toObject<Chat>()
                        var product = Product()
                        FirebaseReferences.productsRef
                            .document(chat.productIDs[chat.productIDs.size - 1]).get()
                            .addOnSuccessListener { productDocument ->
                                if (productDocument != null) {
                                    product = productDocument.toObject<Product>()!!
                                    //Toast.makeText(context, product.category, Toast.LENGTH_LONG).show()
                                }
                                storeHeadList.add(
                                    ChatHead(
                                        product.productID,
                                        product.storeID,
                                        chat.chatID,
                                        product.name,
                                        "",
                                        product.price,
                                        product.images[0],
                                        chat.userID,
                                        product.storeName,
                                        1
                                    )
                                )
                                if (document.equals(result.last())) {
                                    storeHeadAdapter = StoreHeadAdapter(storeHeadList)
                                    binding.rvStore.adapter = storeHeadAdapter
                                }
                            }

                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, getString(R.string.Error), Toast.LENGTH_LONG).show()
            }
    }


}