package org.jsantamariap.eh_ho.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_posts.*
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.isFirsTimeCreated
import org.jsantamariap.eh_ho.topics.CreateTopicFragment
import org.jsantamariap.eh_ho.topics.TRANSACTION_CREATE_TOPIC

const val EXTRA_TOPIC_ID = "TOPIC_ID"

// Todas las activity tienen la propiedad intent

class PostsActivity : AppCompatActivity(),
    PostsFragment.PostsInteractionListener {

    // MARK: - Life cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        if (isFirsTimeCreated(savedInstanceState)) {
            val topicId: String = intent.getStringExtra(EXTRA_TOPIC_ID) ?: ""
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, PostsFragment(topicId))
                .commit()
        }

        /*
        // ?: significa que si es null, devuelve lo que hay a su derecha
        val topicId: String = intent.getStringExtra(EXTRA_TOPIC_ID) ?: ""
        val topic: Topic? = TopicsRepo.getTopic(topicId)

        TopicsRepo.getPosts(topicId, this.applicationContext)
         */
    }

    override fun onResume() {
        super.onResume()

        enableLoading()
    }

    // MARK: - Interface PostsFragment.PostsInteractionListener

    // todo esto seguramente lo tenga que implementar
    /*
    override fun onCreateTopic() {
        // crear pila con addToBackStack
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CreatePostFragment())
            .addToBackStack(TRANSACTION_CREATE_POST)
            .commit()
    }
     */

    override fun onLoadPosts() {
        enableLoading(false)
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
}