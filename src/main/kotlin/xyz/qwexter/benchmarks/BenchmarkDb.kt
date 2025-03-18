package xyz.qwexter.benchmarks

import xyz.qwexter.sample.Db
import xyz.qwexter.sample.DbModel

class BenchmarkDb(
    val passPercentage: Double, // 0.0 to 1.0
    val batchSize: Int,
    val distributionType: DistributionType = DistributionType.Ordered
) : Db {

    private val items: List<DbModel> = DataGenerator.generateItems(
        batchSize = batchSize,
        passPercentage = passPercentage,
        distributionType = distributionType
    )

    override fun getItems(): List<DbModel> {
        return items
    }
}