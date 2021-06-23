package di

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
import utils.Crypto
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
            email = "pizzaslice2022@gmail.com",
            username = "pizzaslice2022@gmail.com",
            password = "=5QTXk4BRwL^W+b>"
        )
    }

    // Validation Service
    single<ValidationService> {
        ValidationServiceImp(
            apiKey = "4B55724F72304D665351364E6145654C766C50544C566D78594845466C346A34515754617539416A3658553D",
            emailServer = get(),  // get() Will resolve EmailServer
            expiredTime = Duration.minutes(2)
        )
    }

    // Crypto
    single { Crypto(hashSalt = "h9UZaWD*bn,c^';\$rzZ_USU6zHTE`F?c", secretKey = "(8(NqRm4_bgqtuN:=WTBuJ9-vS*M;S.(") }
}