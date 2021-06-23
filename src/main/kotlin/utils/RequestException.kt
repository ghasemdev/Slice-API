package utils

sealed class RequestException(massage: String) : Exception(massage)

class DuplicateRecordException(massage: String) : RequestException(massage)

class RecordNotExistException(massage: String) : RequestException(massage)