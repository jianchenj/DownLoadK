package com.pptv.httpsproject.utils

import android.content.Context
import android.content.Intent
import java.io.File

class ApkUtils {
    companion object {
        fun installApk() {
            val installIntent =
        }

        private fun getInstallIntent(context: Context, apkFile : File) {
            val intent =  Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            if ()
        }
    }
}