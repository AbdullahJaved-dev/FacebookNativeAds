package com.logicielhouse.facebooknativeads

import android.app.Application
import com.facebook.ads.AudienceNetworkAds

/**
 * Created by Abdullah on 1/16/2021.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AudienceNetworkAds.initialize(applicationContext)
    }
}