package cagidech.chairumpire.domain

sealed interface DataError : RootError {
    enum class Local: DataError {
        UNKNOWN,
        DISK_FULL
    }

    enum class Network: DataError {
        REQUEST_TIMEOUT,
        NO_INTERNET,
        SERVER_ERROR,
        UNKNOWN
    }
}