package net.thimmwork.testing.junit5;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.runner.Description;

import java.util.concurrent.Semaphore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class FlexiScopedSemaphore extends FlexiScope {
    private final Semaphore semaphore = new Semaphore(1);

    @Override
    public void setUp(@NotNull ExtensionContext context) {
        System.out.println("draining permits... " + context.getDisplayName());
        int noOfPermits = semaphore.drainPermits();
        assertThat("no permits available. did this method get called multiple times?",
                noOfPermits, equalTo(1));
    }

    @Override
    public void tearDown(@NotNull ExtensionContext context) {
        System.out.println("releasing permits... " + context.getDisplayName());
        semaphore.release();
        assertThat("more than one permit available. did this method get called multiple times?",
                semaphore.availablePermits(), equalTo(1));
    }

    public boolean isDrained() {
        System.out.println("available permits: " + semaphore.availablePermits());
        return semaphore.availablePermits() == 0;
    }
}
