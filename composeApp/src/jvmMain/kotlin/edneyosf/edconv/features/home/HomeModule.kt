package edneyosf.edconv.features.home

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeFeatureModule = module {
    viewModel {
        HomeViewModel(
            config = get(),
            remoteConfig = get(),
            process = get()
        )
    }
}