package org.jsantamariap.eh_ho.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.inputPassword
import kotlinx.android.synthetic.main.fragment_sign_up.inputUsername
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.SignUpModel
import org.jsantamariap.eh_ho.inflate
import java.lang.IllegalArgumentException

class SignUpFragment: Fragment() {

    // MARK: - Properties

    private var signUpInteractionListener: SignUpInteractionListener? = null

    // MARK: - Life cycle

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SignUpInteractionListener)
            signUpInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${SignUpInteractionListener::class.java.canonicalName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_sign_up)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSignUp.setOnClickListener {
            if (isFormValid()) {
                val model = SignUpModel(
                    inputUsername.text.toString(),
                    inputEmail.text.toString(),
                    inputPassword.text.toString()
                )
                signUpInteractionListener?.onSignUp(model)
            } else {
                showError()
            }
        }

        labelSignIn.setOnClickListener {
            signUpInteractionListener?.onGoToSignIn()
        }
    }

    override fun onDetach() {
        super.onDetach()

        signUpInteractionListener = null
    }

    // MARK: - Private functions

    private fun isFormValid() =
        inputEmail.text.isNotEmpty()
                && inputUsername.text.isNotEmpty()
                && inputPassword.text.isNotEmpty()
                && inputConfirmPassword.text.isNotEmpty()

    private fun showError() {
        if (inputEmail.text.isEmpty()) {
            inputEmail.error = context?.getString(R.string.error_empty)
        }
        if (inputUsername.text.isEmpty()) {
            inputUsername.error = context?.getString(R.string.error_empty)
        }
        if (inputPassword.text.isEmpty()) {
            inputPassword.error = context?.getString(R.string.error_empty)
        }
        if (inputConfirmPassword.text.isEmpty()) {
            inputConfirmPassword.error = context?.getString(R.string.error_empty)
        }
    }

    // MARK: - Interface SignUpInteractionListener

    interface SignUpInteractionListener {
        fun onGoToSignIn()
        fun onSignUp(signUpModel: SignUpModel)
    }
}