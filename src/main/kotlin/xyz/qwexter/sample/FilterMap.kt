package xyz.qwexter.sample

inline fun <T, R> Sequence<T>.filterMap(
    crossinline predicate: (T) -> Boolean,
    crossinline transform: (T) -> R,
): Sequence<R> = object : Sequence<R> {

    override fun iterator(): Iterator<R> = object : Iterator<R> {
        val iterator = this@filterMap.iterator()
        var nextState: Int = -1 // -1 for unknown, 0 for done, 1 for continue
        var nextItem: T? = null

        override fun hasNext(): Boolean {
            if (nextState == -1)
                calcNext()
            return nextState == 1
        }

        override fun next(): R {
            if (nextState == -1)
                calcNext()
            if (nextState == 0 || nextItem == null)
                throw NoSuchElementException()
            val result: T = nextItem!!
            nextItem = null
            nextState = -1
            @Suppress("UNCHECKED_CAST")
            return transform(result)
        }

        private fun calcNext() {
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (predicate(item)) {
                    nextItem = item
                    nextState = 1
                    return
                }
            }
            nextState = 0
        }
    }
}