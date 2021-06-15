package di

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Usage:
 * Add as companion to test class like
 * class GreetingDatabaseImplTest: KoinTest {
 *     companion object {
 *         @JvmField
 *         @RegisterExtension
 *         var extension: KoinTestExtension = KoinTestExtension.create {
 *             modules(loggingDependencyModule())
 *         }
 *     }
 * }
 *
 * [KoinTestExtension] which will automatically start and stop Koin.
 * @author Taylor Sloan based on KoinTestRule written by Nick Cipollo
 */
class KoinTestExtension constructor(private val appDeclaration: KoinAppDeclaration) : AfterEachCallback,
    BeforeEachCallback {

    private var _koin: Koin? = null
    val koin: Koin
        get() = _koin ?: error("No Koin application found")

    override fun beforeEach(context: ExtensionContext?) {
        _koin = startKoin(appDeclaration = appDeclaration).koin
    }

    override fun afterEach(context: ExtensionContext?) {
        stopKoin()
        _koin = null
    }

    companion object {
        fun create(appDeclaration: KoinAppDeclaration): KoinTestExtension {
            return KoinTestExtension(appDeclaration)
        }
    }
}