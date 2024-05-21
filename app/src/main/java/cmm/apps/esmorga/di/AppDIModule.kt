package cmm.apps.esmorga.di

import cmm.apps.esmorga.data.di.DataDIModule
import cmm.apps.esmorga.datasource_local.di.LocalDIModule
import cmm.apps.esmorga.datasource_remote.di.RemoteDIModule
import cmm.apps.esmorga.domain.di.DomainDIModule
import cmm.apps.esmorga.view.di.ViewDIModule


object AppDIModules {

    val modules = listOf(
        ViewDIModule.module,
        DomainDIModule.module,
        DataDIModule.module,
        RemoteDIModule.module,
        LocalDIModule.module
    )
}