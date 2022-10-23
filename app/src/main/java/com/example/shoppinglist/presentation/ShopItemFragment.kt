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
import com.example.shoppinglist.domain.ShopItem
import kotlinx.android.synthetic.main.fragment_shop_item.*

class ShopItemFragment() : Fragment() {

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("Fragment lifecycle", "onCreateView")

        val view = inflater.inflate(R.layout.fragment_shop_item, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.d("Fragment lifecycle", "onViewCreated")

        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
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

        viewModel.shopItem.observe(viewLifecycleOwner) {
            edit_text_name.setText(it.name)
            edit_text_count.setText(it.count.toString())
        }

        button_save.setOnClickListener() {
            viewModel.editShopItem(
                edit_text_name.text?.toString(),
                edit_text_count.text?.toString()
            )
        }
    }

    private fun launchAddMode() {
        button_save.setOnClickListener() {
            viewModel.addShopItem(edit_text_name.text?.toString(), edit_text_count.text?.toString())
        }
    }

    private fun addTextChangeListeners() {
        edit_text_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edit_text_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            if (it)
                text_input_layout_count.error = getString(R.string.error_input_count)
            else
                text_input_layout_count.error = null
        }

        viewModel.errorInputName.observe(viewLifecycleOwner) {
            if (it)
                text_input_layout_name.error = getString(R.string.error_input_name)
            else
                text_input_layout_count.error = null
        }

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