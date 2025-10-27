package edneyosf.edconv.core

import edneyosf.edconv.core.config.EdConfig
import edneyosf.edconv.core.config.RemoteConfig
import edneyosf.edconv.core.process.EdProcess
import org.koin.dsl.module

val coreModule = module {
    single { EdConfig() }
    single { RemoteConfig() }
    single { EdProcess() }
}