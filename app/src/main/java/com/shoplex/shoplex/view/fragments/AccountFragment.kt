package eg.gov.iti.shoplex.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentAccountBinding
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.view.activities.LoginActivity
import com.shoplex.shoplex.view.activities.OrderActivity
import com.shoplex.shoplex.view.activities.ProfileActivity

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAccountBinding =
            FragmentAccountBinding.inflate(inflater, container, false)

        if (UserInfo.userID == null)
            binding.btnLogout.text = getString(R.string.login)

        binding.cardProfile.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.cardOrder.setOnClickListener {
            val intent = Intent(context, OrderActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            if (UserInfo.userID == null) {
                startActivity(Intent(this.context, LoginActivity::class.java))
            } else {
                showDialog()
            }
        }
        binding.switchNotification.setOnClickListener {
            if(binding.switchNotification.isChecked()){
                Toast.makeText(context,"checked",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"not checked",Toast.LENGTH_SHORT).show()

            }
        }
        return binding.root
    }

    fun showDialog() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(getString(R.string.logOut))
        builder?.setMessage(getString(R.string.logoutMessage))

        builder?.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            Firebase.auth.signOut()
            getActivity()?.finish()
        }

        builder?.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.cancel()
        }

        builder?.show()
    }
}