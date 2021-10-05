package com.teakave.pinscreen.ui.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.teakave.pinscreen.R
import com.teakave.pinscreen.databinding.PinFragmentBinding
import kotlinx.coroutines.flow.collect

class PinFragment : Fragment(R.layout.pin_fragment) {

    private var binding: PinFragmentBinding? = null
    private lateinit var viewModel: PinViewModel

    companion object {
        fun newInstance() = PinFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PinFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PinViewModel::class.java)
        listenUiState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumbersListener()
        setupShowPinListener()
        setupRemovePinListener()
    }

    private fun getAllNumberViews() = listOf(
        binding?.textZero,
        binding?.textOne,
        binding?.textTwo,
        binding?.textThree,
        binding?.textFour,
        binding?.textFive,
        binding?.textSix,
        binding?.textSeven,
        binding?.textEight,
        binding?.textNine
    )

    private fun getAllPinViews() = listOf(
        binding?.viewPinFirst,
        binding?.viewPinSecond,
        binding?.viewPinThird,
        binding?.viewPinFourth
    )

    private fun getAllPinTextViews() = listOf(
        binding?.textPinFirst,
        binding?.textPinSecond,
        binding?.textPinThird,
        binding?.textPinFourth
    )

    private fun listenUiState() {
        lifecycleScope.launchWhenCreated {
            viewModel.loginUiState.collect { uiState ->
                when (uiState) {
                    is PinUiState.ShowPin -> {
                        showPinVisibleButton()
                        updatePinViews(uiState.pin, true)
                    }
                    is PinUiState.HidePin -> {
                        showPinHiddenButton()
                        updatePinViews(uiState.pin, false)
                    }
                    is PinUiState.AddedPin -> {
                        hideError()
                        updatePinViews(uiState.pin, uiState.showPin)
                    }
                    is PinUiState.RemovedPin -> resetPinView(uiState.pin, uiState.showPin)
                    is PinUiState.Loading -> {
                        updatePinViews(uiState.pin, uiState.showPin)
                        disableInputs()
                    }
                    is PinUiState.LoginSuccess -> {
                        onSuccess()
                        changePinVisibleButton(uiState.showPin)
                        resetAllPinViews()
                        enableInputs()
                    }
                    is PinUiState.LoginError -> {
                        showError()
                        changePinVisibleButton(uiState.showPin)
                        resetAllPinViews()
                        enableInputs()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun changePinVisibleButton(showPin: Boolean) {
        if (showPin) {
            showPinVisibleButton()
        } else {
            showPinHiddenButton()
        }
    }

    private fun setupNumbersListener() =
        getAllNumberViews().forEach { numberView ->
            numberView?.run {
                setOnClickListener {
                    viewModel.numberClicked(this.text.toString().toIntOrNull())
                }
            }
        }

    private fun setupShowPinListener() =
        binding?.viewShowPin?.setOnClickListener {
            viewModel.showPinClicked()
        }


    private fun setupRemovePinListener() =
        binding?.imageRemovePin?.setOnClickListener {
            viewModel.removePinClicked()
        }

    private fun resetPinView(pin: String, showPin: Boolean) {
        getAllPinTextViews().getOrNull(pin.length)?.text = ""
        getAllPinViews().getOrNull(pin.length)?.setBackgroundResource(R.drawable.pin_empty)
        updatePinViews(pin, showPin)
    }

    private fun showPinVisibleButton() =
        binding?.viewShowPin?.setBackgroundResource(R.drawable.ic_visibility_on)

    private fun showPinHiddenButton() =
        binding?.viewShowPin?.setBackgroundResource(R.drawable.ic_visibility_off)

    private fun resetAllPinViews() {
        getAllPinTextViews().forEach {
            it?.text = ""
        }
        getAllPinViews().forEach {
            it?.setBackgroundResource(R.drawable.pin_empty)
        }
    }

    private fun updatePinViews(pin: String, showPin: Boolean) {
        for (index in pin.indices) {
            pin.getOrNull(index)?.let { pinOnIndex ->
                getAllPinTextViews().getOrNull(index)?.run {
                    text = pinOnIndex.toString()
                    isVisible = showPin
                }
            }
            getAllPinViews().getOrNull(index)?.setBackgroundResource(
                if (showPin) {
                    R.drawable.pin_filled_visible
                } else {
                    R.drawable.pin_filled_hidden
                }
            )
        }
    }

    private fun disableInputs() = getAllNumberViews().forEach {
        it?.isEnabled = false
    }

    private fun enableInputs() = getAllNumberViews().forEach {
        it?.isEnabled = true
    }

    private fun hideError() {
        binding?.textError?.isVisible = false
    }

    private fun showError() {
        binding?.textError?.isVisible = true
    }

    private fun onSuccess() {
        // todo: implement onSuccess() functionality
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }

}