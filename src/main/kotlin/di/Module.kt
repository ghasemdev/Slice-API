package di

import model.Email
import org.koin.dsl.module
import utils.Crypto

val ktorModule = module {
    single {
        Email(
            email = "pizzaslice2021@gmail.com",
            username = "pizzaslice2021@gmail.com",
            password = "YdZb*5?/?\$w6!>hkr+2C9#M7=ub1vPxY"
        )
    }
//    single<OtpService> {
//        OtpServiceImp(
//            apiKey = "4B55724F72304D665351364E6145654C766C50544C566D78594845466C346A34515754617539416A3658553D",
//            emailServer = get(),  // get() Will resolve EmailServer
//            expiredTime = 2 * 60
//        )
//    }
    single { Crypto(hashSalt = "h9UZaWD*bn,c^';\$rzZ_USU6zHTE`F?c", secretKey = "(8(NqRm4_bgqtuN:=WTBuJ9-vS*M;S.(") }
}