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

    private val signUpFragment: SignUpFragment = SignUpFragment()
    private val signInFragment: SignInFragment = SignInFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*
        //val button: Button = findViewById(R.id.button_login)
        // 1. Listener a partir definición de interfaz a partir de una clase
        //button.setOnClickListener(Listener())

        // 2. Listener a partir definición de una clase anónima
        val listener: View.OnClickListener = object: View.OnClickListener {
            override fun onClick(view: View?) {
                Toast.makeText(view?.context, "Hola clase anónima", Toast.LENGTH_SHORT).show()
            }
        }
        button.setOnClickListener(listener)

        // 3. Listener mediante funciones anónimas/lambdas
        val listener: (View) -> Unit = {
            Toast.makeText(it.context, "Hola función lambda", Toast.LENGTH_SHORT).show()
        }
        button.setOnClickListener(listener)

        // 3. Listener mediante lambda abreviada
        button.setOnClickListener {
            Toast.makeText(it?.context, "Hola lambda breve", Toast.LENGTH_SHORT).show()
        }

        //val inputUsername: EditText = findViewById(R.id.input_username)

        button.setOnClickListener {
            Toast.makeText(
                it?.context,
                "Welcome to Eh-Ho ${inputUsername.text}",
                Toast.LENGTH_SHORT
            ).show()
        }

        button.setOnClickListener {
            val intent: Intent = Intent(this, TopicsActivity::class.java)
            startActivity(intent)
        }
         */

        // el if es para evitar el repintado
        // modo 1
        //if (savedInstanceState == null) {
        // modo 2 (usando extensiones)
        if (isFirsTimeCreated(savedInstanceState)) {
            checkSession()
        }
    }

    private fun checkSession() {
        if (UserRepo.isLogged(this.applicationContext)) {
            showTopics()
        } else {
            onGoToSignIn()
        }
    }
/*
    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
*/

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

    private fun enableLoading(enable: Boolean = true) {
        if (enable) {
            fragmentContainer.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE
        } else {
            fragmentContainer.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }

    // modo arcaico1: handler->thread explícitos
    /*
    private fun simulateLoading(signInModel: SignInModel) {
        val runnable = Runnable {
            Thread.sleep(3000)
            viewLoading.post {
                showTopics()
                // a las preferencias siempre el contexto de la app y no de la
                // actividad
                UserRepo.signIn(this.applicationContext, signInModel)
            }
        }
        Thread(runnable).start()
    }
    */

    // modo arcaico2: sync task (mecanismo nativo android)
    /*
    private fun simulateLoading() {
        //argumentos tarea segundo plano
        //argumentos progress
        //argumentos finalizacion tares
        // se crea una clase anonima

        val task = object : AsyncTask<Long, Void, Boolean>() {
            // vararg en listado de argumentos es decir un array
            override fun doInBackground(vararg time: Long?): Boolean {
                Thread.sleep(time[0] ?: 3000)
                return true
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                showTopics()
            }
        }
        task.execute(5000)

        // uso con tipos Any como parametros tarea segundo plano
        val task = object : AsyncTask<Any, Void, Boolean>() {
            // vararg en listado de argumentos es decir un array
            override fun doInBackground(vararg time: Any?): Boolean {
                val second = time[1] as String?
                Thread.sleep(time[0] as Long ?: 3000)
                return true
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                showTopics()
            }
        }
        task.execute(5000, "hola mundo")
    }
    */
}

// 1. Definición de interfaz a partir de una clase
/*
class Listener: View.OnClickListener {
    override fun onClick(view: View?) {
        Toast.makeText(view?.context, "Hola interfaz", Toast.LENGTH_SHORT).show()
    }
}
*/