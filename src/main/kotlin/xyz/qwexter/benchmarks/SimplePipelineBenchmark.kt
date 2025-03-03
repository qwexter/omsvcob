package xyz.qwexter.benchmarks

import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Fork
import xyz.qwexter.sample.DataRepository
import xyz.qwexter.sample.Db
import xyz.qwexter.sample.SimplePipelineRepository
import xyz.qwexter.sample.UiModel
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@Fork(2)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 25, time = 1, timeUnit = TimeUnit.SECONDS)
open class SimplePipelineBenchmark {

    @Param("1.0", "0.7", "0.5", "0.3", "0.1", "0.0")
    private var percent: Double = 1.0

    @Param("100", "1000", "10000", "100000", "1000000")
    private var batchSize: Int = 0

    private lateinit var repository: DataRepository

    @Setup
    fun setup() {
        repository = SimplePipelineRepository(Db(percent, batchSize))
    }

    @Benchmark
    fun baseline(): List<UiModel> = repository.getItemsListBaseline()

    @Benchmark
    fun buildList(): List<UiModel> = repository.getItemsListBuildList()

    @Benchmark
    fun collectionsOperators(): List<UiModel> = repository.getItemsListCollectionOperators()

    @Benchmark
    fun sequence(): List<UiModel> = repository.getItemsListSequence()

    @Benchmark
    fun sequenceCustom(): List<UiModel> = repository.getItemsListCustomSequence()
}