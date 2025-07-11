package cmm.apps.esmorga.domain.buildConfig

interface EsmorgaBuildConfig {

    fun getEnvironment(): Environment
    fun isQa(): Boolean
    fun isProd(): Boolean

    enum class Environment {
        PROD,
        QA
    }
}