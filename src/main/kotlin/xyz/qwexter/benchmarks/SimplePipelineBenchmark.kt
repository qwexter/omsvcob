package xyz.qwexter.benchmarks

import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Fork
import xyz.qwexter.sample.DataRepository
import xyz.qwexter.sample.SimplePipelineRepository
import xyz.qwexter.sample.UiModel
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@Fork(2)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 25, time = 1, timeUnit = TimeUnit.SECONDS)
open class SimplePipelineBenchmark {

    @Param("1.0", "0.9", "0.75", "0.5", "0.25", "0.1", "0.0")
    private var percent: Double = 1.0

    @Param("100", "1000", "10000", "100000")
    private var batchSize: Int = 0

    @Param("Ordered", "Shuffled", "Distributed")
    private var distribution: String = "Ordered"

    private lateinit var repository: DataRepository

    @Setup
    fun setup() {
        val distributionType = DistributionType.valueOf(distribution)
        repository = SimplePipelineRepository(BenchmarkDb(percent, batchSize, distributionType))
    }

    @Benchmark
    fun collectionsOperators(): List<UiModel> = repository.getItemsListCollectionOperators()

    @Benchmark
    fun sequence(): List<UiModel> = repository.getItemsListSequence()

}