package xyz.qwexter.sample

interface Db {
    fun getItems(): List<DbModel>
}

data class DbModel(val id: Int, val isEnabled: Boolean)
data class UiModel(val id: Int)