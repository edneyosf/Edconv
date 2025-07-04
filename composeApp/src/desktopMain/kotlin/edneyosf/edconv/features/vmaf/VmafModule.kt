package edneyosf.edconv.features.vmaf

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val vmafFeatureModule = module {
    viewModel { VmafViewModel(config = get(), process = get()) }
}