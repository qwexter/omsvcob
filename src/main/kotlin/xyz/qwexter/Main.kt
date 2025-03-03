package xyz.qwexter

import xyz.qwexter.sample.ComplexPipelineRepository
import xyz.qwexter.sample.Db
import xyz.qwexter.sample.SimplePipelineRepository

fun main() {
    val dividers = listOf(1.0, 0.7, 0.5, 0.3, 0.1, 0.0)
    val batchSizes = listOf(100, 1000, 10000, 100000, 1000000)
    val repositories = listOf(
        "Simple Pipeline" to ::SimplePipelineRepository,
        "Complex pipeline" to ::ComplexPipelineRepository
    )
    println("| Implementation | Divider | Batch Size | Baseline | buildList | List | Sequence | SequenceCustom |")
    println("|-|-|-|-|-|-|-|-|")

    for ((repoName, repoFactory) in repositories) {
        for (divider in dividers) {
            for (batchSize in batchSizes) {
                val repository = repoFactory(Db(divider, batchSize))
                val results = mapOf(
                    "baseline" to repository.getItemsListBaseline().size,
                    "buildlist" to repository.getItemsListBuildList().size,
                    "collection operators" to repository.getItemsListCollectionOperators().size,
                    "sequence" to repository.getItemsListSequence().size,
                    "sequenceCustom" to repository.getItemsListCustomSequence().size
                )

                println(
                    "| $repoName | $divider | $batchSize " +
                            "| ${results["baseline"]} " +
                            "| ${results["buildlist"]} " +
                            "| ${results["collection operators"]} " +
                            "| ${results["sequence"]} " +
                            "| ${results["sequenceCustom"]} |"
                )
            }
        }
    }
}
