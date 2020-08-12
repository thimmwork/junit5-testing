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