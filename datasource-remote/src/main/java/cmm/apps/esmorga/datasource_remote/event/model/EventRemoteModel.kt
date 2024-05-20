package cmm.apps.esmorga.datasource_remote.event.model

import com.google.gson.annotations.SerializedName

data class EventListWrapperRemoteModel(
    @SerializedName("totalEvents") val remoteTotalEvents: Int,
    @SerializedName("events") val remoteEventList: List<EventRemoteModel>
)

data class EventRemoteModel(
    @SerializedName("eventName") val remoteName: String,
    @SerializedName("eventDate") val remoteDate: String
)
