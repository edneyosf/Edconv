package edneyosf.edconv.features.converter

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val converterHomeModule = module {
    viewModel { ConverterViewModel(config = get(), process = get()) }
}