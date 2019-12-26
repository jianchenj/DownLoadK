package com.pptv.httpsproject.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import java.io.File


class ApkUtils {
    companion object {
        fun installApk(context: Context, apkFile: File) {
            val installIntent = getInstallIntent(context, apkFile)
            context.startActivity(installIntent)
        }

        private fun getInstallIntent(context: Context, apkFile: File): Intent {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            /**
             *  在安卓7.0（含）之后 尝试传递 file://URI可能会触发FileUriExposedException
             *
             *  解决问题需要用 FileProvider 使用content://uri替代file://uri
             *  第一步 AndroidManifest 声明FIleProvider
             *  第二步 编写XML文件
             *  第三步 使用FileProvider
             */
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                val uri: Uri = FileProvider.getUriForFile(context, "com.httpproject.update.provider", apkFile)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(
                    uri,
                    "application/vnd.android.package-archive"
                )//这个type是MIME类型 ,代表apk类型,参考https://www.jianshu.com/p/ad7c610748fe
            } else {
                val uri = getApkUri(apkFile)
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
            }
            return intent
        }

        /**
         *  7.0之前可以使用file//uri
         */
        private fun getApkUri(apkFile: File): Uri {
            try {
                //如果没有设置 SDCard 写权限，或者没有 SDCard,apk 文件保存在内存中，需要授予权限才能安装
                val command = arrayOf("chmod", "777", apkFile.toString())
                val builder = ProcessBuilder(*command)//ProcessBuilder("myCommand","myArg1","myArg2");
                builder.start()
            } catch (e: Exception) {

            }
            return Uri.fromFile(apkFile)
        }
    }
}