package cagidech.chairumpire.ui.model

import cagidech.chairumpire.R

enum class Services : MenuItem {
    STRINGING() {
        override val stringResourceId: Int
            get() = R.string.service_stringing
    },
    TENNIS_CLINIC() {
        override val stringResourceId: Int
            get() = R.string.service_tennis_clinic
    },
    TENNIS_PRIVATE() {
        override val stringResourceId: Int
            get() = R.string.service_tennis_private
    },
    PICKLEBALL_CLINIC() {
        override val stringResourceId: Int
            get() = R.string.service_pickleball_clinic
    },
    PICKLEBALL_PRIVATE() {
        override val stringResourceId: Int
            get() = R.string.service_pickleball_private
    },
    PICKLEBALL_SOCIAL() {
        override val stringResourceId: Int
            get() = R.string.service_pickleball_social
    },

}