package xyz.qwexter

import xyz.qwexter.benchmarks.BenchmarkDb
import xyz.qwexter.sample.SimplePipelineRepository

fun main() {
    val dividers = listOf(1.0, 0.75, 0.5, 0.25, 0.1, 0.0)
    val batchSizes = listOf(100, 1000, 10000, 100000, 1000000)
    val repositories = listOf(
        "Simple Pipeline" to ::SimplePipelineRepository,
    )
    println("| Implementation | Filter | Batch Size | ColOps | Sequences |")
    println("|-|-|-|-|-|")

    for ((repoName, repoFactory) in repositories) {
        for (divider in dividers) {
            for (batchSize in batchSizes) {
                val db = BenchmarkDb(divider, batchSize)
                val repository = repoFactory(db)
                val results = mapOf(
                    "collection operators" to repository.getItemsListCollectionOperators(),
                    "sequence" to repository.getItemsListSequence(),
                )
                println(
                    "| $repoName | $divider | $batchSize " +
                            "| ${results["collection operators"]!!.size} " +
                            "| ${results["sequence"]!!.size} "
                )
            }
        }
    }
}
