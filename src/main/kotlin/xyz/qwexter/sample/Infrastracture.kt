package xyz.qwexter.sample

class Db(
    passPercentage: Double, // 0.0 to 1.0
    val batchSize: Int,
) {
    private val items: MutableList<DbModel> = ArrayList(batchSize)

    init {
        val enabledCount = (batchSize * passPercentage).toInt()
        (0 until batchSize).forEach { index ->
            items.add(
                DbModel(
                    id = index,
                    isEnabled = index < enabledCount
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