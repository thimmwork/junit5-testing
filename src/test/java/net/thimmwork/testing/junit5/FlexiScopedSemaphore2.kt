package net.thimmwork.testing.junit5

import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.extension.ExtensionContext
import java.util.concurrent.Semaphore

class FlexiScopedSemaphore2 : FlexiScope() {
    private val semaphore = Semaphore(1)

    override fun setUp(context: ExtensionContext) {
        println("draining permits for Semaphore 2... ${context.displayName}")
        val noOfPermits = semaphore.drainPermits()
        MatcherAssert.assertThat("no permits available. did this method get called multiple times?",
                noOfPermits, IsEqual.equalTo(1))
    }

    override fun tearDown(context: ExtensionContext) {
        println("releasing permits for Semaphore 2... ${context.displayName}")
        semaphore.release()
        MatcherAssert.assertThat("more than one permit available. did this method get called multiple times?",
                semaphore.availablePermits(), IsEqual.equalTo(1))
    }

    fun isDrained(): Boolean {
        println("available permits for Semaphore 2: " + semaphore.availablePermits())
        return semaphore.availablePermits() == 0
    }
}