package dev.uten2c.confkt

import kotlin.reflect.KProperty

class Option<T> {

    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value!!

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}