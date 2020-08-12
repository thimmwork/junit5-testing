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
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestInstancePostProcessor

/**
 * This class is designed as a base class for test extensions that need to be initialized/teared down
 * once and are required to be executable in both package test runs and single test classes, but perform operations
 * that are too expensive to execute before/after every test class.
 * Examples are databases, container orchestration or insertion of large datasets.
 *
 * <p>FlexiScope will detect its first run and call the setUp()/tearDown() methods
 * only once in the corresponding scope.
 *
 * <p>Use the @FlexiScoped annotation to inject your FlexiScope implementation via constructor
 */
abstract class FlexiScope : TestInstancePostProcessor, ParameterResolver, BeforeAllCallback, ExtensionContext.Store.CloseableResource {
    lateinit var context: ExtensionContext

    companion object {
        val namespace = ExtensionContext.Namespace.create("net.thimmwork.testing.junit5")
    }

    override fun beforeAll(context: ExtensionContext) {
        if (!isStarted(context)) {
            setUp(context)
            this.context = context
            context.root.getStore(namespace).put(getName(), this)
        }
    }

    fun isStarted(context: ExtensionContext): Boolean {
        return context.root.getStore(namespace).get(getName()) != null
    }

    fun getName(): String = this.javaClass.canonicalName

    abstract fun setUp(context: ExtensionContext)

    abstract fun tearDown(context: ExtensionContext)

    override fun close() {
        tearDown(context)
        context.getStore(namespace).remove(getName())
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.isAnnotated(FlexiScoped::class.java) && parameterContext.parameter.type.isInstance(this)
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? {
        val desiredType = parameterContext.parameter.type
        val injectableExtension = extensionContext.root.getStore(namespace).get(getName())
        if (!desiredType.isInstance(injectableExtension)) {
            throw IllegalArgumentException("unable to inject @FlexiScoped parameter. Required type: $desiredType, found ${injectableExtension.javaClass}")
        }
        return injectableExtension
    }

    override fun postProcessTestInstance(testInstance: Any, context: ExtensionContext) {
        context.getStore(namespace).put(getName(), testInstance)
    }
}
