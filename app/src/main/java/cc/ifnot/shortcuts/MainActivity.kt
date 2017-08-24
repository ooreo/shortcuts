package cc.ifnot.shortcuts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
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
