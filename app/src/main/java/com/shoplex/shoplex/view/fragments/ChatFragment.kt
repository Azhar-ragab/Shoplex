package eg.gov.iti.shoplex.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentChatBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.StoreHeadAdapter
import com.shoplex.shoplex.model.pojo.ChatHead


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var chatHeadAdapter: ChatHeadAdapter
    private lateinit var storeHeadAdapter: StoreHeadAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val binding : FragmentChatBinding = FragmentChatBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        //chat Recycler View

        val chatHead = ArrayList<ChatHead>()
        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))
        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))
        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))
        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))
        chatHead.add(ChatHead("Head Phone",12.0, "https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70","Azhar",5))

        chatHeadAdapter = ChatHeadAdapter(chatHead)
        binding.rvChat.addItemDecoration(
                DividerItemDecoration(
                        binding.rvChat.getContext(),
                        DividerItemDecoration.VERTICAL
                )
        )
        binding.rvChat.adapter = chatHeadAdapter


        //Store Recycler View
        val storeHead= ArrayList<ChatHead>()
        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))
        storeHead.add(ChatHead("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))

        storeHeadAdapter= StoreHeadAdapter(storeHead)
        binding.rvStore.adapter = storeHeadAdapter

        return binding.root
    }

}