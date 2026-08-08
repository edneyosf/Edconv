package edneyosf.edconv.feature.mediainfo

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mediaInfoModule = module {
    viewModel { MediaInfoViewModel(process = get()) }
}