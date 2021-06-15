package di

import org.koin.dsl.module
import utils.Crypto

val ktorModuleTest = module {
    single { Crypto(hashSalt = "b;raT/\$.ZQ+U8bm6;7]sncEh2;H9Jp97", secretKey = "rTz]~YfW95HhH;BB'txtVgy%aR%&P'a{") }
}