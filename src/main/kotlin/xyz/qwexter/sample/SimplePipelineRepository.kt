package xyz.qwexter.sample


class SimplePipelineRepository(private val db: Db) : DataRepository {

    override fun getItemsListBaseline(): List<UiModel> {
        val result = mutableListOf<UiModel>()
        db.getItems().forEach { if (it.isEnabled) result.add(UiModel(it.id)) }
        return result
    }

    override fun getItemsListBuildList(): List<UiModel> {
        return buildList {
            db.getItems().forEach {
                if (it.isEnabled) add(UiModel(it.id))
            }
        }
    }

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

    override fun getItemsListCustomSequence(): List<UiModel> {
        return db.getItems()
            .asSequence()
            .filterMap({ it.isEnabled }, { UiModel(it.id) })
            .toList()
    }

}