package xyz.qwexter.benchmarks

import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Fork
import xyz.qwexter.sample.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@Fork(2)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 25, time = 1, timeUnit = TimeUnit.SECONDS)
open class ComplexPipelineBenchmark {

    @Param("1", "2", "5", "10", "25", "50")
    var divider: Int = 10

    @Param("100", "1000", "10000", "100000", "1000000")
    var batchSize: Int = 10000

    private lateinit var repository: DataRepository

    @Setup
    fun setup() {
        repository = ComplexPipelineRepository(Db(divider, batchSize))
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