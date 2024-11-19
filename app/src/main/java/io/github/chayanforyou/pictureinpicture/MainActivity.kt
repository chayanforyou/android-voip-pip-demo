package io.github.chayanforyou.pictureinpicture

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Rational
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.TextViewCompat
import io.github.chayanforyou.pictureinpicture.databinding.ActivityMainBinding
import io.github.chayanforyou.pictureinpicture.utils.setLayoutGravity
import io.github.chayanforyou.pictureinpicture.utils.setMarginRes
import io.github.chayanforyou.pictureinpicture.utils.setViewSizeRes


class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustFullScreen()

        binding.minimizeButton.setOnClickListener { minimize() }

        binding.hangup.setOnClickListener {
            binding.minimizeButton.visibility = View.GONE
            binding.localVideoView.visibility = View.GONE
            binding.controls.visibility = View.GONE
            binding.tvCallState.text = getString(R.string.call_ended)
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 1200L)
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        if (isInPictureInPictureMode) {
            binding.minimizeButton.visibility = View.GONE
            binding.localVideoView.visibility = View.GONE
            binding.controls.visibility = View.GONE
            binding.layProfile.setMarginRes(top = R.dimen.margin_0dp)
            binding.layProfile.setLayoutGravity(Gravity.CENTER)
            binding.profilePic.setViewSizeRes(R.dimen.image_size_pip)
            TextViewCompat.setTextAppearance(binding.tvName, R.style.TextAppearance_Medium)
            TextViewCompat.setTextAppearance(binding.tvCallState, R.style.TextAppearance_Small)
        } else {
            binding.minimizeButton.visibility = View.VISIBLE
            binding.localVideoView.visibility = View.VISIBLE
            binding.controls.visibility = View.VISIBLE
            binding.layProfile.setMarginRes(top =R.dimen.margin_180dp)
            binding.layProfile.setLayoutGravity(Gravity.CENTER_HORIZONTAL)
            binding.profilePic.setViewSizeRes(R.dimen.image_size_normal)
            TextViewCompat.setTextAppearance(binding.tvName, R.style.TextAppearance_Large)
            TextViewCompat.setTextAppearance(binding.tvCallState, R.style.TextAppearance_Medium)
        }
    }

    override fun onUserLeaveHint() {
        minimize()
    }

    private fun adjustFullScreen() {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun updatePictureInPictureParams(): PictureInPictureParams {
        val params = PictureInPictureParams.Builder()
            .setAspectRatio(Rational(9, 16))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            params.setAutoEnterEnabled(true)
                .setSeamlessResizeEnabled(false)
        }
        return params.build().also {
            setPictureInPictureParams(it)
        }
    }

    private fun minimize() {
        enterPictureInPictureMode(updatePictureInPictureParams())
    }
}
