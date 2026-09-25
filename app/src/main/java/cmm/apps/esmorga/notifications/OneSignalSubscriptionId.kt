package cmm.apps.esmorga.notifications

private const val LOCAL_SUBSCRIPTION_PREFIX = "local-"
private const val PROD_ENVIRONMENT = "prod"
private const val QA_ENVIRONMENT = "qa"
private const val ANDROID_PLATFORM = "android"

internal fun isRegisteredOneSignalSubscriptionId(subscriptionId: String?): Boolean {
    return !subscriptionId.isNullOrEmpty() && !subscriptionId.startsWith(LOCAL_SUBSCRIPTION_PREFIX)
}

internal fun resolveOneSignalEnvironmentTag(flavor: String): String {
    return if (flavor.contains(PROD_ENVIRONMENT, ignoreCase = true)) PROD_ENVIRONMENT else QA_ENVIRONMENT
}

internal fun createOneSignalDeviceTags(flavor: String, versionName: String): Map<String, String> {
    return mapOf(
        "environment" to resolveOneSignalEnvironmentTag(flavor),
        "platform" to ANDROID_PLATFORM,
        "version" to versionName
    )
}


