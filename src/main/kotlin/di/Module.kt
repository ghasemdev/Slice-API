package di

import kpy.io.property
import kpy.util.crypto.Crypto
import model.EmailServer
import org.koin.dsl.module
import routes.files.pictures.PictureService
import routes.files.pictures.PictureServiceImp
import routes.foods.FoodsService
import routes.foods.FoodsServiceImp
import routes.user.UserService
import routes.user.UserServiceImp
import routes.validation.ValidationService
import routes.validation.ValidationServiceImp
import routes.wishlist.WishlistService
import routes.wishlist.WishlistServiceImp
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val ktorModule = module {
    // Food Service
    single<WishlistService> { WishlistServiceImp() }

    // Food Service
    single<FoodsService> { FoodsServiceImp() }

    // Picture Service
    single<PictureService> { PictureServiceImp() }

    // User Service
    single<UserService> { UserServiceImp() }

    // Email
    single {
        EmailServer(
            property("EMAIL_SERVER_EMAIL")!!,
            property("EMAIL_SERVER_USERNAME")!!,
            property("EMAIL_SERVER_PASSWORD")!!
        )
    }

    // Validation Service
    single<ValidationService> {
        ValidationServiceImp(
            apiKey = property("SMS_APIKEY")!!,
            emailServer = get(),  // get() Will resolve EmailServer
            expiredTime = Duration.minutes(2)
        )
    }

    // Crypto
    single { Crypto(hashSalt = property("CRYPTO_HASH_SALT")!!, secretKey = property("CRYPTO_SECRET_KEY")!!) }
}