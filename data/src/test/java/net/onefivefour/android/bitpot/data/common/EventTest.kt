package net.onefivefour.android.bitpot.data.common

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class EventTest {

    private lateinit var sut: Event<String>

    class Consumer1
    class Consumer2

    private lateinit var consumer1: Consumer1
    private lateinit var consumer2: Consumer2

    @Before
    fun setup() {
        consumer1 = Consumer1()
        consumer2 = Consumer2()

        sut = Event("")
    }


    @Test
    fun consume_noConsumer_wasConsumedReturnsFalse() {
        val resultConsumer1 = sut.wasConsumedBy(consumer1)
        val resultConsumer2 = sut.wasConsumedBy(consumer2)

        assertEquals(false, resultConsumer1)
        assertEquals(false, resultConsumer2)
    }

    @Test
    fun consume_consumer1_wasConsumedReturnsTrueOnce() {
        sut.consume(consumer1)

        val resultConsumer1 = sut.wasConsumedBy(consumer1)
        val resultConsumer2 = sut.wasConsumedBy(consumer2)

        assertEquals(true, resultConsumer1)
        assertEquals(false, resultConsumer2)
    }

    @Test
    fun consume_consumer1And2_wasConsumedReturnsTrueTwice() {
        sut.consume(consumer1)
        sut.consume(consumer2)

        val resultConsumer1 = sut.wasConsumedBy(consumer1)
        val resultConsumer2 = sut.wasConsumedBy(consumer2)

        assertEquals(true, resultConsumer1)
        assertEquals(true, resultConsumer2)
    }


}