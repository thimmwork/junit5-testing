package net.thimmwork.testing.junit5;

import net.thimmwork.testing.junit5.annotation.FlexiScoped;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(FlexiScopedSemaphore.class)
public class FlexiScopeTest {

    private final FlexiScopedSemaphore semaphore;

    public FlexiScopeTest(@FlexiScoped FlexiScopedSemaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Test
    public void setUp_is_called_only_once_in_test_class() {
        assertThat("semaphore was not acquired", semaphore.isDrained(), equalTo(true));
    }

    @Test
    public void setUp_is_called_only_once_in_test_class_with_two_methods() {
        assertThat("semaphore was not acquired", semaphore.isDrained(), equalTo(true));
    }
}
