package xyz.qwexter.benchmarks

import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.CompilerControl
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit


@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class SafeLoopingBenchmark {

    @Param("1", "10", "100", "1000")
    private var size = 0;


    private lateinit var xs: IntArray

    @Setup
    fun setup() {
        xs = IntArray(size)
        (0..<size).forEach { c ->
            xs[c] = c
        }
    }

    @Benchmark
    fun measureWrong1(): Int {
        var acc = 0
        for (x in xs) {
            acc = work(x)
        }
        return acc
    }

    @Benchmark
    fun measureWrong2(): Int {
        var acc = 0
        for (x in xs) {
            acc += work(x)
        }
        return acc
    }

    @Benchmark
    fun measureRight1(bh: Blackhole) {
        for (x in xs) {
            bh.consume(work(x))
        }
    }
    @Benchmark
    fun measureRight2() {
        for (x in xs) {
            sink(work(x))
        }
    }


    companion object {
        @JvmStatic
        val Base: Int = 42;

        @JvmStatic
        fun work(x: Int): Int {
            return Base + x
        }

        @JvmStatic
        @CompilerControl(CompilerControl.Mode.DONT_INLINE)
        fun sink(v: Int) {
            // IT IS VERY IMPORTANT TO MATCH THE SIGNATURE TO AVOID AUTOBOXING.
            // The method intentionally does nothing.
        }
    }

}