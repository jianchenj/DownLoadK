package com.pptv.httpsproject.utils

import android.content.Context
import android.util.Log
import java.io.File

class StorageUtils {
    companion object {
        /**
         *  1  Environment.getExternalStorageDirectory() 是sd卡根目录，6.0后需要用户授权，应用卸载不会被删除
         *
         *  2  context.getCacheDir 和 getFilesDir 应用卸载会被删除
         *      内部存储 :/data/data/< package name >/files/ 用户需要用root权限的文件浏览器才能看到，只有本应用可以操作
         *      getCacheDir 临时缓存在低内存时会被清空， getFilesDir 不会
         *
         *  3 context.getExternalCacheDir() 和 getExternalFilesDir  应用卸载会被删除 ，外部存储在手机低内存的时候不会被清空
         *     外部存储 /mnt/sdcard/Android/data/< package name >/files/
         *
         *  用户选择清除缓存会将所有cache的内容清空， 清除数据会将所有file内容清空
         */


        /**
         * 应用内部缓存目录
         */
        fun getCacheDirectory(context: Context): File? {
            val appCacheDir = context.cacheDir
            if (appCacheDir == null) {
                Log.w("StorageUtils", "找不到内部缓存路径");
            }
            return appCacheDir
        }

        fun getExternalCacheDirectory(context: Context): File? {
            val externalCache = context.externalCacheDir
            if (externalCache == null) {
                Log.w("StorageUtils", "找不到外部缓存路径");
            }
            return externalCache
        }

        /**
         * 在外部cache中自定义缓存路径
         */
        fun getExternalCacheCustomDir(context: Context): File {

            val appCacheDir = File(context.externalCacheDir, "update")
            return if (!appCacheDir.exists()) {
                if (appCacheDir.mkdir()) {
                    appCacheDir
                } else {
                    if (context.externalCacheDir == null) {
                        context.cacheDir
                    } else {
                        context.externalCacheDir
                    }
                }
            } else {
                appCacheDir
            }
        }
    }
}