package com.hucet.flickr.vo

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.hucet.flickr.vo.Photo.Companion.PHOTO_TABLE
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = PHOTO_TABLE
)
@Parcelize
data class Photo(
    @ColumnInfo(name = PHOTO_ID)
    @PrimaryKey
    val id: Long,
    val title: String,
    @SerializedName("description")
    @Embedded
    val description: Description,
    @SerializedName("url_o")
    val originImageUrl: String?,
    @SerializedName("url_s")
    val smallImageUrl: String?
) : Parcelable {
    companion object {
        const val PHOTO_TABLE = "photos"
        const val PHOTO_ID = "photo_id"
    }
}

@Parcelize
data class Description(
    @SerializedName("_content")
    val content: String
) : Parcelable

fun Photo.getDescription(): String {
    return if (description.content.isEmpty()) title
    else description.content
}