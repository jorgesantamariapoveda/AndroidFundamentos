package org.jsantamariap.eh_ho.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.jsantamariap.eh_ho.R
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
            // creación del fragment
            // punto 3, crear instancia del fragment
            // ya no haría falta puesto que los creo al principio
            //val signInFragment = SignInFragment()
            // punto 4, añadir instancia al UI con ayuda de FragmentManager
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, signInFragment)
                .commit()
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

    override fun onSignIn() {
        showTopics()
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
    }
}

// 1. Definición de interfaz a partir de una clase
/*
class Listener: View.OnClickListener {
    override fun onClick(view: View?) {
        Toast.makeText(view?.context, "Hola interfaz", Toast.LENGTH_SHORT).show()
    }
}
*/