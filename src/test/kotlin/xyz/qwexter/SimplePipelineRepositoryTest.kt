package xyz.qwexter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import xyz.qwexter.sample.Db
import xyz.qwexter.sample.SimplePipelineRepository
import xyz.qwexter.sample.UiModel
import java.util.stream.Stream

/*
    Don't relay on tests a lot, they were generated
 */
class SimplePipelineRepositoryTest {

    class SimplePipelineRepositoryTest {

        companion object {
            @JvmStatic
            fun testCases(): Stream<Arguments> {
                return Stream.of(
                    // Test cases with standard pass rates
                    Arguments.of(1.0, 10),   // 100% pass rate, small batch
                    Arguments.of(0.7, 10),   // 70% pass rate, small batch
                    Arguments.of(0.5, 15),   // 50% pass rate, small batch
                    Arguments.of(0.3, 20),   // 30% pass rate, small batch
                    Arguments.of(0.1, 25),   // 10% pass rate, small batch
                    Arguments.of(0.0, 10),   // 0% pass rate, small batch

                    // Additional small batch sizes
                    Arguments.of(0.7, 20),   // 70% pass rate, small batch
                    Arguments.of(0.3, 10),   // 30% pass rate, very small batch

                    // Medium batch sizes
                    Arguments.of(0.7, 50),   // 70% pass rate, medium batch
                    Arguments.of(0.5, 50),   // 50% pass rate, medium batch
                    Arguments.of(0.3, 50),   // 30% pass rate, medium batch

                    // Larger batch sizes
                    Arguments.of(0.7, 100),  // 70% pass rate, larger batch
                    Arguments.of(0.3, 100),  // 30% pass rate, larger batch

                    // Edge cases
                    Arguments.of(1.0, 10),   // 100% pass rate
                    Arguments.of(0.0, 10),   // 0% pass rate
                    Arguments.of(0.5, 1)     // Single element
                )
            }

            @JvmStatic
            fun detailedTestCases(): Stream<Arguments> {
                // Test with a range of pass percentages for comprehensive coverage
                val passRates = listOf(1.0, 0.7, 0.5, 0.3, 0.1, 0.0)
                val batchSizes = listOf(10, 25, 50, 100)

                return batchSizes.flatMap { size ->
                    passRates.map { rate ->
                        Arguments.of(rate, size)
                    }
                }.stream()
            }
        }

        @ParameterizedTest
        @MethodSource("testCases")
        @DisplayName("All methods should return the same filtered and mapped result")
        fun `all methods should return the same result`(passPercentage: Double, batchSize: Int) {
            val db = Db(passPercentage, batchSize)
            val repository = SimplePipelineRepository(db)

            // Expected result: Filter enabled items and map to UiModel
            val expected = db.getItems()
                .filter { it.isEnabled }
                .map { UiModel(it.id) }

            val baselineResult = repository.getItemsListBaseline()
            val buildListResult = repository.getItemsListBuildList()
            val collectionOperatorsResult = repository.getItemsListCollectionOperators()
            val sequenceResult = repository.getItemsListSequence()
            val customSequenceResult = repository.getItemsListCustomSequence()

            // Assert: Verify correctness and method consistency
            assertEquals(expected, baselineResult, "Baseline method failed")
            assertEquals(expected, buildListResult, "buildList method failed")
            assertEquals(expected, collectionOperatorsResult, "Collection operators method failed")
            assertEquals(expected, sequenceResult, "Sequence method failed")
            assertEquals(expected, customSequenceResult, "Custom sequence method failed")
        }

        @ParameterizedTest
        @MethodSource("detailedTestCases")
        @DisplayName("Verify pass percentages are correctly applied")
        fun `verify pass percentages`(passPercentage: Double, batchSize: Int) {
            val db = Db(passPercentage, batchSize)

            // Count enabled items and verify percentage is correct
            val enabledItems = db.getItems().count { it.isEnabled }
            val actualPercentage = enabledItems.toDouble() / batchSize

            // Allow a small margin of error for integer division effects
            val epsilon = 1.0 / batchSize
            val lowerBound = (passPercentage - epsilon).coerceAtLeast(0.0)
            val upperBound = (passPercentage + epsilon).coerceAtMost(1.0)

            assert(actualPercentage in lowerBound..upperBound) {
                "Expected pass percentage $passPercentage but got $actualPercentage for batch size $batchSize"
            }

            // Verify all implementation methods return the same results
            val repository = SimplePipelineRepository(db)
            val expected = repository.getItemsListBaseline()

            assertEquals(expected, repository.getItemsListBuildList(), "buildList method failed")
            assertEquals(expected, repository.getItemsListCollectionOperators(), "Collection operators method failed")
            assertEquals(expected, repository.getItemsListSequence(), "Sequence method failed")
            assertEquals(expected, repository.getItemsListCustomSequence(), "Custom sequence method failed")
        }

        @ParameterizedTest
        @MethodSource("detailedTestCases")
        @DisplayName("Performance characteristics check for small batch sizes")
        fun `check result sizes match expected pass percentage`(passPercentage: Double, batchSize: Int) {
            val db = Db(passPercentage, batchSize)
            val repository = SimplePipelineRepository(db)

            // Get results from all methods
            val baselineResult = repository.getItemsListBaseline()
            val buildListResult = repository.getItemsListBuildList()
            val collectionOperatorsResult = repository.getItemsListCollectionOperators()
            val sequenceResult = repository.getItemsListSequence()
            val customSequenceResult = repository.getItemsListCustomSequence()

            // Expected result size based on pass percentage
            val expectedSize = (batchSize * passPercentage).toInt()

            // Verify result sizes match expected
            assertEquals(expectedSize, baselineResult.size, "Baseline result size incorrect")
            assertEquals(expectedSize, buildListResult.size, "buildList result size incorrect")
            assertEquals(expectedSize, collectionOperatorsResult.size, "Collection operators result size incorrect")
            assertEquals(expectedSize, sequenceResult.size, "Sequence result size incorrect")
            assertEquals(expectedSize, customSequenceResult.size, "Custom sequence result size incorrect")

            // Verify first and last elements if results are not empty
            if (expectedSize > 0) {
                // Check first element
                assertEquals(baselineResult.first().id, buildListResult.first().id)
                assertEquals(baselineResult.first().id, collectionOperatorsResult.first().id)
                assertEquals(baselineResult.first().id, sequenceResult.first().id)
                assertEquals(baselineResult.first().id, customSequenceResult.first().id)

                // Check last element
                assertEquals(baselineResult.last().id, buildListResult.last().id)
                assertEquals(baselineResult.last().id, collectionOperatorsResult.last().id)
                assertEquals(baselineResult.last().id, sequenceResult.last().id)
                assertEquals(baselineResult.last().id, customSequenceResult.last().id)
            }
        }
    }
}
