package com.pptv.httpsproject.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.pptv.httpsproject.service.DownLoadService

class UpdateDialog {
    companion object {
        fun show(context: Context, content: String?, downloadUrl: String?) {
            if (downloadUrl == null) return
            if (isContextValid(context)) {
                AlertDialog.Builder(context).setTitle("")
                    .setMessage(content)
                    .setPositiveButton("下载") { _, which -> goToDownLoad(context, downloadUrl) }
                    .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            dialog.dismiss()
                        }
                    }).setCancelable(false).show()
            }
        }

        private fun isContextValid(context: Context): Boolean {
            return context is Activity && !context.isFinishing
        }


        /**
         * 启动服务传递下载地址进行下载
         *
         * @param context     activity
         * @param downloadUrl 下载地址
         */
        private fun goToDownLoad(context: Context, downloadUrl: String) {
            val intent = Intent(context.applicationContext, DownLoadService::class.java)
            intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl)
            context.startService(intent)
        }
    }
}