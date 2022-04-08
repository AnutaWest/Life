package com.example.life

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.life.databinding.DialogSettignsBinding

class SettingsDialog : DialogFragment() {
    private lateinit var binding: DialogSettignsBinding
    private val vm: SettingsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = activity?.let {
        binding = DialogSettignsBinding.inflate(layoutInflater)

        vm.height.value?.also { h -> binding.seekbarHeight.progress = h }
        vm.width.value?.also { w -> binding.seekbarWidth.progress = w }
        vm.speed.value?.also { speed -> binding.seekbarSpeed.progress = speed }

        val builder = AlertDialog.Builder(it)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { _, _ -> onOk()}
            .setNegativeButton(R.string.cancel) { _, _ -> }
        builder.create()
    } ?: throw IllegalStateException("Activity cannot be null")


    private fun onOk() {
        vm.speed.value = binding.seekbarSpeed.progress
        vm.width.value = binding.seekbarWidth.progress
        vm.height.value = binding.seekbarHeight.progress
    }
}