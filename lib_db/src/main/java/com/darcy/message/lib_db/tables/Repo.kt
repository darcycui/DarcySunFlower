package com.darcy.message.lib_db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darcy.message.lib_common.entity.IDataEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity(tableName = "repos")
@Serializable
data class Repo(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    var description: String,

    @ColumnInfo(name = "starCount")
    @SerializedName("stargazers_count")
    var starCount: Int = 100

) : IDataEntity {
    override fun equals(other: Any?): Boolean {
        return if (other is Repo){
            other.id == this.id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}