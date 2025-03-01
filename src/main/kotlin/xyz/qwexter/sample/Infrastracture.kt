package xyz.qwexter.sample

class Db(
    private val divider: Int,
    val batchSize: Int
) {
    private val items: MutableList<DbModel> = ArrayList(batchSize)

    init {
        (0 until batchSize).forEach { index ->
            items.add(
                DbModel(
                    id = index,
                    isEnabled = index % divider == 0
                )
            )
        }
    }

    fun getItems(): List<DbModel> {
        return items
    }
}

data class DbModel(val id: Int, val isEnabled: Boolean)
data class UiModel(val id: Int)