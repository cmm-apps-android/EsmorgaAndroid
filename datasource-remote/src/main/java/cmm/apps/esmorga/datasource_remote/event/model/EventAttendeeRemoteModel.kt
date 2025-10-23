package cmm.apps.esmorga.datasource_remote.event.model

import com.google.gson.annotations.SerializedName


data class EventAttendeeWrapperRemoteModel(
    @SerializedName("totalUsers") val remoteTotalAttendees: Int,
    @SerializedName("users") val remoteEventAttendeeList: List<String>
)
