package com.example.mytodoapp.data.network

import kotlin.reflect.KClass

/**
 * ServiceLocator is a class that provides a centralized registry for storing and retrieving instances
 * of various classes.
 * It allows for easy access to registered instances based on their corresponding class types.
 */
object ServiceLocator {
    private val instances = mutableMapOf<KClass<*>, Any>()

    inline fun <reified T : Any> register(instance: T) = register(T::class, instance)

    fun <T : Any> register(xClass: KClass<out T>, instance: T) {
        instances[xClass] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> get(kClass: KClass<T>): T {
        val instance = instances[kClass]
        if (instance != null) {
            return instance as T
        } else {
            throw IllegalStateException("No instance registered for class ${kClass.simpleName}")
        }
    }
}