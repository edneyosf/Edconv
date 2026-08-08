package edneyosf.edconv.feature.console

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val consoleModule = module {
    viewModel { ConsoleViewModel(process = get()) }
}