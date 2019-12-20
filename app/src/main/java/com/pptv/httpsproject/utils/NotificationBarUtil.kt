package com.pptv.httpsproject.utils

import android.Manifest.permission.EXPAND_STATUS_BAR
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresPermission

class NotificationBarUtil {
    companion object {
        @RequiresPermission(EXPAND_STATUS_BAR)
        fun setNotificationBarVisibility(context: Context, visible: Boolean) {
            invokePanels(
                context, when (visible) {
                    true -> if (Build.VERSION.SDK_INT <= 16) "expand" else "expandNotificationsPanel"
                    else -> if (Build.VERSION.SDK_INT <= 16) "collapse" else "collapsePanels"
                }
            )
        }

        /**
         *  通过反射 展开/合上statusbar
         *
         */
        private fun invokePanels(context: Context, methodName: String) {
            try {
                @SuppressLint("WrongConstant")
                val service: Any = context.getSystemService("statusbar")
                @SuppressLint("PrivateApi")
                val statusBarManager = Class.forName("android.app.StatusBarManager")
                val expand = statusBarManager.getMethod(methodName)
                expand.invoke(service)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}