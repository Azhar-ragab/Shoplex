package eg.gov.iti.shoplex.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.Report
import com.shoplex.shoplex.Review
import com.shoplex.shoplex.databinding.DialogAddReportBinding
import com.shoplex.shoplex.databinding.FragmentAccountBinding
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.view.activities.LoginActivity
import com.shoplex.shoplex.view.activities.OrderActivity
import com.shoplex.shoplex.view.activities.ProfileActivity

class AccountFragment : Fragment() {
    lateinit var  binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

           binding = FragmentAccountBinding.inflate(inflater, container, false)

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
        binding.cardReport.setOnClickListener {
            showAddReportDialog()

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
    private fun showAddReportDialog() {
        val dialogbinding = DialogAddReportBinding.inflate(layoutInflater)
        val reportBtnSheetDialog = BottomSheetDialog(dialogbinding.root.context)

        dialogbinding.btnSendReport.setOnClickListener {
            val reportMsg = dialogbinding.edReport.text.toString()
            val report = Report(UserInfo.name,
                reportMsg, Timestamp.now().toDate())
            FirebaseReferences.usersRef.document(UserInfo.userID.toString()).collection("Reports").add(report)
            reportBtnSheetDialog.dismiss()
        }
            reportBtnSheetDialog.setContentView(dialogbinding.root)
            reportBtnSheetDialog.show()

        }
    }
