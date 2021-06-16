package routes.files.pictures

import db.DatabaseFactory.dbQuery
import db.entity.UserEntity
import java.io.File
import java.util.*

class PictureServiceImp : PictureService {
    companion object {
        private const val PROFILE_PICTURE_PATH = "src/main/resources/images/profile-picture.jpg"
    }

    override suspend fun getProfile(userId: String): File? = dbQuery {
        val user = UserEntity.findById(UUID.fromString(userId)) // find user
        user?.let {
            val byteArray = user.profilePicture
            byteArray?.let {
                // create file from profile picture
                return@dbQuery File(PROFILE_PICTURE_PATH).also { it.writeBytes(byteArray) }
            }
        }
        return@dbQuery null
    }

    override suspend fun updateProfile(userId: String, picture: ByteArray): Unit = dbQuery {
        val user = UserEntity.findById(UUID.fromString(userId)) // find user
        user?.let { it.profilePicture = picture }
    }
}