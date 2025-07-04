package edneyosf.edconv.features.queue

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val queueFeatureModule = module {
    viewModel { QueueViewModel(process = get()) }
}
