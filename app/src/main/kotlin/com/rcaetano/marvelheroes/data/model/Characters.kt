package com.rcaetano.marvelheroes.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CharacterListResponse(
    @SerializedName("attributionHTML") val attributionHTML: String,
    @SerializedName("attributionText") val attributionText: String,
    @SerializedName("code") val code: Int,
    @SerializedName("copyright") val copyright: String,
    @SerializedName("data") val `data`: Data,
    @SerializedName("etag") val etag: String,
    @SerializedName("status") val status: String
)

data class Data(
    @SerializedName("count") val count: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("results") val results: ArrayList<Character>,
    @SerializedName("total") val total: Int
)

@Parcelize
data class Character(
    @SerializedName("comics") val comics: Comics,
    @SerializedName("description") val description: String,
    @SerializedName("events") val events: Events,
    @SerializedName("id") val id: Int,
    @SerializedName("modified") val modified: String,
    @SerializedName("name") val name: String,
    @SerializedName("resourceURI") val resourceURI: String,
    @SerializedName("series") val series: Series,
    @SerializedName("stories") val stories: Stories,
    @SerializedName("thumbnail") val thumbnail: Thumbnail,
    @SerializedName("urls") val urls: List<Url>
) : Parcelable

@Parcelize
data class Comics(
    @SerializedName("available") val available: Int,
    @SerializedName("collectionURI") val collectionURI: String,
    @SerializedName("items") val items: List<Item>,
    @SerializedName("returned") val returned: Int
) : Parcelable

@Parcelize
data class Events(
    @SerializedName("available") val available: Int,
    @SerializedName("collectionURI") val collectionURI: String,
    @SerializedName("items") val items: List<Item>,
    @SerializedName("returned") val returned: Int
) : Parcelable

@Parcelize
data class Series(
    @SerializedName("available") val available: Int,
    @SerializedName("collectionURI") val collectionURI: String,
    @SerializedName("items") val items: List<Item>,
    @SerializedName("returned") val returned: Int
) : Parcelable

@Parcelize
data class Stories(
    @SerializedName("available") val available: Int,
    @SerializedName("collectionURI") val collectionURI: String,
    @SerializedName("items") val items: List<Item>,
    @SerializedName("returned") val returned: Int
) : Parcelable

@Parcelize
data class Item(
    @SerializedName("name") val name: String,
    @SerializedName("resourceURI") val resourceURI: String,
    @SerializedName("type") val type: String?
) : Parcelable

@Parcelize
data class Thumbnail(
    @SerializedName("extension") val extension: String,
    @SerializedName("path") val path: String
) : Parcelable

@Parcelize
data class Url(
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String
) : Parcelable
