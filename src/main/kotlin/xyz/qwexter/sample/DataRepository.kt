package xyz.qwexter.sample

interface DataRepository {
    fun getItemsListCollectionOperators(): List<UiModel>

    fun getItemsListSequence(): List<UiModel>
}
