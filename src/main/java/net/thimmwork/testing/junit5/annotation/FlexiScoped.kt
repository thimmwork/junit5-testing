package net.thimmwork.testing.junit5.annotation

/**
 * Use this annotation to injext an instance of an initialized FlexiScope implementation.
 * Don't forget to annotate your test or base class with @ExtendWith
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class FlexiScoped
