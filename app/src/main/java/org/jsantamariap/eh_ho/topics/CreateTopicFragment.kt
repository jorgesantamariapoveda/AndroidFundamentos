package org.jsantamariap.eh_ho.topics

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_topic.*
import org.jsantamariap.eh_ho.LoadingDialogFragment
import org.jsantamariap.eh_ho.R
import org.jsantamariap.eh_ho.data.CreateTopicModel
import org.jsantamariap.eh_ho.data.RequestError
import org.jsantamariap.eh_ho.data.TopicsRepo
import org.jsantamariap.eh_ho.data.UserRepo
import org.jsantamariap.eh_ho.inflate
import java.lang.IllegalArgumentException

const val TAG_LOADING_DIALOG = "tag_loadign_dialog"

class CreateTopicFragment : Fragment() {

    // MARK: - Properties

    var interactionListener: CreateTopicInteractionListener? = null
    var username: String? = null

    private val loadingDialogFragment: LoadingDialogFragment by lazy {
        val message = getString(R.string.label_creating_topic)
        LoadingDialogFragment.newInstance(message)
    }

    // MARK: - Life cycle

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CreateTopicInteractionListener) {
            interactionListener = context
            username = UserRepo.getUsername(context)
        } else {
            throw IllegalArgumentException("Context doesn't implement ${CreateTopicInteractionListener::class.java.canonicalName}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // provoca que se creen un par de métodos en el ciclo de vida
        // para añadir menus: onCreateOptionsMenu y onOptionsItemSelected
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // ¡OJO! primero se infla y después se llama al super.onCreate...
        inflater.inflate(R.menu.menu_create_topic, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // una forma sería así
        // if (item.itemId == R.id.action_send)
        // y otra es una nueva forma de kotlin que no existe en java
        // que es con when
        when (item.itemId) {
            R.id.action_send -> createTopic()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_create_topic)
    }

    override fun onDetach() {
        super.onDetach()

        interactionListener = null
    }

    override fun onResume() {
        super.onResume()

        username?.let {
            labelAuthor.text = it
        }
    }

    // MARK: - Private functions

    private fun createTopic() {
        if (isFormValid()) {
            postTopic()
        } else {
            showError()
        }
    }

    private fun postTopic() {
        enabledLoadingDialog()

        val model = CreateTopicModel(inputTitle.text.toString(), inputContent.text.toString())

        this.context?.let {
            TopicsRepo.addTopic(
                it.applicationContext,
                model,
                {
                    enabledLoadingDialog(false)
                    interactionListener?.onTopicCreated()
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

    private fun isFormValid() = inputTitle.text.isNotEmpty() &&
            inputContent.text.isNotEmpty()

    private fun showError() {
        if (inputTitle.text.isEmpty()) {
            inputTitle.error = context?.getString(R.string.error_empty)
        }
        if (inputContent.text.isEmpty()) {
            inputContent.error = context?.getString(R.string.error_empty)
        }
    }


    // MARK: - Interface CreateTopicInteractionListener

    interface CreateTopicInteractionListener {
        fun onTopicCreated()
    }
}