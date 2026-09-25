package cmm.apps.esmorga.notifications

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.edit
import cmm.apps.esmorga.BuildConfig
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.onesignal.user.subscriptions.IPushSubscriptionObserver
import com.onesignal.user.subscriptions.PushSubscriptionChangedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

object OneSignalManager : Application.ActivityLifecycleCallbacks {

    private const val PREFERENCES_NAME = "onesignal_integration"
    private const val INTEGRATION_DIALOG_SHOWN_KEY = "integration_dialog_shown"
    private const val INTEGRATION_COMPLETE_TITLE = "Your OneSignal SDK integration is complete!"
    private const val INTEGRATION_COMPLETE_MESSAGE = "You can now send Push Notifications & In-App Messages through OneSignal. Tap below to enable push notifications."
    private const val INTEGRATION_COMPLETE_BUTTON = "Got it"

    private val isInitialized = AtomicBoolean(false)
    private val isIntegrationDialogVisible = AtomicBoolean(false)
    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val pushSubscriptionObserver = object : IPushSubscriptionObserver {
        override fun onPushSubscriptionChange(state: PushSubscriptionChangedState) {
            maybeShowIntegrationCompleteDialog(state.current.id)
        }
    }

    @Volatile
    private var currentActivity: WeakReference<Activity>? = null

    @Volatile
    private var shouldShowIntegrationDialog = false

    private lateinit var preferences: SharedPreferences

    fun initialize(application: Application, appId: String) {
        if (!isInitialized.compareAndSet(false, true)) {
            maybeShowIntegrationCompleteDialog(OneSignal.User.pushSubscription.id)
            return
        }

        preferences = application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        application.registerActivityLifecycleCallbacks(this)

        setLogLevel(if (BuildConfig.DEBUG) LogLevel.VERBOSE else LogLevel.NONE)
        OneSignal.initWithContext(application, appId)
        addTags(createOneSignalDeviceTags(BuildConfig.FLAVOR, BuildConfig.VERSION_NAME))
        setupPushSubscriptionObserver()
    }

    private fun addTags(tags: Map<String, String>) {
        OneSignal.User.addTags(tags)
    }

    private fun setLogLevel(level: LogLevel) {
        OneSignal.Debug.logLevel = level
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity)
        if (shouldShowIntegrationDialog) {
            maybeShowIntegrationCompleteDialog(OneSignal.User.pushSubscription.id)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (currentActivity?.get() === activity) {
            currentActivity = null
        }
    }

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity?.get() === activity) {
            currentActivity = null
        }
    }

    private fun setupPushSubscriptionObserver() {
        OneSignal.User.pushSubscription.addObserver(pushSubscriptionObserver)
        maybeShowIntegrationCompleteDialog(OneSignal.User.pushSubscription.id)
    }

    private fun maybeShowIntegrationCompleteDialog(subscriptionId: String?) {
        if (!isRegisteredOneSignalSubscriptionId(subscriptionId) || hasShownIntegrationDialog()) {
            return
        }

        Handler(Looper.getMainLooper()).post {
            val activity = currentActivity?.get()
            if (activity == null || activity.isFinishing || activity.isDestroyed) {
                shouldShowIntegrationDialog = true
                return@post
            }

            if (!markIntegrationDialogShown() || !isIntegrationDialogVisible.compareAndSet(false, true)) {
                shouldShowIntegrationDialog = false
                return@post
            }

            shouldShowIntegrationDialog = false
            showIntegrationCompleteDialog(activity)
        }
    }

    private fun showIntegrationCompleteDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle(INTEGRATION_COMPLETE_TITLE)
            .setMessage(INTEGRATION_COMPLETE_MESSAGE)
            .setPositiveButton(INTEGRATION_COMPLETE_BUTTON) { _, _ ->
                requestPushPermission()
            }
            .setCancelable(false)
            .create()
            .apply {
                setOnDismissListener {
                    isIntegrationDialogVisible.set(false)
                }
                show()
            }
    }

    private fun requestPushPermission() {
        mainScope.launch {
            OneSignal.Notifications.requestPermission(true)
        }
    }

    private fun hasShownIntegrationDialog(): Boolean {
        return ::preferences.isInitialized && preferences.getBoolean(INTEGRATION_DIALOG_SHOWN_KEY, false)
    }

    private fun markIntegrationDialogShown(): Boolean {
        return ::preferences.isInitialized.also {
            if (it) {
                preferences.edit { putBoolean(INTEGRATION_DIALOG_SHOWN_KEY, true) }
            }
        }
    }
}





