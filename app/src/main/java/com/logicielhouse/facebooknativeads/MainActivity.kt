package com.logicielhouse.facebooknativeads

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.ads.AdError
import com.facebook.ads.NativeAdsManager
import com.logicielhouse.facebooknativeads.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NativeAdsManager.Listener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var nativeAdsManager: NativeAdsManager
    private lateinit var adsAdapter: AdsAdapter
    private var list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        nativeAdsManager = NativeAdsManager(this, getString(R.string.add_placement_id), 4)
        nativeAdsManager.loadAds()
        nativeAdsManager.setListener(this)

        list.add("a")
        list.add("b")
        list.add("c")
        list.add("d")
        list.add("e")
        list.add("f")
        list.add("g")
        list.add("h")
        list.add("i")

        adsAdapter = AdsAdapter(this@MainActivity, list, nativeAdsManager)

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = adsAdapter
        }
    }

    override fun onAdsLoaded() {
        adsAdapter.notifyDataSetChanged()
    }

    override fun onAdError(p0: AdError?) {
        Log.e("TAG", "onAdError: ${p0?.errorMessage}", )
    }
}