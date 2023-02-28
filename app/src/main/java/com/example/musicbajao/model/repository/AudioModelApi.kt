package com.example.musicbajao.model.repo.musicapi.data
import java.io.Serializable

data class AudioModelApi(
    val collaborative: Boolean,
    val description: String,
    val external_urls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val owner: Owner,
    val primary_color: String,
    val `public`: Boolean,
    val snapshot_id: String,
    val tracks: Tracks,
    val type: String,
    val uri: String
):Serializable

data class ExternalUrls(
    val spotify: String
):Serializable


data class AddedBy(
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
):Serializable

data class Album(
    val album_type: String,
    val artists: List<ArtistX>,
    val available_markets: List<String>,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<ImageX>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
):Serializable


data class ArtistX(
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
):Serializable

data class ExternalIds(
    val isrc: String
):Serializable

data class Followers(
    val href: Any,
    val total: Int
):Serializable

data class Image(
    val height: Any,
    val url: String,
    val width: Any
):Serializable

data class ImageX(
    val height: Int,
    val url: String,
    val width: Int
):Serializable

data class Item(
    val added_at: String,
    val added_by: AddedBy,
    val is_local: Boolean,
    val primary_color: Any,
    val track: Track,
    val video_thumbnail: VideoThumbnail
):Serializable

data class Owner(
    val display_name: String,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
):Serializable

data class Track(
    val album: Album,
    val artists: List<ArtistX>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val episode: Boolean,
    val explicit: Boolean,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val is_local: Boolean,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val track: Boolean,
    val track_number: Int,
    val type: String,
    val uri: String
):Serializable

data class Tracks(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: Any,
    val total: Int
):Serializable

data class VideoThumbnail(
    val url: Any
):Serializable