package cagidech.chairumpire.ui.util

import cagidech.chairumpire.R
import cagidech.chairumpire.ui.model.Services

fun Services.asUiText(): UiText {
    return when (this) {
        Services.STRINGING -> UiText.StringResource(R.string.service_stringing)
        Services.TENNIS_CLINIC -> UiText.StringResource(R.string.service_tennis_clinic)
        Services.TENNIS_PRIVATE -> UiText.StringResource(R.string.service_tennis_private)
        Services.PICKLEBALL_CLINIC -> UiText.StringResource(R.string.service_pickleball_clinic)
        Services.PICKLEBALL_PRIVATE -> UiText.StringResource(R.string.service_pickleball_private)
        Services.PICKLEBALL_SOCIAL -> UiText.StringResource(R.string.service_pickleball_social)
    }
}