/**
 * Copyright 2020 thimmwork
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
