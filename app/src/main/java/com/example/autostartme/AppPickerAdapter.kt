package com.example.autostartme

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.autostartme.databinding.ItemAppPickerBinding

class AppPickerAdapter(
    private val packageManager: PackageManager,
    private val onAppClick: (String) -> Unit
) : RecyclerView.Adapter<AppPickerAdapter.ViewHolder>() {

    private val allApps = mutableListOf<ResolveInfo>()
    private val filteredApps = mutableListOf<ResolveInfo>()

    fun submitList(apps: List<ResolveInfo>) {
        allApps.clear()
        allApps.addAll(apps)
        filteredApps.clear()
        filteredApps.addAll(apps)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredApps.clear()
        if (query.isBlank()) {
            filteredApps.addAll(allApps)
        } else {
            val lowerQuery = query.lowercase()
            filteredApps.addAll(allApps.filter {
                val label = it.loadLabel(packageManager).toString().lowercase()
                val pkg = it.activityInfo.packageName.lowercase()
                label.contains(lowerQuery) || pkg.contains(lowerQuery)
            })
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppPickerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredApps[position])
    }

    override fun getItemCount() = filteredApps.size

    inner class ViewHolder(
        private val binding: ItemAppPickerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(resolveInfo: ResolveInfo) {
            binding.appName.text = resolveInfo.loadLabel(packageManager)
            binding.appPackage.text = resolveInfo.activityInfo.packageName
            binding.appIcon.setImageDrawable(resolveInfo.loadIcon(packageManager))
            binding.root.setOnClickListener {
                onAppClick(resolveInfo.activityInfo.packageName)
            }
        }
    }
}
