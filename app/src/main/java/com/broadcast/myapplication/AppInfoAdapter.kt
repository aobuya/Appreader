package com.broadcast.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppInfoAdapter(private val appInfoList: List<AppInfo>) :
    RecyclerView.Adapter<AppInfoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        val packageNameTextView: TextView = itemView.findViewById(R.id.packageNameTextView)
        val versionNameTextView: TextView = itemView.findViewById(R.id.versionNameTextView)

        fun bind(appInfo: AppInfo) {
            appNameTextView.text = appInfo.appName
            packageNameTextView.text = appInfo.packageName
            versionNameTextView.text = appInfo.versionName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.app_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(appInfoList[position])
    }

    override fun getItemCount(): Int {
        return appInfoList.size
    }
}
