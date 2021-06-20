package com.shoplex.shoplex.view.activities.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.LoginTabFragmentBinding
import com.shoplex.shoplex.model.enumurations.AuthType
import com.shoplex.shoplex.viewmodel.AuthVM
import org.json.JSONException
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginTabFragment : Fragment() {

    private lateinit var binding: LoginTabFragmentBinding
    private lateinit var authVM: AuthVM

    private lateinit var startGoogleLogin: ActivityResultLauncher<Intent>

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startGoogleLogin =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult(ApiException::class.java)
                    if (task.isSuccessful && account != null) {
                        Firebase.auth.fetchSignInMethodsForEmail(account.email!!)
                            .addOnCompleteListener {
                                if (it.isSuccessful && (it.result.signInMethods.isNullOrEmpty() || it.result.signInMethods?.first() == "google.com")) {
                                    authVM.login(AuthType.Google, account.idToken!!)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Email Registered before!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Google sign in failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        authVM = (activity as AuthActivity).authVM
        binding = LoginTabFragmentBinding.inflate(inflater, container, false)
        binding.userData = authVM
        binding.tvForgetPass.setOnClickListener {
            openDialog()
        }

        authVM.isLoginBtnPressed.observe(requireActivity(), {
            if (it) {
                validate()
            }
        })

        binding.btnGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(requireContext(), gso).signInIntent
            startGoogleLogin.launch(signInIntent)
        }

        binding.btnFace.setOnClickListener {
            loginWithFacebook()
        }

        onEditTextChanged()

        return binding.root
    }

    private fun loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, setOf("public_profile", "email"))
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult?) {
                    loginResult?.let {
                        Log.d("facebook", it.accessToken.token)
                        val request =
                            GraphRequest.newMeRequest(loginResult.accessToken) { _, response ->
                                val json = response.jsonObject
                                try {
                                    if (json != null) {
                                        //val data = json.getJSONObject("picture").getJSONObject("data")
                                        //val name = json.getString("name")
                                        val email = json.getString("email")
                                        //val picUrl = data.getString("url")

                                        // authVM.login(AuthType.Facebook, loginResult.accessToken.token)

                                        Firebase.auth.fetchSignInMethodsForEmail(email)
                                            .addOnCompleteListener { signInResponse ->
                                                if (signInResponse.isSuccessful && (signInResponse.result.signInMethods.isNullOrEmpty() || signInResponse.result.signInMethods?.first() == "facebook.com")) {
                                                    // authVM.login(AuthType.Facebook, loginResult.accessToken.token)
                                                } else {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Email Registered before!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                    }
                                } catch (e: JSONException) {
                                }
                            }
                    }
                }

                override fun onCancel() {
                    Toast.makeText(
                        requireActivity(),
                        "Facebook login cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(
                        requireActivity(),
                        "Facebook login failed: ${error.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun validate() {
        when {
            binding.edEmail.length() == 0 -> binding.tiEmail.error = getString(R.string.Required)
            !(isEmailValid(binding.edEmail.text.toString())) -> binding.tiEmail.error =
                getString(R.string.require_email)
            binding.edPassword.length() == 0 -> binding.tiPassword.error =
                getString(R.string.Required)
            binding.edPassword.length() < 8 -> binding.tiPassword.error =
                getString(R.string.min_password_err)

            else -> authVM.isLoginValid.value = true
        }
        authVM.isLoginBtnPressed.value = false
    }

    //Email Validation
    private fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun onEditTextChanged() {
        binding.edEmail.addTextChangedListener {
            binding.tiEmail.error = null
        }
        binding.edPassword.addTextChangedListener {
            binding.tiPassword.error = null
        }
    }

    //openDialog
    private fun openDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.forgotPassword))
        val view: View = layoutInflater.inflate(R.layout.dialog_forget_password, null)
        builder.setView(view)
        val email: EditText = view.findViewById(R.id.edEmailDialog)
        builder.setPositiveButton(getString(R.string.reset)) { _, _ ->
            when {
                email.text.toString().isEmpty() -> email.error = getString(R.string.Required)
                else -> forgetPassword(email.text.toString())
            }
        }

        builder.setNegativeButton(getString(R.string.close_dialog)) { _, _ ->

        }
        builder.show()
    }


    //Forget Password
    private fun forgetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Snackbar.make(binding.root, getString(R.string.email_send), Snackbar.LENGTH_LONG)
                    .show()

            } else {
                Snackbar.make(binding.root, getString(R.string.require_email), Snackbar.LENGTH_LONG)
                    .show()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}