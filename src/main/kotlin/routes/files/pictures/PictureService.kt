package routes.files.pictures

import java.io.File

interface PictureService {
    suspend fun getProfile(userId: String): File?
    suspend fun updateProfile(userId: String, picture: ByteArray)
}