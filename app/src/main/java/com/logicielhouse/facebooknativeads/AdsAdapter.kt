package com.logicielhouse.facebooknativeads

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.*


/**
 * Created by Abdullah on 1/16/2021.
 */
class AdsAdapter(
    private val context: Context,
    private val list: ArrayList<String>,
    private val nativeAdsManager: NativeAdsManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val AD_DISPLAY_FREQUENCY: Int = 4
    private val POST_TYPE: Int = 0
    private val AD_TYPE: Int = 1
    private val adList: ArrayList<NativeAd> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == AD_TYPE) {
            val view: NativeAdLayout =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_ad_unit, parent, false) as NativeAdLayout
            AdViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_data_item, parent, false)
            DataViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        if (viewType == AD_TYPE) {
            var ad: NativeAd?
            if (adList.size > position / AD_DISPLAY_FREQUENCY) {
                ad = adList[position / AD_DISPLAY_FREQUENCY]
            } else {
                try {
                    ad = nativeAdsManager.nextNativeAd()
                } catch (e: Exception) {
                    ad = null
                    Log.e("TAG", "onBindViewHolder: $e")
                }

                if (ad != null) {
                    adList.add(ad)

                    (holder as AdViewHolder).apply {
                        adChoicesContainer.removeAllViews()
                        adTitle.text = ad.advertiserName
                        adBody.text = ad.adBodyText
                        adSocialContext.text = ad.adSocialContext
                        adSponsoredLabel.text = "Sponsored"
                        adBtnCallToAction.text = ad.adCallToAction
                        adBtnCallToAction.visibility = if (ad.hasCallToAction()) {
                            View.VISIBLE
                        } else {
                            View.INVISIBLE
                        }

                        val adOptionsView = AdOptionsView(context, ad, nativeAdLayout)
                        adChoicesContainer.addView(adOptionsView, 0)

                        val clickableViews = ArrayList<View>()
                        clickableViews.add(adIcon)
                        clickableViews.add(adMedia)
                        clickableViews.add(adBtnCallToAction)
                        ad.registerViewForInteraction(nativeAdLayout, adMedia, clickableViews)
                    }
                }
            }
        } else {
            (holder as DataViewHolder).apply {
                tvData.text = list[getOriginalPosition(position)]
            }
        }
    }

    private fun getOriginalPosition(position: Int): Int {
        return if (AD_DISPLAY_FREQUENCY == 0) {
            position
        } else {
            position - position / AD_DISPLAY_FREQUENCY
        }
    }

    override fun getItemCount(): Int {
        var additionalContent = 0
        if (list.size > 0 && AD_DISPLAY_FREQUENCY > 0 && list.size > AD_DISPLAY_FREQUENCY) {
            additionalContent = list.size / AD_DISPLAY_FREQUENCY
        }
        return list.size + additionalContent
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % AD_DISPLAY_FREQUENCY == 0 && position != 0) {
            AD_TYPE
        } else {
            POST_TYPE
        }
    }

    class AdViewHolder(itemView: NativeAdLayout) : RecyclerView.ViewHolder(itemView) {

        val nativeAdLayout: NativeAdLayout = itemView
        val adMedia: MediaView = itemView.findViewById(R.id.native_ad_media)
        val adIcon: MediaView = itemView.findViewById(R.id.native_ad_icon)
        val adTitle: TextView = itemView.findViewById(R.id.native_ad_title)
        val adBody: TextView = itemView.findViewById(R.id.native_ad_body)
        val adSocialContext: TextView = itemView.findViewById(R.id.native_ad_social_context)
        val adSponsoredLabel: TextView =
            itemView.findViewById(R.id.native_ad_sponsored_label)
        val adBtnCallToAction: Button = itemView.findViewById(R.id.native_ad_call_to_action)

        val adChoicesContainer: LinearLayout =
            itemView.findViewById(R.id.ad_choices_container)
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvData: TextView = itemView.findViewById(R.id.tvData)
    }
}