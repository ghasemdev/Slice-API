package di

import kpy.util.crypto.Crypto
import org.koin.dsl.module

val ktorModuleTest = module {
    single { Crypto(hashSalt = "b;raT/\$.ZQ+U8bm6;7]sncEh2;H9Jp97", secretKey = "rTz]~YfW95HhH;BB'txtVgy%aR%&P'a{") }
}