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

    companion object {
        @JvmStatic
        fun testCases(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(2, 10),  // Test case 1: divider=2, batchSize=10
                Arguments.of(3, 15),  // Test case 2: divider=3, batchSize=15
                Arguments.of(4, 20),  // Test case 3: divider=4, batchSize=20
                Arguments.of(5, 25)   // Test case 4: divider=5, batchSize=25
            )
        }
    }

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("All methods should return the same filtered and mapped result")
    fun `all methods should return the same result`(divider: Int, batchSize: Int) {
        val db = Db(divider, batchSize)
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
}
