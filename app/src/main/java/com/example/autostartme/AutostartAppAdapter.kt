package com.example.autostartme

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.autostartme.databinding.ItemAutostartAppBinding

class AutostartAppAdapter(
    private val packageManager: PackageManager,
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<String, AutostartAppAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAutostartAppBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemAutostartAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(packageName: String) {
            try {
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                binding.appName.text = packageManager.getApplicationLabel(appInfo)
                binding.appIcon.setImageDrawable(packageManager.getApplicationIcon(appInfo))
            } catch (_: PackageManager.NameNotFoundException) {
                binding.appName.text = packageName
                binding.appIcon.setImageResource(android.R.drawable.sym_def_app_icon)
            }
            binding.appPackage.text = packageName
            binding.btnDelete.setOnClickListener { onDeleteClick(packageName) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(old: String, new: String) = old == new
            override fun areContentsTheSame(old: String, new: String) = old == new
        }
    }
}
