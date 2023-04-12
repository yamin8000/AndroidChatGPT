package io.github.rayangroup.hamyar.ad

import android.app.Activity
import android.view.ViewGroup
import io.github.rayangroup.hamyar.util.log
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object AdHelper {

    suspend fun showTapsellAd(
        activity: Activity,
        adId: String,
        adView: ViewGroup?
    ) = suspendCoroutine<Unit> {
        TapsellPlus.showStandardBannerAd(
            activity,
            adId,
            adView,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                    tapsellPlusAdModel.responseId.log()
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                    tapsellPlusErrorModel.errorMessage.log()
                }
            })
    }

    suspend fun requestTapsellAd(
        activity: Activity
    ) = suspendCancellableCoroutine { cancellableContinuation ->
        TapsellPlus.requestStandardBannerAd(
            activity,
            AdConstants.STANDARD_BANNER_ZONE_ID,
            TapsellPlusBannerType.BANNER_320x50,
            object : AdRequestCallback() {
                override fun response(ad: TapsellPlusAdModel?) {
                    super.response(ad)
                    cancellableContinuation.resume(ad?.responseId ?: "")
                }

                override fun error(error: String?) {
                    super.error(error)
                    error?.log()
                    cancellableContinuation.resume("")
                }
            }
        )
    }
}