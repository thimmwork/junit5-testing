package net.thimmwork.testing.junit5

import net.thimmwork.testing.junit5.annotation.FlexiScoped
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(FlexiScopedSemaphore::class)
class FlexiScopeSuiteTest(@param:FlexiScoped val semaphore: FlexiScopedSemaphore) {
    @Test
    fun setUp_is_called_only_once_in_test_suite() {
        MatcherAssert.assertThat("semaphore was not acquired", semaphore.isDrained, IsEqual.equalTo(true))
    }
}