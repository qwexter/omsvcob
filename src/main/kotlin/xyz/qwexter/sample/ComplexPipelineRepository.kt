package xyz.qwexter.sample

class ComplexPipelineRepository(private val db: Db) : DataRepository {

    override fun getItemsListBaseline(): List<UiModel> {
        val result = mutableListOf<UiModel>()
        for (dbModel in db.getItems()) {

            if (!dbModel.isEnabled) continue
            var uiModel = UiModel(dbModel.id)

            if (uiModel.id >= (db.batchSize * 0.8)) continue
            uiModel = UiModel(uiModel.id)

            if (uiModel.id >= (db.batchSize * 0.4)) continue
            uiModel = UiModel(uiModel.id)

            if (uiModel.id >= (db.batchSize * 0.2)) continue

            result.add(UiModel(uiModel.id))
        }

        return result
    }

    override fun getItemsListBuildList(): List<UiModel> {
        return buildList {
            for (dbModel in db.getItems()) {
                if (!dbModel.isEnabled) continue
                var uiModel = UiModel(dbModel.id)

                if (uiModel.id >= (db.batchSize * 0.8)) continue
                uiModel = UiModel(uiModel.id)

                if (uiModel.id >= (db.batchSize * 0.4)) continue
                uiModel = UiModel(uiModel.id)

                if (uiModel.id >= (db.batchSize * 0.2)) continue

                add(UiModel(uiModel.id))
            }
        }
    }

    override fun getItemsListCollectionOperators(): List<UiModel> {
        return db.getItems()
            .filter { it.isEnabled }
            .map { UiModel(it.id) }
            .filter { it.id < (db.batchSize * 0.8) }
            .map { UiModel(it.id) }
            .filter { it.id < (db.batchSize * 0.4) }
            .map { UiModel(it.id) }
            .filter { it.id < (db.batchSize * 0.2) }
            .map { UiModel(it.id) }
    }

    override fun getItemsListSequence(): List<UiModel> {
        return db.getItems()
            .asSequence()
            .filter { it.isEnabled }
            .map { UiModel(it.id) }
            .filter { it.id < (db.batchSize * 0.8) }
            .map { UiModel(it.id) }
            .filter { it.id < (db.batchSize * 0.4) }
            .map { UiModel(it.id) }
            .filter { it.id < (db.batchSize * 0.2) }
            .map { UiModel(it.id) }
            .toList()
    }

    override fun getItemsListCustomSequence(): List<UiModel> {
        return db.getItems()
            .asSequence()
            .filterMap({ it.isEnabled }, { UiModel(it.id) })
            .filterMap({ it.id < (db.batchSize * 0.8) }, { UiModel(it.id) })
            .filterMap({ it.id < (db.batchSize * 0.4) }, { UiModel(it.id) })
            .filterMap({ it.id < (db.batchSize * 0.2) }, { UiModel(it.id) })
            .toList()
    }

}