package net.thimmwork.testing.junit5

import net.thimmwork.testing.junit5.annotation.FlexiScoped
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestInstancePostProcessor


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
