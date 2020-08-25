package org.jsantamariap.eh_ho.topics

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
// con el siguiente import se evita el uso de findViewById
import org.jsantamariap.eh_ho.*
import org.jsantamariap.eh_ho.data.Topic
import org.jsantamariap.eh_ho.data.UserRepo
import org.jsantamariap.eh_ho.login.LoginActivity
import org.jsantamariap.eh_ho.isFirsTimeCreated
import org.jsantamariap.eh_ho.posts.EXTRA_TOPIC_ID
import org.jsantamariap.eh_ho.posts.PostsActivity

const val TRANSACTION_CREATE_TOPIC = "create_topic"

class TopicsActivity : AppCompatActivity(),
    TopicsFragment.TopicsInteractionListener,
    CreateTopicFragment.CreateTopicInteractionListener {

    // MARK: - Life cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        if (isFirsTimeCreated(savedInstanceState))
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, TopicsFragment())
                .commit()
    }

    override fun onResume() {
        super.onResume()

        enableLoading()
    }

    // MARK: - Interface TopicsInteractionListener

    override fun onCreateTopic() {
        // crear pila con addToBackStack
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CreateTopicFragment())
            .addToBackStack(TRANSACTION_CREATE_TOPIC)
            .commit()
    }

    override fun onShowPosts(topic: Topic) {
        goToPosts(topic)
    }

    override fun onLogout() {
        // 1.- borrar datos del usuario del shared preferences
        UserRepo.logout(this.applicationContext)
        // 2.- volver a la pantalla de login activity_login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // super importante, con esto TopicsActivity se destruye
        // de la pila de actividades
    }

    override fun onLoadTopics() {
        enableLoading(false)
    }

    // MARK: - Interface CreateTopicInteractionListener

    override fun onTopicCreated() {
        // dentro de la actividad tengo más control de los flujos
        // por ello se hace aquí y no dentro del fragment
        supportFragmentManager.popBackStack()
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

    private fun goToPosts(topic: Topic) {
        val intent = Intent(this, PostsActivity::class.java)
        // pasar datos entre actividades
        // lástima que no se puedan pasar las referencias u objetos sino
        // que solamente datos primitivos
        intent.putExtra(EXTRA_TOPIC_ID, topic.id)
        startActivity(intent)
    }
}