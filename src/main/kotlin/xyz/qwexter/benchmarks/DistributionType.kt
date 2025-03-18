package xyz.qwexter.benchmarks

import xyz.qwexter.sample.DbModel
import kotlin.math.roundToInt

/**
 * Distribution types for arranging enabled elements in the dataset
 */
enum class DistributionType {
    /**
     * Elements are ordered with all enabled elements at the beginning
     */
    Ordered,

    /**
     * Elements are completely randomized
     */
    Shuffled,

    /**
     * Elements are distributed evenly throughout the collection
     */
    Distributed,

    /**
     * Elements are arranged in clusters (groups of enabled elements followed by disabled)
     */
    Clustered,

    /**
     * Elements are arranged so that enabled elements appear at the end
     */
    Reversed
}

/**
 * Generates test data with different distribution patterns
 */
object DataGenerator {

    /**
     * Generates a list of items with the specified distribution type
     *
     * @param batchSize Total number of items to generate
     * @param passPercentage Percentage of items that should be enabled (0.0 to 1.0)
     * @param distributionType Type of distribution to use
     * @return List of DbModel instances with the specified distribution
     */
    fun generateItems(
        batchSize: Int,
        passPercentage: Double,
        distributionType: DistributionType
    ): List<DbModel> {
        val enabledCount = (batchSize * passPercentage).roundToInt()

        return when (distributionType) {
            DistributionType.Ordered -> createOrderedDistribution(batchSize, enabledCount)
            DistributionType.Shuffled -> createShuffledDistribution(batchSize, enabledCount)
            DistributionType.Distributed -> createEvenDistribution(batchSize, enabledCount)
            DistributionType.Clustered -> createClusteredDistribution(batchSize, enabledCount, passPercentage)
            DistributionType.Reversed -> createOrderedDistribution(batchSize, enabledCount).reversed()
        }
    }

    private fun createOrderedDistribution(batchSize: Int, enabledCount: Int): List<DbModel> {
        // All enabled elements at the beginning
        return List(batchSize) { index ->
            DbModel(id = index, isEnabled = index < enabledCount)
        }
    }

    private fun createShuffledDistribution(batchSize: Int, enabledCount: Int): List<DbModel> {
        return List(batchSize) { index ->
            DbModel(id = index, isEnabled = index < enabledCount)
        }.shuffled()
    }

    private fun createEvenDistribution(batchSize: Int, enabledCount: Int): List<DbModel> {
        val items = mutableListOf<DbModel>()
        if (enabledCount == 0 || enabledCount == batchSize) {
            // If no elements should be enabled, fill with false
            repeat(batchSize) { index ->
                items.add(DbModel(id = index, isEnabled = enabledCount == batchSize))
            }
        } else {
            // Calculate even spacing between enabled elements
            val interval = batchSize.toDouble() / enabledCount
            var nextEnabledIndex = 0.0

            repeat(batchSize) { index ->
                val isEnabled = index == nextEnabledIndex.roundToInt()
                items.add(DbModel(id = index, isEnabled = isEnabled))

                if (isEnabled) {
                    nextEnabledIndex += interval
                }
            }
        }

        return items
    }

    private fun createClusteredDistribution(batchSize: Int, enabledCount: Int, passPercentage: Double): List<DbModel> {
        val items = mutableListOf<DbModel>()

        // Create clusters of enabled elements followed by disabled elements
        val clusterSize = 10 // Size of each cluster
        val enabledPerCluster = (clusterSize * passPercentage).roundToInt()

        var remainingEnabled = enabledCount
        var i = 0

        while (i < batchSize) {
            val clusterEnabledCount = minOf(enabledPerCluster, remainingEnabled)

            // Add enabled elements for this cluster
            for (j in 0 until clusterEnabledCount) {
                if (i < batchSize) {
                    items.add(DbModel(id = i++, isEnabled = true))
                }
            }

            // Add disabled elements for this cluster
            for (j in 0 until (clusterSize - enabledPerCluster)) {
                if (i < batchSize) {
                    items.add(DbModel(id = i++, isEnabled = false))
                }
            }

            remainingEnabled -= clusterEnabledCount
            if (remainingEnabled <= 0) {
                // Fill the rest with disabled elements
                while (i < batchSize) {
                    items.add(DbModel(id = i++, isEnabled = false))
                }
            }
        }

        return items
    }
}