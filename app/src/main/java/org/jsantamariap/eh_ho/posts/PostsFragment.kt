package org.jsantamariap.eh_ho.posts

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_posts.*
import kotlinx.android.synthetic.main.fragment_topics.*
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.Topic
import org.jsantamariap.eh_ho.data.TopicsRepo
import org.jsantamariap.eh_ho.inflate
import org.jsantamariap.eh_ho.topics.TopicsAdapter
import org.jsantamariap.eh_ho.topics.TopicsFragment
import java.lang.IllegalArgumentException

class PostsFragment(private val topicId: String) : Fragment() {

    // MARK: - Properties

    private var postsInteractionListener: PostsInteractionListener? = null

    private val postsAdapter: PostsAdapter by lazy {
        val adapter = PostsAdapter()
        adapter
    }

    // MARK: - Life cycle

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is PostsInteractionListener)
            this.postsInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${PostsInteractionListener::class.java.canonicalName}")
    }

    override fun onDetach() {
        super.onDetach()

        this.postsInteractionListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_posts, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionAddTopic -> this.postsInteractionListener?.onCreatePost()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_posts)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // configuración RecyclerView
        listPosts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        // asignación de los datos por medio del adaptador. Dicho adaptador "adapta o convierte" nuestro modelo
        // de datos al item visual
        listPosts.adapter = postsAdapter
    }

    override fun onResume() {
        super.onResume()

        loadPosts(topicId)
    }

    // MARK: - Private functions

    private fun loadPosts(id: String) {
        context?.let {
            TopicsRepo.getPosts(
                id,
                it.applicationContext,
                { list ->
                    // carga el recyclerView por medio del adapter
                    postsAdapter.setPosts(list)
                    // avisa a la actividad para que haga lo que crea oportuno
                    this.postsInteractionListener?.onLoadPosts()
                },
                {
                    it.volleyError?.printStackTrace()
                }
            )
        }
    }

    // MARK: - Interface PostsInteractionListener

    interface PostsInteractionListener {
        fun onLoadPosts()
        fun onCreatePost()
    }
}