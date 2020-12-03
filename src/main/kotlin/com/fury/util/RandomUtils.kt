package com.fury.util

import com.google.common.base.Preconditions.checkArgument
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object RandomUtils {

    /**
     * Returns a pseudo-random `boolean`.
     * @return The pseudo-random `boolean`.
     */
    fun nextBoolean(): Boolean {
        return ThreadLocalRandom.current().nextBoolean()
    }

    /**
     * Returns a pseudo-random `double`.
     * @return The pseudo-random `double`.
     */
    fun nextDouble(): Double {
        return ThreadLocalRandom.current().nextDouble()
    }

    /**
     * Returns a pseudo-random `float` between inclusive `0` and
     * exclusive `range`.
     * @param range The exclusive range.
     * @return The pseudo-random `float`.
     * @throws IllegalArgumentException If the specified range is less than `0`.
     */
    fun floatRandom(range: Float): Float {
        if (range < 0f)
            throw IllegalArgumentException("range <= 0")
        return ThreadLocalRandom.current().nextFloat() * range
    }

    /**
     * Returns a pseudo-random `int` value between inclusive `min` and inclusive `max`.
     * @param min The minimum inclusive number.
     * @param max The maximum inclusive number.
     * @return The pseudo-random `int`.
     * @throws IllegalArgumentException If `max - min + 1` is less than `0`.
     */
    fun inclusive(min: Int, max: Int): Int {
        if (min == max)
            return min
        checkArgument(max >= min, "max < min")
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min
    }

    /**
     * Returns a pseudo-random `int` value between inclusive `min`
     * and inclusive `max`.
     *
     * @param min The minimum inclusive number.
     * @param max The maximum inclusive number.
     * @return The pseudo-random `int`.
     * @throws IllegalArgumentException If `max - min + 1` is less than
     * `0`.
     */
    fun inclusive(min: Double, max: Double): Double {
        if (min == max)
            return min
        checkArgument(max >= min, "max < min")
        return ThreadLocalRandom.current().nextDouble(max - min) + min
    }

    /**
     * Returns a pseudo-random `int` value between inclusive `0` and inclusive `range`.
     * @param range The maximum inclusive number.
     * @return The pseudo-random `int`.
     * @throws IllegalArgumentException If `max - min + 1` is less than `0`.
     */
    @JvmStatic
    fun inclusive(range: Int): Int {
        return inclusive(0, range)
    }

    /**
     * Pseudo-randomly retrieves a element from `array`.
     * @param array The array to retrieve an element from.
     * @return The element retrieved from the array.
     */
    @JvmStatic
    fun <T> random(array: Array<T>): T {
        return array[(ThreadLocalRandom.current().nextDouble() * array.size).toInt()]
    }

    /**
     * Pseudo-randomly retrieves an `int` from this `array`.
     * @param array The array to retrieve an `int` from.
     * @return The `int` retrieved from the array.
     */
    @JvmStatic
    fun random(vararg array: Int): Int {
        return array[(ThreadLocalRandom.current().nextDouble() * array.size).toInt()]
    }

    /**
     * Pseudo-randomly retrieves an `long` from this `array`.
     * @param array The array to retrieve an `long` from.
     * @return The `long` retrieved from the array.
     */
    @JvmStatic
    fun random(vararg array: Long): Long {
        return array[(ThreadLocalRandom.current().nextDouble() * array.size).toInt()]
    }

    /**
     * Pseudo-randomly retrieves an `double` from this `array`.
     * @param array The array to retrieve an `double` from.
     * @return The `double` retrieved from the array.
     */
    @JvmStatic
    fun random(vararg array: Double): Double {
        return array[(ThreadLocalRandom.current().nextDouble() * array.size).toInt()]
    }

    /**
     * Pseudo-randomly retrieves an `short` from this `array`.
     * @param array The array to retrieve an `short` from.
     * @return The `short` retrieved from the array.
     */
    @JvmStatic
    fun random(vararg array: Short): Short {
        return array[(ThreadLocalRandom.current().nextDouble() * array.size).toInt()]
    }

    /**
     * Pseudo-randomly retrieves an `byte` from this `array`.
     * @param array The array to retrieve an `byte` from.
     * @return The `byte` retrieved from the array.
     */
    @JvmStatic
    fun random(vararg array: Byte): Byte {
        return array[(ThreadLocalRandom.current().nextDouble() * array.size).toInt()]
    }

    /**
     * Pseudo-randomly retrieves an `float` from this `array`.
     * @param array The array to retrieve an `float` from.
     * @return The `float` retrieved from the array.
     */
    @JvmStatic
    fun random(vararg array: Float): Float {
        return array[(ThreadLocalRandom.current().nextDouble() * array.size).toInt()]
    }

    /**
     * Pseudo-randomly retrieves an `boolean` from this `array`.
     * @param array The array to retrieve an `boolean` from.
     * @return The `boolean` retrieved from the array.
     */
    @JvmStatic
    fun random(vararg array: Boolean): Boolean {
        return array[(ThreadLocalRandom.current().nextDouble() * array.size).toInt()]
    }

    /**
     * Pseudo-randomly retrieves an `char` from this `array`.
     * @param array The array to retrieve an `char` from.
     * @return The `char` retrieved from the array.
     */
    @JvmStatic
    fun random(vararg array: Char): Char {
        return array[(ThreadLocalRandom.current().nextDouble() * array.size).toInt()]
    }

    /**
     * Pseudo-randomly retrieves a element from `list`.
     * @param list The list to retrieve an element from.
     * @return The element retrieved from the list.
     */
    @JvmStatic
    fun <T> random(list: List<T>): T {
        return list[(ThreadLocalRandom.current().nextDouble() * list.size).toInt()]
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an `T` array.
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    fun <T> shuffle(array: Array<T>): Array<T> {
        for (i in array.size - 1 downTo 1) {
            val index = ThreadLocalRandom.current().nextInt(i + 1)
            val a = array[index]
            array[index] = array[i]
            array[i] = a
        }
        return array
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an `int` array.
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    fun shuffle(array: IntArray): IntArray {
        for (i in array.size - 1 downTo 1) {
            val index = ThreadLocalRandom.current().nextInt(i + 1)
            val a = array[index]
            array[index] = array[i]
            array[i] = a
        }
        return array
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an `long` array.
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    fun shuffle(array: LongArray): LongArray {
        for (i in array.size - 1 downTo 1) {
            val index = ThreadLocalRandom.current().nextInt(i + 1)
            val a = array[index]
            array[index] = array[i]
            array[i] = a
        }
        return array
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an `double` array.
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    fun shuffle(array: DoubleArray): DoubleArray {
        for (i in array.size - 1 downTo 1) {
            val index = ThreadLocalRandom.current().nextInt(i + 1)
            val a = array[index]
            array[index] = array[i]
            array[i] = a
        }
        return array
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an `short` array.
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    fun shuffle(array: ShortArray): ShortArray {
        for (i in array.size - 1 downTo 1) {
            val index = ThreadLocalRandom.current().nextInt(i + 1)
            val a = array[index]
            array[index] = array[i]
            array[i] = a
        }
        return array
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an `byte` array.
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    fun shuffle(array: ByteArray): ByteArray {
        for (i in array.size - 1 downTo 1) {
            val index = ThreadLocalRandom.current().nextInt(i + 1)
            val a = array[index]
            array[index] = array[i]
            array[i] = a
        }
        return array
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an `float` array.
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    fun shuffle(array: FloatArray): FloatArray {
        for (i in array.size - 1 downTo 1) {
            val index = ThreadLocalRandom.current().nextInt(i + 1)
            val a = array[index]
            array[index] = array[i]
            array[i] = a
        }
        return array
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an `boolean` array.
     * @param array The array that will be shuffled.
     * @return The shuffled array.
     */
    fun shuffle(array: BooleanArray): BooleanArray {
        for (i in array.size - 1 downTo 1) {
            val index = ThreadLocalRandom.current().nextInt(i + 1)
            val a = array[index]
            array[index] = array[i]
            array[i] = a
        }
        return array
    }

    /**
     * An implementation of the Fisher-Yates shuffle algorithm that will shuffle the elements of an `char` array.
     * @param array The array that will be shuffled.
     */
    fun shuffle(array: CharArray): CharArray {
        for (i in array.size - 1 downTo 1) {
            val index = ThreadLocalRandom.current().nextInt(i + 1)
            val a = array[index]
            array[index] = array[i]
            array[i] = a
        }
        return array
    }

    /**
     * Returns a pseudo-random `int` value between inclusive `min`
     * and inclusive `max` excluding the specified numbers within the
     * `excludes` array.
     * @param min the minimum inclusive number.
     * @param max the maximum inclusive number.
     * @return the pseudo-random `int`.
     * @throws IllegalArgumentException if `max - min + 1` is less than `0`.
     */
    fun inclusiveExcludes(min: Int, max: Int, vararg exclude: Int): Int {
        Arrays.sort(exclude)

        var result = inclusive(min, max)
        while (Arrays.binarySearch(exclude, result) >= 0) {
            result = inclusive(min, max)
        }

        return result
    }

    /**
     * Returns a pseudo-random `int` value between inclusive `min`
     * and inclusive `max` excluding the specified numbers within the
     * `excludes` array.
     * @return the pseudo-random `int`.
     * @throws IllegalArgumentException if `max - min + 1` is less than `0`.
     */
    fun <T> randomExclude(array: Array<T>, exclude: T): T {
        var result = random(array)
        while (exclude == result)
            result = random(array)
        return result
    }

    /**
     * Determines if a pseudorandomly generated double rounded to two decimal
     * places is below or equal to `value`.
     * @param value the value to determine this for.
     * @return `true` if successful, `false` otherwise.
     */
    @JvmStatic
    fun success(value: Double): Boolean {
        return ThreadLocalRandom.current().nextDouble() <= value
    }
}