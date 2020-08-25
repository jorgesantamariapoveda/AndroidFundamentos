package org.jsantamariap.eh_ho.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.jsantamariap.eh_ho.BuildConfig
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.RequestError
import org.jsantamariap.eh_ho.data.SignInModel
import org.jsantamariap.eh_ho.data.SignUpModel
import org.jsantamariap.eh_ho.data.UserRepo
import org.jsantamariap.eh_ho.isFirsTimeCreated
import org.jsantamariap.eh_ho.topics.TopicsActivity

class LoginActivity : AppCompatActivity(),
    SignInFragment.SignInInteractionListener,
    SignUpFragment.SignUpInteractionListener {

    // MARK: - Properties

    private val signUpFragment: SignUpFragment = SignUpFragment()
    private val signInFragment: SignInFragment = SignInFragment()

    // MARK: - Life cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // el if es para evitar el repintado
        // modo 1
        //if (savedInstanceState == null) {
        // modo 2 (usando extensiones)
        if (isFirsTimeCreated(savedInstanceState)) {
            checkSession()
        }
    }

    // MARK: - Private functions

    private fun checkSession() {
        if (UserRepo.isLogged(this.applicationContext)) {
            showTopics()
        } else {
            onGoToSignIn()
        }
    }

    // el onclick directamente sobre el código xml únicamente se puede hacer
    // si el método está implementado en una actividad, no en un fragmento.
    // Cuando estaba desde el xml si tenia el parámetro view, sino casca
    // ahora como lo hacemos desde el onclick del fragment no hace falta el
    // parámetro view. De hecho antes tampoco se estaba usando aunque era
    // obligatorio ponerlo. Aunque ese view hace referencia al propio button
    private fun showTopics() {
        val intent: Intent = Intent(this, TopicsActivity::class.java)
        startActivity(intent)
        // se añadió en la última sesión cuando implementamos el menú logout
        // esto es para eliminar de la pila, la actividad sino cuando volvemos
        // desde la pantalla de login seguirá estando una vez realizado
        // el logout
        finish()
    }

    // MARK: - Interface SignInInteractionListener

    override fun onGoToSignUp() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, signUpFragment)
            .commit()
    }

    override fun onSignIn(signInModel: SignInModel) {
        enableLoading()

        UserRepo.signIn(
            this.applicationContext,
            signInModel,
            { showTopics() },
            { error ->
                enableLoading(false)
                handleError(error)
            }
        )
    }

    // MARK: - Interface SignUpInteractionListener

    override fun onGoToSignIn() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, signInFragment)
            .commit()
    }

    override fun onSignUp(signUpModel: SignUpModel) {
        enableLoading()

        UserRepo.signUp(
            this.applicationContext,
            signUpModel,
            {
                enableLoading(false)
                // Revisar respuesta del servidor
                Snackbar.make(container, R.string.message_sign_up, Snackbar.LENGTH_LONG).show()
            },
            {
                enableLoading(false)
                handleError(it)
            })
    }

    // MARK: - Private functions

    private fun enableLoading(enable: Boolean = true) {
        if (enable) {
            fragmentContainer.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE
        } else {
            fragmentContainer.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }

    private fun handleError(error: RequestError) {
        if (error.messageResId != null) {
            // container es un id del activity_login.xml
            // esta opción es la ideal, un texto que nosotros creamos y le pasamos el id
            // para que esté traducido
            Snackbar.make(container, error.messageResId, Snackbar.LENGTH_LONG).show()
        } else if (error.message != null) {
            // este caso es cuando el texto nos lo envió el servidor
            Snackbar.make(container, error.message, Snackbar.LENGTH_LONG).show()
        } else {
            // último caso en el cual el servidor no nos devolvió nada, por ejemplo podría
            // ser un timeout
            Snackbar.make(container, R.string.error_default, Snackbar.LENGTH_LONG).show()
        }
    }

}
