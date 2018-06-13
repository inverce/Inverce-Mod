package com.inverce.mod.v2.core.configuration

import android.content.Context
import android.content.SharedPreferences
import com.inverce.mod.core.IM

inline fun <reified T> SharedValue(initial: T, name: String = "Auto"): SharedValue<T> {
    return SharedValue(T::class.java, initial, name)
}

open class SharedImpl<T>(
        val get: (pref: SharedPreferences, name: String, initial: T) -> T,
        val set: (pref: SharedPreferences.Editor, name: String, value: T) -> SharedPreferences.Editor
)

open class SharedValue<T>(clazz: Class<T>, protected val initial: T, name: String) : Value<T>() {
    @Suppress("UNCHECKED_CAST") // TODO; Change to search via extends
    private val impl: SharedImpl<T> by lazy { (
                impls[clazz] ?: impls[Object::class.java] ?: emptyImpl<T>()
                ) as SharedImpl<T>
    }

    val store: SharedPreferences
        get() = IM.context().getSharedPreferences("ds", Context.MODE_PRIVATE)


    init {
        if (clazz == Object::class.java) {
            throw IllegalArgumentException("Target class cannot be Object. Please specify target T class via SharedValue<T>(clazz: Class<T>, initial: T... constructor")
        }
        getter = { impl.get(store, name, initial) }
        setter = { v ->
            store.edit().apply {
                impl.set(this, name, v)
            }.apply()
        }
    }

    companion object {
        private fun <T> emptyImpl(): SharedImpl<T> = SharedImpl(
                { _, _, _ -> throw IllegalStateException("Generic Type T not allowed, to register more types use SharedValue.registerTypeImplementation method") },
                { _, _, _ -> throw IllegalStateException("Generic Type T not allowed, to register more types use SharedValue.registerTypeImplementation method") })

        private val impls = HashMap<Class<*>, SharedImpl<*>>()

        /**
         * Use clazz = Object.class to provide default implementation
         */
        fun <T> registerTypeAdapter(clazz: Class<T>, impl: SharedImpl<T>) {
            impls[clazz] = impl
        }

        inline fun <reified T> registerTypeAdapter(impl: SharedImpl<T>) = registerTypeAdapter(T::class.java, impl)

        init {
            registerTypeAdapter<Boolean>(SharedImpl({ pref, name, initial -> pref.getBoolean(name, initial) }, { pref, name, value -> pref.putBoolean(name, value) }))
            registerTypeAdapter<Int>(SharedImpl({ pref, name, initial -> pref.getInt(name, initial) }, { pref, name, value -> pref.putInt(name, value) }))
            registerTypeAdapter<Long>(SharedImpl({ pref, name, initial -> pref.getLong(name, initial) }, { pref, name, value -> pref.putLong(name, value) }))
            registerTypeAdapter<Float>(SharedImpl({ pref, name, initial -> pref.getFloat(name, initial) }, { pref, name, value -> pref.putFloat(name, value) }))
            registerTypeAdapter<String>(SharedImpl({ pref, name, initial -> pref.getString(name, initial) }, { pref, name, value -> pref.putString(name, value) }))
            registerTypeAdapter<Double>(SharedImpl({ pref, name, initial -> Double.fromBits(pref.getLong(name, initial.toRawBits())) }, { pref, name, value -> pref.putLong(name, value.toRawBits()) }))

//            registerTypeAdapter<Serializable>(SharedImpl({ pref, name, initial -> pref.getInt(name, initial) }, { pref, name, value -> pref.putInt(name, value) }))
//            registerTypeAdapter<Parcelable>(SharedImpl({ pref, name, initial -> pref.getInt(name, initial) }, { pref, name, value -> pref.putInt(name, value) }))

        }

    }
}



