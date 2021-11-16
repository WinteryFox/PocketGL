package dev.amyfoxie

abstract class Bindable {
    var isBound = false
        private set

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

    fun run(func: () -> Unit) {
        bind()
        func()
        unbind()
    }
}
