package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentShopItemBinding
import com.example.shoppinglist.domain.ShopItem

class ShopItemFragment() : Fragment() {

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding is null")

    private lateinit var onEditingFinishListener: OnEditingFinishListener

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    private lateinit var viewModel: ShopItemViewModel

    override fun onAttach(context: Context) {
        Log.d("Fragment lifecycle", "onAttach")
        // context это активити к которой прикрепляется фрагмент
        super.onAttach(context)
        if (context is OnEditingFinishListener)
            onEditingFinishListener = context
        else
            throw RuntimeException("Activity must implement OnEditingFinishListener")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Fragment lifecycle", "onCreate")
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        Log.d("Fragment lifecycle", "onCreateView")

        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Fragment lifecycle", "onViewCreated")

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        addTextChangeListeners()
        selectScreenMode()
        observeViewModel()
    }

    override fun onStart() {
        Log.d("Fragment lifecycle", "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("Fragment lifecycle", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("Fragment lifecycle", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("Fragment lifecycle", "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("Fragment lifecycle", "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("Fragment lifecycle", "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("Fragment lifecycle", "onDetach")
        super.onDetach()
    }

    private fun parseParams() {
        val args = requireArguments()

        if (!args.containsKey(KEY_SCREEN_MODE))
            throw RuntimeException("Param screen mode is absent")

        val mode = args.getString(KEY_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD)
            throw RuntimeException("Unknown screen mode $mode")

        screenMode = mode
        if (screenMode == MODE_EDIT && !args.containsKey(KEY_SHOP_ITEM_ID))
            throw RuntimeException("Param shop item id is absent")
        if (screenMode == MODE_EDIT)
            shopItemId = args.getInt(KEY_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
    }

    private fun selectScreenMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.loadShopItem(shopItemId)

        binding.buttonSave.setOnClickListener() {
            viewModel.editShopItem(
                binding.editTextName.text?.toString(),
                binding.editTextCount.text?.toString()
            )
        }
    }

    private fun launchAddMode() {
        binding.buttonSave.setOnClickListener() {
            viewModel.addShopItem(binding.editTextName.text?.toString(), binding.editTextCount.text?.toString())
        }
    }

    private fun addTextChangeListeners() {
        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.editTextCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishListener.onEditingFinish()
        }
    }

    interface OnEditingFinishListener {
        fun onEditingFinish()
    }

    companion object {
        private const val KEY_SCREEN_MODE = "extra_mode"
        private const val KEY_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_SCREEN_MODE, MODE_EDIT)
                    putInt(KEY_SHOP_ITEM_ID, shopItemId)
                }
            }
        }

    }

}