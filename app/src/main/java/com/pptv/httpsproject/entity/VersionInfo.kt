package com.pptv.httpsproject.entity

import java.io.Serializable

class VersionInfo : Serializable {
    var downloadUrl: String? = null
    var versionCode: Int = 0
    var versionName: String? = null
    var updateMessage: String? = null
}