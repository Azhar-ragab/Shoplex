package eg.gov.iti.shoplex.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.BuildConfig
import com.shoplex.shoplex.R
import com.shoplex.shoplex.Report
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
        binding.cardShare.setOnClickListener {
            shareSuccess()

        }
        binding.cardRate.setOnClickListener {
            rateSuccess()
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
                Toast.makeText(context,getString(R.string.checked),Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,getString(R.string.notchecked),Toast.LENGTH_SHORT).show()

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
            val report = Report("Client",
                reportMsg, Timestamp.now().toDate())
            FirebaseReferences.ReportRef.add(report)
            reportBtnSheetDialog.dismiss()
            Snackbar.make(requireView(), "Your Report has sent Thank You",Snackbar.LENGTH_LONG).show()
        }
            reportBtnSheetDialog.setContentView(dialogbinding.root)
            reportBtnSheetDialog.show()

        }

   //share application
    private fun shareSuccess(){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shoplex")
        var shareMessage = "\nLet me recommend you this application\n\n"
        shareMessage = """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                     """.trimIndent()
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    //rate application
    private fun rateSuccess(){
        val uri: Uri = Uri.parse("market://details?id="+ activity?.getPackageName())
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id="+ activity?.getPackageName())))
        }
    }



    }
