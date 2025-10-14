package edneyosf.edconv.features.metrics

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val metricsFeatureModule = module {
    viewModel { MetricsViewModel(config = get(), process = get()) }
}