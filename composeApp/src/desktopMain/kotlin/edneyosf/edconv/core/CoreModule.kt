package edneyosf.edconv.core

import edneyosf.edconv.core.common.EdProcess
import org.koin.dsl.module

val coreModule = module {
    single { EdProcess() }
}