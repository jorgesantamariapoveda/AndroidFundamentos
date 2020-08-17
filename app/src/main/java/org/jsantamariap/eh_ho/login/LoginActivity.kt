package org.jsantamariap.eh_ho.login

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.SignInModel
import org.jsantamariap.eh_ho.UserRepo
import org.jsantamariap.eh_ho.topics.TopicsActivity

class LoginActivity : AppCompatActivity(),
    SignInFragment.SignInInteractionListener,
    SignUpFragment.SignUpInteractionListener {

    val signUpFragment: SignUpFragment = SignUpFragment()
    val signInFragment: SignInFragment = SignInFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //val button: Button = findViewById(R.id.button_login)

        // 1. Listener a partir definición de interfaz a partir de una clase
        //button.setOnClickListener(Listener())

        // 2. Listener a partir definición de una clase anónima
        /*
        val listener: View.OnClickListener = object: View.OnClickListener {
            override fun onClick(view: View?) {
                Toast.makeText(view?.context, "Hola clase anónima", Toast.LENGTH_SHORT).show()
            }
        }
        button.setOnClickListener(listener)
         */

        // 3. Listener mediante funciones anónimas/lambdas
        /*
        val listener: (View) -> Unit = {
            Toast.makeText(it.context, "Hola función lambda", Toast.LENGTH_SHORT).show()
        }
        button.setOnClickListener(listener)
         */

        // 3. Listener mediante lambda abreviada
        /*
        button.setOnClickListener {
            Toast.makeText(it?.context, "Hola lambda breve", Toast.LENGTH_SHORT).show()
        }
         */

        //val inputUsername: EditText = findViewById(R.id.input_username)

        /*
        button.setOnClickListener {
            Toast.makeText(
                it?.context,
                "Welcome to Eh-Ho ${inputUsername.text}",
                Toast.LENGTH_SHORT
            ).show()
        }
         */

        /*
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
            // tras hacer lo del shared preferences reemplazamos el código por
            // la llamada a onGoToSignIn, en lugar de add es replace pero llama
            // al mismo fragment
            onGoToSignIn()
            /*
            // creación del fragment
            // punto 3, crear instancia del fragment
            // ya no haría falta puesto que los creo al principio
            //val signInFragment = SignInFragment()
            // punto 4, añadir instancia al UI con ayuda de FragmentManager
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, signInFragment)
                .commit()
             */
        }
    }

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


    // deja de usarse, antes estaba directamente con onclick en el xml
    // porque es una actividad. Ahora estará en un fragment que no puede
    // usar el onclick del xml y tiene que crearse en su propio código
    //fun showTopics(view: View) {
    // cuando estaba desde el xml si que era con view, sino casca
    // ahora como lo hacemos desde el onclick del fragment no hace falta el
    // parámetro view. De hecho antes tampoco se estaba usando aunque era
    // obligatorio ponerlo
    private fun showTopics() {
        // el view hace referencia al propio button
        val intent: Intent = Intent(this, TopicsActivity::class.java)
        startActivity(intent)
        // se añadió en la última sesión cuando implementamos el menú logout
        // esto es para eliminar de la pila, la actividad sino cuando volvemos
        // desde la pantalla de login seguirá estando una vez realizado
        // el logout
        finish()
    }

    override fun onGoToSignUp() {
        // se está generando un nuevo fragmento por cada click
        /*
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SignUpFragment())
            .commit()
         */
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, signUpFragment)
            .commit()
    }

    override fun onSignIn(signInModel: SignInModel) {
        // Antes de añadir el progress bar era esta la única llamada
        //showTopics()

        enableLoading()
        simulateLoading(signInModel)
    }

    override fun onGoToSignIn() {
        // se está generando un nuevo fragmento por cada click
        /*
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SignInFragment())
            .commit()
         */
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, signInFragment)
            .commit()
    }

    override fun onSignUp() {
        enableLoading()
        //simulateLoading()
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
    private fun simulateLoading(signInModel: SignInModel) {
        val runnable = Runnable {
            Thread.sleep(3000)
            viewLoading.post {
                showTopics()
                // a las preferencias siempre el contexto de la app y no de la
                // actividad
                UserRepo.signIn(this.applicationContext, signInModel.username)
            }
        }
        Thread(runnable).start()
    }

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