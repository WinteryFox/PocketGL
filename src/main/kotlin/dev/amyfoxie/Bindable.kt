package dev.amyfoxie

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

abstract class Bindable {
    private var isBound = false

    fun bind() {
        if (isBound)
            throw AlreadyBoundException("I'm already bound to something else!")

        isBound = true
        delegateBind()
    }

    fun unbind() {
        if (!isBound)
            throw NotBoundException("I wasn't bound to anything!")

        delegateUnbind()
        isBound = false
    }

    protected abstract fun delegateBind()

    protected abstract fun delegateUnbind()

    @OptIn(ExperimentalContracts::class)
    fun run(func: () -> Unit) {
        contract {
            callsInPlace(func, InvocationKind.EXACTLY_ONCE)
        }
        bind()
        func()
        unbind()
    }
}
