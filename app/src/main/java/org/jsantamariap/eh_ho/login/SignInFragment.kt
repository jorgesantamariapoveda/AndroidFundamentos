package org.jsantamariap.eh_ho.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.SignInModel
import org.jsantamariap.eh_ho.inflate
import java.lang.IllegalArgumentException

class SignInFragment: Fragment() {

    private var signInInteractionListener: SignInInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // este contexto es precisamente la instancia de la activity
        // ¿ojo! esto es una mala práctica -> loginActivity = context as LoginActivity
        // es por ello que se crea una interfaz SignInInteractionListener
        if (context is SignInInteractionListener)
            signInInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${SignInInteractionListener::class.java.canonicalName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_sign_in)
    }

    override fun onDetach() {
        super.onDetach()

        // importante, porque si por lo que sea se destruye la activity
        // y no el fragmento, se quedaría como una referencia en memoria
        // zombie
        signInInteractionListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // al igual que en una activity podemos acceder a los elementos de la vista
        buttonLogin.setOnClickListener {
            val signInModel = SignInModel(
                inputUsername.text.toString(),
                inputPassword.text.toString()
            )
            signInInteractionListener?.onSignIn(signInModel)
        }

        labelCreateAccount.setOnClickListener {
            signInInteractionListener?.onGoToSignUp()
        }
    }

    // Definimos la interface que implementará la activity
    interface SignInInteractionListener {
        fun onGoToSignUp()
        fun onSignIn(signInModel: SignInModel)
    }

}