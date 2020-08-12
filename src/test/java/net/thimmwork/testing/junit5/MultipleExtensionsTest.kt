package net.thimmwork.testing.junit5

import net.thimmwork.testing.junit5.annotation.FlexiScoped
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(FlexiScopedSemaphore::class, FlexiScopedSemaphore2::class)
class MultipleExtensionsTest(@FlexiScoped val firstSemaphore: FlexiScopedSemaphore,
                             @FlexiScoped val secondSemaphore: FlexiScopedSemaphore2) {
    @Test
    internal fun `unknown parameter will not be injected`() {
        assertNotNull(firstSemaphore)
        assertNotNull(secondSemaphore)
    }
}
