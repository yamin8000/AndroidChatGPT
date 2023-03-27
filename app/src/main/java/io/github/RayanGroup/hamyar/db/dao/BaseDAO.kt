package io.github.RayanGroup.hamyar.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

abstract class BaseDao<T>(tableName: String) {

    private val baseQuery = "select * from `$tableName`"

    private val baseWhereQuery = "$baseQuery where"

    @Insert
    abstract suspend fun insert(entity: T): Long

    @Insert
    abstract suspend fun insertAll(entities: List<T>): List<Long>

    @Update
    abstract suspend fun update(entity: T): Int

    @Delete
    abstract suspend fun delete(entity: T): Int

    @Delete
    abstract suspend fun deleteAll(entities: List<T>): Int

    @RawQuery
    protected abstract suspend fun getById(query: SupportSQLiteQuery): T?

    suspend fun getById(id: Long) = getById(SimpleSQLiteQuery("$baseQuery where id = $id"))

    @RawQuery
    protected abstract suspend fun getAll(query: SupportSQLiteQuery): List<T>

    suspend fun getAll() = getAll(SimpleSQLiteQuery(baseQuery))

    @RawQuery
    protected abstract suspend fun getAllByIds(query: SupportSQLiteQuery): List<T>

    suspend fun getAllByIds(
        ids: List<Long>
    ): List<T> {
        val params = ids.joinToString("")
        return getAllByIds(SimpleSQLiteQuery("$baseWhereQuery id in ($params)"))
    }

    @RawQuery
    protected abstract suspend fun getByParam(query: SupportSQLiteQuery): List<T>

    suspend fun <P> getByParam(
        param: String, value: P
    ) = getByParam(SimpleSQLiteQuery("$baseWhereQuery $param='$value'"))

    suspend fun getByParams(
        paramPair: Pair<String, *>,
        vararg paramPairs: Pair<String, *>
    ): List<T> {
        val params = listOf(paramPair, *paramPairs)
        val condition = buildString {
            params.forEachIndexed { index, pair ->
                append("${pair.first}='${pair.second}'")
                if (index != params.lastIndex) append(" and ")
            }
        }
        return getByParam(SimpleSQLiteQuery("$baseWhereQuery $condition"))
    }
}