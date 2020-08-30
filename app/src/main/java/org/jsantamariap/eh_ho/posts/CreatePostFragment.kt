package org.jsantamariap.eh_ho.posts

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_post.*
import org.jsantamariap.eh_ho.LoadingDialogFragment
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.*
import org.jsantamariap.eh_ho.inflate
import java.lang.IllegalArgumentException

const val TAG_LOADING_DIALOG = "tag_loadign_dialog"

class CreatePostFragment(private val topicId: String) : Fragment() {

    // MARK: - Properties
    var interactionListener: CreatePostFragment.CreatePostInteractionListener? = null

    private val loadingDialogFragment: LoadingDialogFragment by lazy {
        val message = getString(R.string.label_creating_post)
        LoadingDialogFragment.newInstance(message)
    }


    // MARK: - Life cycle

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CreatePostFragment.CreatePostInteractionListener) {
            interactionListener = context
        } else {
            throw IllegalArgumentException("Context doesn't implement ${CreatePostFragment.CreatePostInteractionListener::class.java.canonicalName}")
        }
    }

    override fun onDetach() {
        super.onDetach()

        interactionListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // provoca que se creen un par de métodos en el ciclo de vida
        // para añadir menus: onCreateOptionsMenu y onOptionsItemSelected
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // ¡OJO! primero se infla y después se llama al super.onCreate...
        inflater.inflate(R.menu.menu_create_post, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSend -> createPost()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_create_post)
    }

    override fun onResume() {
        super.onResume()

        val topic = TopicsRepo.getTopic(topicId)
        topic?.let {
            labelTopic.text = topic.title
        }
    }

    // MARK: - Private functions

    private fun createPost() {
        if (isFormValid()) {
            addPost()
        } else {
            showError()
        }
    }

    private fun isFormValid() =
        inputContent.text.isNotEmpty()

    private fun addPost() {
        enabledLoadingDialog()

        val model = CreatePostModel(topicId, inputContent.text.toString())

        this.context?.let {
            TopicsRepo.addPost(
                it.applicationContext,
                model,
                {
                    enabledLoadingDialog(false)
                    interactionListener?.onPostCreated()
                },
                {
                    enabledLoadingDialog(false)
                    handleError(it)
                }
            )
        }
    }

    private fun enabledLoadingDialog(enabled: Boolean = true) {
        if (enabled) {
            fragmentManager?.let {
                loadingDialogFragment.show(it, TAG_LOADING_DIALOG)
            }
        } else {
            loadingDialogFragment.dismiss()
        }
    }

    private fun handleError(error: RequestError) {
        val message =
            if (error.messageResId != null) {
                getString(error.messageResId)
            } else if (error.message != null) {
                error.message
            } else {
                getString(R.string.error_default)
            }

        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showError() {
        if (inputContent.text.isEmpty()) {
            inputContent.error = context?.getString(R.string.error_empty)
        }
    }

    // MARK: - Interface CreatePostInteractionListener

    interface CreatePostInteractionListener {
        fun onPostCreated()
    }

}