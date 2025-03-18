package xyz.qwexter.sample


class SimplePipelineRepository(private val db: Db) : DataRepository {

    override fun getItemsListCollectionOperators(): List<UiModel> {
        return db.getItems()
            .filter { it.isEnabled }
            .map { UiModel(it.id) }
    }

    override fun getItemsListSequence(): List<UiModel> {
        return db.getItems()
            .asSequence()
            .filter { it.isEnabled }
            .map { UiModel(it.id) }
            .toList()
    }
}