package cc.ifnot.shortcuts.activity

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import cc.ifnot.shortcuts.App
import cc.ifnot.shortcuts.BuildConfig
import cc.ifnot.shortcuts.R
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initShortCuts()
        initData(intent)
    }

    private val DYNAMICSHORTCUTSID = "dynamic_shortcuts_id"

    private fun initShortCuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            for (i in 0..4) {

                val label = "dynamic_shortcuts" + i
                val info = ShortcutInfo.Builder(this, DYNAMICSHORTCUTSID + i)
                        .setLongLabel(label)
                        .setShortLabel(label)
                        .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                        .setIntent(Intent(this, MainActivity::class.java).setAction(Intent.ACTION_VIEW)
                                .putExtra(LAUNCHERMODE, when (i) {
                                    0 -> LAUNCHER_DYNAMIC_SHORTCUTS_ZERO
                                    1 -> LAUNCHER_DYNAMIC_SHORTCUTS_ONE
                                    else -> LAUNCHER_NORMAL
                                })
                        )
                        .build()

                val shortcutManager = this.getSystemService(SHORTCUT_SERVICE) as ShortcutManager
                if (shortcutManager.dynamicShortcuts.size + shortcutManager.manifestShortcuts.size < shortcutManager.maxShortcutCountPerActivity) {
                    shortcutManager.addDynamicShortcuts(Collections.singletonList(info))
                    Log.d(TAG, "shortcuts status!!!" + shortcutManager.dynamicShortcuts.size + shortcutManager.manifestShortcuts.size
                            + shortcutManager.maxShortcutCountPerActivity)
                    Log.d(TAG, DYNAMICSHORTCUTSID + i + " shortcuts has been created")
                } else {
                    Log.d(TAG, "max shortcuts!!!" + shortcutManager.dynamicShortcuts.size + shortcutManager.manifestShortcuts.size
                            + shortcutManager.maxShortcutCountPerActivity)
                    Log.d(TAG, DYNAMICSHORTCUTSID + i + " shortcuts has been created")
                }

            }
        }
    }

    private val TAG = this.javaClass.name

    private val LAUNCHERMODE = "cc.ifnot.shortcuts.activity.MainActivity.LAUNCHERMODE"

    private val LAUNCHER_NORMAL = 0
    private val LAUNCHER_DYNAMIC_SHORTCUTS_ZERO = 1.shl(0)
    private val LAUNCHER_DYNAMIC_SHORTCUTS_ONE = 1.shl(1)
    private val LAUNCHER_STATIC_SHORTCUTS = 1.shl(2)

    private fun initData(intent: Intent?) {
        val launcherMode = intent?.getIntExtra(LAUNCHERMODE, LAUNCHER_NORMAL)
        if (intent == null) {
            Log.d(TAG, "intent is null")
        }
        Log.d(TAG, launcherMode.toString())
        from.text = when (launcherMode) {
            LAUNCHER_STATIC_SHORTCUTS -> {
                App.prefs.edit().putInt(App.MAINACTIVITY_STATIC_SHORTCUTS_OPENS, App.prefs.getInt(App.MAINACTIVITY_STATIC_SHORTCUTS_OPENS, 0) + 1)
                        .apply()
                "from static shortcuts" + App.prefs.getInt(App.MAINACTIVITY_STATIC_SHORTCUTS_OPENS, 0).toString()
            }
            LAUNCHER_DYNAMIC_SHORTCUTS_ZERO -> {
                App.prefs.edit().putInt(App.MAINACTIVITY_DYNAMIC_SHORTCUTS_ZERO_OPENS, App.prefs.getInt(App.MAINACTIVITY_DYNAMIC_SHORTCUTS_ZERO_OPENS, 0) + 1)
                        .apply()
                "from dynamic shortcuts_zero" + App.prefs.getInt(App.MAINACTIVITY_DYNAMIC_SHORTCUTS_ZERO_OPENS, 0).toString()
            }
            LAUNCHER_DYNAMIC_SHORTCUTS_ONE -> {
                App.prefs.edit().putInt(App.MAINACTIVITY_DYNAMIC_SHORTCUTS_ONE_OPENS, App.prefs.getInt(App.MAINACTIVITY_DYNAMIC_SHORTCUTS_ONE_OPENS, 0) + 1)
                        .apply()
                "from dynamic shortcuts_zero" + App.prefs.getInt(App.MAINACTIVITY_DYNAMIC_SHORTCUTS_ONE_OPENS, 0).toString()
            }
            else -> {
                App.prefs.edit().putInt(App.MAINACTIVITY_OPENS, App.prefs.getInt(App.MAINACTIVITY_OPENS, 0) + 1)
                        .apply()
                null
            }
        }

        if (!TextUtils.isEmpty(from.text)) {
            Toast.makeText(this, from.text, Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun initView() {
        title = String.format("%s: %s", title, BuildConfig.VERSION_NAME)
        version.text = String.format("appId: %s\nversionName: %s\nversionCode: %s\ngitCommit: %s\nbuildTime: %s", BuildConfig.APPLICATION_ID,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE,
                BuildConfig.GIT_COMMIT,
                parseDate(BuildConfig.BUILD_TIME)
        )

        btn.setOnClickListener({
            startActivity(Intent(this, MainActivity2::class.java))
        })

        clear.setOnClickListener({
            App.prefs.edit().clear().apply()
            Toast.makeText(this, getString(R.string.clear_done), Toast.LENGTH_SHORT)
                    .show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })

        open.text = String.format("App Open Counts: %d\nMainActivity Open Counts(not from shortcuts): %d\nStatic ShortCuts Open: %d\nDynamic ShortCuts Open: %d -- %d",
                App.prefs.getInt(App.APP_OPENS, 0),
                App.prefs.getInt(App.MAINACTIVITY_OPENS, 0),
                App.prefs.getInt(App.MAINACTIVITY_STATIC_SHORTCUTS_OPENS, 0),
                App.prefs.getInt(App.MAINACTIVITY_DYNAMIC_SHORTCUTS_ZERO_OPENS, 0),
                App.prefs.getInt(App.MAINACTIVITY_DYNAMIC_SHORTCUTS_ONE_OPENS, 0)
        )
    }

    private fun parseDate(build_time: String): String =
            //return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(build_time.toLong()))
            SimpleDateFormat.getDateTimeInstance().format(Date(build_time.toLong()))
}
