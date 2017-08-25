package cc.ifnot.shortcuts

import android.content.Intent
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
        initData(intent)
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
    }

    private fun parseDate(build_time: String): String =
            //return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(build_time.toLong()))
            SimpleDateFormat.getDateTimeInstance().format(Date(build_time.toLong()))
}