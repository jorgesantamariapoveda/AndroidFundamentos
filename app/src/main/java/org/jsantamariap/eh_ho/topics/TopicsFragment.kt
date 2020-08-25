package org.jsantamariap.eh_ho.topics

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_topics.*
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.Topic
import org.jsantamariap.eh_ho.data.TopicsRepo
import org.jsantamariap.eh_ho.inflate
import java.lang.IllegalArgumentException

class TopicsFragment : Fragment() {

    // MARK: - Properties

    private var topicsInteractionListener: TopicsInteractionListener? = null

    // además de ser lazy, también es una constante con lo que nos aseguramos
    // que sea inmutable
    private val topicsAdapter: TopicsAdapter by lazy {
        val adapter = TopicsAdapter {
            // Pasando datos entre actividades
            this.topicsInteractionListener?.onShowPosts(it)
        }
        adapter
    }

    // MARK: - Life cycle

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is TopicsInteractionListener)
            this.topicsInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${TopicsInteractionListener::class.java.canonicalName}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // provoca que se creen un par de métodos en el ciclo de vida
        // para añadir menus: onCreateOptionsMenu y onOptionsItemSelected
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_topics, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> this.topicsInteractionListener?.onLogout()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_topics)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // comentado, antes de hacer petición al servidor. Una vez lo tenemos, el momento
        // apropiado para recuperarlos en cuando ya están todos los elementos de la pantalla
        // cargados, por lo tanto se hará en el onResume

        listTopics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        // esto añade una linea tras cada topic
        listTopics.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        listTopics.adapter = topicsAdapter

        buttonCreateTopic.setOnClickListener {
            this.topicsInteractionListener?.onCreateTopic()
        }
    }

    override fun onResume() {
        super.onResume()

        loadTopics()
    }

    override fun onDetach() {
        super.onDetach()

        this.topicsInteractionListener = null
    }

    // MARK: - Private functions

    private fun loadTopics() {
        context?.let {
            TopicsRepo.getTopics(
                it.applicationContext,
                {
                    topicsAdapter.setTopics(it)
                    this.topicsInteractionListener?.onLoadTopics()
                },
                {
                    // TODO: manejo de errores (forma parte de la práctica)
                }
            )
        }
    }

    // MARK: - Interface TopicsInteractionListener

    interface TopicsInteractionListener {
        fun onCreateTopic()
        fun onShowPosts(topic: Topic)
        fun onLogout()
        fun onLoadTopics()
    }
}