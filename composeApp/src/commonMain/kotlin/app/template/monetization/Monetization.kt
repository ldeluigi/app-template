package app.template.monetization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// How the app could earn money one day, none of it built yet: purchases that unlock features, and ads at chosen
// places. Each screen depends only on the part it uses: one shows or hides a feature, a shop sells, the app shows an
// ad at a natural break. The app is free until then ([Free]). A store's billing (Google Play Billing, StoreKit) or an
// ad network plugs in as a platform's own [Monetization] (Platform.monetization), and the screens stay as they are.

/** What a purchase can unlock. */
enum class Feature {
    /** No ads anywhere. */
    NO_ADS,
}

/** Something for sale in the store, with the features it unlocks. */
data class Product(
    val id: String,
    val title: String,
    /** The price as the store writes it in the player's currency, e.g. `2,99 €`. */
    val price: String,
    val unlocks: Set<Feature>,
)

/** Where the app may show an ad. */
enum class AdPlacement {
    /** At a natural break, never in the middle of what the person is doing. */
    NATURAL_BREAK,
}

/** The features the players have unlocked. */
interface Entitlements {
    /** The unlocked features, updated when a purchase goes through or is restored. */
    val unlocked: StateFlow<Set<Feature>>
}

/** The store: what is for sale, buying it, and getting back what was bought. */
interface Shop {
    /** The products for sale, once the store has listed them. */
    val products: StateFlow<List<Product>>

    /** Buys [product] through the store; returns whether it was bought. */
    suspend fun purchase(product: Product): Boolean

    /** Restores the purchases made on another phone or before a reinstall: both stores ask apps to offer it. */
    suspend fun restore(): Boolean
}

/** Ads, only at the places the app chooses. Asking for consent, where the law wants it, is part of showing. */
fun interface Ads {
    /** Shows an ad at [placement] if one may show there; returns at once otherwise. */
    suspend fun show(placement: AdPlacement)
}

/** Earning money, each part its own interface. */
interface Monetization {
    /** What the players have unlocked. */
    val entitlements: Entitlements

    /** What is for sale. */
    val shop: Shop

    /** Ads. */
    val ads: Ads
}

/** The app with nothing to buy: everything unlocked, nothing for sale, no ads. */
object Free : Monetization, Entitlements, Shop, Ads {
    override val entitlements: Entitlements = this
    override val shop: Shop = this
    override val ads: Ads = this

    override val unlocked: StateFlow<Set<Feature>> = MutableStateFlow(Feature.entries.toSet()).asStateFlow()
    override val products: StateFlow<List<Product>> = MutableStateFlow(emptyList<Product>()).asStateFlow()

    override suspend fun purchase(product: Product): Boolean = false

    override suspend fun restore(): Boolean = true

    override suspend fun show(placement: AdPlacement) = Unit
}

/** Shows [content] once [feature] is unlocked, and [locked] (an offer, say) until then. */
@Composable
fun Unlocked(
    entitlements: Entitlements,
    feature: Feature,
    locked: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val unlocked by entitlements.unlocked.collectAsState()
    if (feature in unlocked) content() else locked()
}
