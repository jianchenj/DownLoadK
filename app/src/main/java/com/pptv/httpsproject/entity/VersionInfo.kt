package com.pptv.httpsproject.entity

import java.io.Serializable

class VersionInfo : Serializable {
    private var downloadUrl: String? = null
    private var versionCode: Int? = null
    private var versionName: String? = null
}