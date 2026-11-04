package app.template.update

import kotlinx.cinterop.BetaInteropApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSLocale
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLRequestReloadIgnoringLocalCacheData
import platform.Foundation.NSURLSession
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.countryCode
import platform.Foundation.create
import platform.Foundation.currentLocale
import platform.Foundation.dataTaskWithRequest
import kotlin.coroutines.resume

/**
 * The newest version of the app with [bundleId] on the App Store of the phone's country, from the store's public
 * lookup (no account, no key), or `null` when it isn't there or the phone is offline.
 */
suspend fun appStoreRelease(bundleId: String): StoreRelease? {
    val country =
        NSLocale.currentLocale.countryCode
            ?.let { "&country=${it.lowercase()}" }
            .orEmpty()
    return download("https://itunes.apple.com/lookup?bundleId=$bundleId$country")?.let(AppStoreListing::parse)
}

/** The text at [link], or `null` when it can't be had. */
@OptIn(BetaInteropApi::class) // NSString.create(data, encoding): Foundation's own way to decode the answer.
private suspend fun download(link: String): String? =
    suspendCancellableCoroutine { continuation ->
        val url = NSURL.URLWithString(link)
        if (url == null) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }
        val request =
            NSURLRequest.requestWithURL(url, NSURLRequestReloadIgnoringLocalCacheData, TIMEOUT_SECONDS)
        val task =
            NSURLSession.sharedSession.dataTaskWithRequest(request) { data, response, _ ->
                val ok = (response as? NSHTTPURLResponse)?.statusCode == HTTP_OK
                val text = data?.takeIf { ok }?.let { NSString.create(data = it, encoding = NSUTF8StringEncoding) }
                continuation.resume(text?.toString())
            }
        continuation.invokeOnCancellation { task.cancel() }
        task.resume()
    }

private const val TIMEOUT_SECONDS = 15.0
private const val HTTP_OK = 200L
