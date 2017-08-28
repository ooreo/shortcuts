package cc.ifnot.shortcuts

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import io.sentry.Sentry


/**
 * Created by dp on 2017/8/28.
 */
class App : Application() {

    init {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean = BuildConfig.DEBUG
        })

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Sentry.init().sendException(e)
//            FirebaseCrash.report(e)
            Logger.d(t)
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        initApp()
    }

    private fun initApp() {
        prefs.edit().putInt(APP_OPENS, prefs.getInt(APP_OPENS, 0) + 1)
                .apply()
    }

    companion object {

        lateinit var instance: App
            private set
        lateinit var prefs: SharedPreferences
            private set
        val APP_OPENS = "cc.ifnot.shortcuts.App.Open.Counts"
        val MAINACTIVITY_OPENS = "cc.ifnot.shortcuts.MainActivity.Open.Counts"
        val MAINACTIVITY_STATIC_SHORTCUTS_OPENS = "cc.ifnot.shortcuts.MainActivity.Static.ShortCuts.Open.Counts"
        val MAINACTIVITY_DYNAMIC_SHORTCUTS_ZERO_OPENS = "cc.ifnot.shortcuts.MainActivity.Dynamic.ShortCuts.Zero.Open.Counts"
        val MAINACTIVITY_DYNAMIC_SHORTCUTS_ONE_OPENS = "cc.ifnot.shortcuts.MainActivity.Dynamic.ShortCuts.One.Open.Counts"

//        fun getInstance(): App = instance

//        fun getPrefs(): SharedPreferences = prefs
    }

}