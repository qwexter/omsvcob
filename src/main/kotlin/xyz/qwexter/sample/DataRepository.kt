package xyz.qwexter.sample

interface DataRepository {
    fun getItemsListBaseline(): List<UiModel>

    fun getItemsListBuildList(): List<UiModel>

    fun getItemsListCollectionOperators(): List<UiModel>

    fun getItemsListSequence(): List<UiModel>

    fun getItemsListCustomSequence(): List<UiModel>
}
