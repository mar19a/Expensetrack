package com.example.expensetrack.di


class DependencyInjectorImpl private constructor() : DependencyInjector {

    private val cache = mutableMapOf<Class<*>, Any>()

    override fun <T> set(klass: Class<T>, instance: T) {
        cache[klass] = instance as Any
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(klass: Class<T>): T? {
        return if (klass !in cache.keys) {
            null
        } else cache[klass] as T
    }

    companion object {
        private val instance by lazy { DependencyInjectorImpl() }
        operator fun invoke(): DependencyInjector = instance
    }
}