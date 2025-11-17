package cmm.apps.esmorga.view.details

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cmm.apps.designsystem.R as DesignSystem


@Composable
fun DetailsHeaderSection(image: String?, title: String, date: String, titleTestTag: String = "") {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(image)
            //.crossfade(true) //Open bug in Coil https://github.com/coil-kt/coil/issues/1688 leads to image not being properly scaled if crossfade is used
            .build(),
        placeholder = painterResource(DesignSystem.drawable.img_event_list_empty),
        error = painterResource(DesignSystem.drawable.img_event_list_empty),
        contentDescription = stringResource(id = R.string.content_description_event_image).format(title),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
    )
    EsmorgaText(
        text = title, style = EsmorgaTextStyle.TITLE, modifier = Modifier
            .padding(top = 32.dp, start = 16.dp, bottom = 16.dp, end = 16.dp)
            .testTag(titleTestTag)
    )

    EsmorgaText(
        text = date,
        style = EsmorgaTextStyle.BODY_1_ACCENT,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun DetailsDescriptionSection(description: String, locationName: String? = null) {
    EsmorgaText(
        text = stringResource(id = R.string.screen_event_details_description),
        style = EsmorgaTextStyle.HEADING_1,
        modifier = Modifier.padding(16.dp)
    )
    EsmorgaText(
        text = description,
        style = EsmorgaTextStyle.BODY_1,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
    )

    locationName?.let {
        EsmorgaText(
            text = stringResource(id = R.string.screen_event_details_location),
            style = EsmorgaTextStyle.HEADING_1,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
        EsmorgaText(
            text = locationName,
            style = EsmorgaTextStyle.BODY_1,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
        )
    }
}