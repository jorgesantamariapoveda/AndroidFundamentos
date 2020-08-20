package org.jsantamariap.eh_ho

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
// import para acceder al layout
import kotlinx.android.synthetic.main.dialog_loading.*

const val ARG_MESSAGE = "message"

class LoadingDialogFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_loading, container, false)
        // en este caso no funciona porque tal vez difiere el tipo de vista, no está
        // muy claro
        // return container?.inflate(R.layout.dialog_loading)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = this.arguments?.getString(ARG_MESSAGE) ?: getString(R.string.label_loading)
        labelMessage.text = message
    }

    companion object {
        // la forma correcta de pasarle unos argumentos a un DialogFragment es
        // a través de un método estático
        fun newInstance(message: String): LoadingDialogFragment {
            val fragment = LoadingDialogFragment()

            val args = Bundle()
            args.putString(ARG_MESSAGE, message)

            fragment.arguments = args

            return fragment
        }
    }

}