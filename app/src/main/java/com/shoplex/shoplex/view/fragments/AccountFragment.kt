package eg.gov.iti.shoplex.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shoplex.shoplex.databinding.FragmentAccountBinding
import com.shoplex.shoplex.view.activities.OrderActivity
import com.shoplex.shoplex.view.activities.ProfileActivity


class AccountFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentAccountBinding = FragmentAccountBinding.inflate(inflater,container,false)
        binding.cardProfile.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
        }

         binding.cardOrder.setOnClickListener {
             val intent = Intent(context, OrderActivity::class.java)
             startActivity(intent)
         }
        return binding.root

    }


}