package cc.ifnot.shortcuts

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
                                .putExtra(LAUNCHERMODE, LAUNCHER_DYNAMIC_SHORTCUTS)
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

    private val LAUNCHERMODE = "cc.ifnot.shortcuts.MainActivity.LAUNCHERMODE"

    private val LAUNCHER_NORMAL = 0
    private val LAUNCHER_STATIC_SHORTCUTS = 1.shl(0)
    private val LAUNCHER_DYNAMIC_SHORTCUTS = 1.shl(1)

    private fun initData(intent: Intent?) {
        val launcherMode = intent?.getIntExtra(LAUNCHERMODE, LAUNCHER_NORMAL)
        if (intent == null) {
            Log.d(TAG, "intent is null")
        }
        Log.d(TAG, launcherMode.toString())
        from.text = when (launcherMode) {
            LAUNCHER_STATIC_SHORTCUTS -> {
                "from static shortcuts"
            }
            LAUNCHER_DYNAMIC_SHORTCUTS -> {
                "from dynamic shortcuts"
            }
            else -> {
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
    }

    private fun parseDate(build_time: String): String =
            //return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(build_time.toLong()))
            SimpleDateFormat.getDateTimeInstance().format(Date(build_time.toLong()))
}
