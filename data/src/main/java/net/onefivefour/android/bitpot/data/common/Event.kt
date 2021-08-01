package net.onefivefour.android.bitpot.data.common

/**
 * Used as a wrapper for data that is exposed via a LiveData.
 * This event can be marked as consumed to prevent it from being
 * handled multiple times by the same consumer.
 */
class Event<T>(val content: T) {

    private val consumers = ArrayList<String>()

    /**
     * @return true if an instance of this class has already consumed this event. Otherwise false.
     * The class name is used to determine this usage!
     */
    fun wasConsumedBy(clazz: Any): Boolean {
        return consumers.contains(getClassName(clazz))
    }

    /**
     * Mark this event consumed by the given class.
     * The class name is used to mark the consumption.
     */
    fun consume(clazz: Any) {
        consumers.add(getClassName(clazz))
    }

    private fun getClassName(clazz: Any) = clazz::class.java.simpleName
}