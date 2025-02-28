package xyz.qwexter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import xyz.qwexter.sample.ComplexPipelineRepository
import xyz.qwexter.sample.Db
import java.util.stream.Stream

/*
    Don't relay on tests a lot, they were generated
 */
class ComplexPipelineRepositoryTest {

    companion object {
        @JvmStatic
        fun testCases(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(2, 10),
                Arguments.of(3, 15),
                Arguments.of(4, 20),
                Arguments.of(5, 25),
                Arguments.of(5, 1000),
                Arguments.of(1, 1000),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("All methods should return the same filtered and mapped result")
    fun `all methods should return the same result`(divider: Int, batchSize: Int) {
        val db = Db(divider, batchSize)
        val repository = ComplexPipelineRepository(db)

        // Compute the baseline result (used as the expected output)
        val expected = repository.getItemsListBaseline()

        // Get actual results from all repository methods
        val buildListResult = repository.getItemsListBuildList()
        val collectionOperatorsResult = repository.getItemsListCollectionOperators()
        val sequenceResult = repository.getItemsListSequence()
        val customSequenceResult = repository.getItemsListCustomSequence()

        // Assert that all methods return the same values
        assertEquals(expected, buildListResult, "buildList method failed")
        assertEquals(expected, collectionOperatorsResult, "Collection operators method failed")
        assertEquals(expected, sequenceResult, "Sequence method failed")
        assertEquals(expected, customSequenceResult, "Custom sequence method failed")
    }
}
