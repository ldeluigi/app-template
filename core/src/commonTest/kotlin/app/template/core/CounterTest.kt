package app.template.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class CounterTest :
    FunSpec({
        test("counts up one at a time") {
            Counter().increment().increment().count shouldBe 2
        }

        test("resets to zero from any count") {
            checkAll(Arb.int(0..1_000)) { count -> Counter(count).reset() shouldBe Counter() }
        }
    })
