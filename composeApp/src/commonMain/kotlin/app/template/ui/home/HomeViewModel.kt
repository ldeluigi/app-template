package app.template.ui.home

import androidx.lifecycle.ViewModel
import app.template.core.Counter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** The home screen's state: the sample counter from core. A screen's logic lives in its view model, not its UI. */
class HomeViewModel : ViewModel() {
    private val mutableCounter = MutableStateFlow(Counter())

    /** The counter, as the screen shows it. */
    val counter: StateFlow<Counter> = mutableCounter.asStateFlow()

    /** The person tapped "Count". */
    fun increment() = mutableCounter.update(Counter::increment)

    /** The person tapped "Reset". */
    fun reset() = mutableCounter.update(Counter::reset)
}
