package cmm.apps.esmorga

import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig

object EsmorgaBuildConfigImpl : EsmorgaBuildConfig {
    override fun getEnvironment(): EsmorgaBuildConfig.Environment = when {
        isProd() -> EsmorgaBuildConfig.Environment.PROD
        isQa() -> EsmorgaBuildConfig.Environment.QA
        else -> EsmorgaBuildConfig.Environment.QA
    }

    override fun isQa(): Boolean {
        return (BuildConfig.FLAVOR).equals("qa", ignoreCase = true)
    }

    override fun isProd(): Boolean {
        return (BuildConfig.FLAVOR).equals("prod", ignoreCase = true)
    }
}