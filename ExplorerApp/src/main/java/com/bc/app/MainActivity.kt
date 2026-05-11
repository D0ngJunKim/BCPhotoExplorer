package com.bc.app

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.bc.env.nav.Scaffold
import com.bc.feature.generated.nav.MainContainerRoutes
import com.bc.feature.generated.nav.OverlayContainerRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appFinishHelper = AppFinishHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                mainRoutes = MainContainerRoutes,
                overlayRoutes = OverlayContainerRoutes,
                onBack = { navigator ->
                    if (!navigator.navigateBack()) {
                        appFinishHelper.tryFinishApp(this)
                    }
                }
            )
        }
    }

    class AppFinishHelper {
        private var backPressedTime: Long = 0L
        private val backPressedInterval: Long = 2000
        private var lastToast: Toast? = null

        fun tryFinishApp(activity: MainActivity) {
            if (backPressedTime + backPressedInterval > System.currentTimeMillis()) {
                lastToast?.cancel()
                activity.finishAffinity()
            } else {
                val toast = Toast.makeText(activity, R.string.app_finish_toast_msg, Toast.LENGTH_SHORT)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    lastToast = toast
                    toast.addCallback(object : Toast.Callback() {
                        override fun onToastHidden() {
                            toast.removeCallback(this)
                            lastToast = null
                        }
                    })
                }
                toast.show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}