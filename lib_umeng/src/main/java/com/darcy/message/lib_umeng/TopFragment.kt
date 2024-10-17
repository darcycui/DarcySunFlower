package com.darcy.message.lib_umeng

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class TopFragment : Fragment() {
    companion object {
        const val TAG = "TopFragment"
        const val KEY1 = "KEY1"
        fun getInstance(value1: String): TopFragment {
            return TopFragment().also {
                it.arguments = bundleOf(
                    KEY1 to value1
                )
            }
        }
    }

    private var value1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        value1 = arguments?.getString(KEY1) ?: "Default1"
        Toast.makeText(requireContext(), value1, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lib_umeng_fragment_top, container, false)
    }

    override fun onResume() {
        super.onResume()
        ReportManager.show(requireContext(), "show:$TAG")
    }

    override fun onPause() {
        super.onPause()
        ReportManager.hide(requireContext(), "hide:$TAG")
    }

}