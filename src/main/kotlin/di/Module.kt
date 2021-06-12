package di

import model.Email
import org.koin.dsl.module
import routes.cats.CatsService
import routes.cats.CatsServiceImp
import routes.sms.OtpService
import routes.sms.OtpServiceImp
import utils.Crypto
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val ktorModule = module {
    single<CatsService> { CatsServiceImp() }
    single {
        Email(
            email = "pizzaslice2021@gmail.com",
            username = "pizzaslice2021@gmail.com",
            password = "YdZb*5?/?\$w6!>hkr+2C9#M7=ub1vPxY"
        )
    }
    single<OtpService> {
        OtpServiceImp(
            apiKey = "4B55724F72304D665351364E6145654C766C50544C566D78594845466C346A34515754617539416A3658553D",
            emailServer = get(),  // get() Will resolve EmailServer
            expiredTime = Duration.minutes(2)
        )
    }
    single { Crypto(hashSalt = "h9UZaWD*bn,c^';\$rzZ_USU6zHTE`F?c", secretKey = "(8(NqRm4_bgqtuN:=WTBuJ9-vS*M;S.(") }
}