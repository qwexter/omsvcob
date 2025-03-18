package xyz.qwexter.benchmarks

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class LoopsBenchmark {

    var x = 3
    var y = 4

    @Benchmark
    fun measureRight(): Int = (x + y)

    private fun reps(rep: Int): Int {
        var s = 0
        for (i in 0 until rep) {
            s += (x + y)
        }
        return s
    }

    @Benchmark
    @OperationsPerInvocation(1)
    fun measureWrong1() {
        reps(1)
    }

    @Benchmark
    @OperationsPerInvocation(10)
    fun measureWrong10() {
        reps(10)
    }

    @Benchmark
    @OperationsPerInvocation(100)
    fun measureWrong100() {
        reps(100)
    }
}
