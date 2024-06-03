package cmm.apps.esmorga.datasource_remote.event.model

import com.google.gson.annotations.SerializedName

data class EventListWrapperRemoteModel(
    @SerializedName("totalEvents") val remoteTotalEvents: Int,
    @SerializedName("events") val remoteEventList: List<EventRemoteModel>
)

data class EventRemoteModel(
    @SerializedName("eventId") val remoteId: String,
    @SerializedName("eventName") val remoteName: String,
    @SerializedName("eventDate") val remoteDate: String,
    @SerializedName("description") val remoteDescription: String,
    @SerializedName("eventType") val remoteType: String,
    @SerializedName("imageUrl") val remoteImageUrl: String? = null,
    @SerializedName("location") val remoteLocation: EventLocationRemoteModel,
    @SerializedName("tags") val remoteTags: List<String>?
)

data class EventLocationRemoteModel(
    @SerializedName("name") val remoteLocationName: String,
    @SerializedName("lat") val remoteLat: Double? = null,
    @SerializedName("long") val remoteLong: Double? = null
)
