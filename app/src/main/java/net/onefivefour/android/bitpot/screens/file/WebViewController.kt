package net.onefivefour.android.bitpot.screens.file

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.view.animation.AccelerateDecelerateInterpolator
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ShareCompat
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.SharedPrefsApp
import net.onefivefour.android.bitpot.analytics.AnalyticsEvent
import net.onefivefour.android.bitpot.analytics.EventTracker
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * This controller is responsible for handling everything the
 * user sees in the file content WebView. This includes:
 *
 * * setting up the WebView
 * * Playing the hint animation
 * * allow changes to the editor config (line numbers, line wrap, dark mode, code language)
 * * share the file content
 *
 * To show the file content with syntax highlighting, we use CodeMirror (https://codemirror.net/)
 * which is a javascript syntax highlighter.
 */
@SuppressLint("SetJavaScriptEnabled")
class WebViewController(
    private val webView: WebView,
    private val activity: Activity,
    private val callback: Callback
): KoinComponent {

    private val eventTracker: EventTracker by inject()

    /**
     * The content that is visible in the WebView, once it loaded
     */
    private var fileContent: String = ""

    /**
     * The file name of the currently displayed file content.
     */
    private var fileName: String = ""

    companion object {
        private const val URL_SCRIPT_WRAPPER = "javascript:(function(){%s;})()"
    }

    init {
        webView.addJavascriptInterface(JavaScriptInterface(), "KotlinExecutor")
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.loadUrl("file:///android_asset/editor.html")
        checkAndPlayHintAnimation()
    }

    private fun setDarkMode(isEnabled: Boolean) {
        webView.loadUrl(wrapUrlScript("setDarkMode($isEnabled);"))
    }

    fun setCodeLanguage(fileName: String) {
        this.fileName = fileName
        val mimeType = FileTypeDetector.detectMimeType(fileName)
        val js = String.format("setLang('%s')", mimeType)
        webView.loadUrl(wrapUrlScript(js))
    }

    fun setLineNumbers(isEnabled: Boolean) {
        webView.loadUrl(wrapUrlScript("setLineNumbers($isEnabled);"))
    }

    fun setLineWrap(isEnabled: Boolean) {
        webView.loadUrl(wrapUrlScript("setLineWrap($isEnabled);"))
    }

    fun updateContent(fileContent: String) {
        this.fileContent = fileContent
        webView.loadUrl(wrapUrlScript("update();"))
    }

    private fun wrapUrlScript(script: String?): String {
        return String.format(URL_SCRIPT_WRAPPER, script)
    }

    private fun checkAndSetDarkMode() {
        when (activity.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> webView.post { setDarkMode(false) }
            Configuration.UI_MODE_NIGHT_YES,
            Configuration.UI_MODE_NIGHT_UNDEFINED -> webView.post { setDarkMode(true) }
        }
    }

    fun shareCurrentFile() {
        eventTracker.sendEvent(AnalyticsEvent.FileShared)

        ShareCompat.IntentBuilder(activity)
            .setText(fileContent)
            .setSubject(this.activity.getString(R.string.shared_file_via_bitpot, fileName))
            .setType("text/plain")
            .startChooser()
    }

    /**
     * This methods plays the hint animation to communicate to the user that
     * the WebView can be pulled up. It does this only until the user pulled
     * up the WebView once. After that we know that he got it.
     */
    private fun checkAndPlayHintAnimation() {
        if (SharedPrefsApp.userSwipedUpFileViewer) return

        webView.postDelayed({
            val root = activity.findViewById<MotionLayout>(R.id.activity_file_motion_layout)

            // Float value will go from 0 to 0.2 and back to 0, to shortly pull up the WebView a bit.
            // !Do not animate it to 1! Since this would trigger the onComplete Call back of the
            // TransitionListener in the FileActivity.
            val valueAnimator = ValueAnimator.ofFloat(0f, 0.2f, 0f)
            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
            valueAnimator.duration = 400
            valueAnimator.addUpdateListener {
                if (!SharedPrefsApp.userSwipedUpFileViewer) {
                    root.progress = it.animatedValue as Float
                }
            }
            valueAnimator.addListener(object : Animator.AnimatorListener {

                @Suppress("MayBeConst")
                val REPEAT_COUNT = 3
                var count = 0

                override fun onAnimationRepeat(animation: Animator?) { /* do nothing */ }
                override fun onAnimationEnd(animation: Animator?) {
                    // if the animation was not played 3 times, restart it after 1 second
                    if (++count < REPEAT_COUNT && !SharedPrefsApp.userSwipedUpFileViewer) {
                        valueAnimator.startDelay = 1000
                        valueAnimator.start()
                    }
                }

                override fun onAnimationCancel(animation: Animator?) { /* do nothing */ }
                override fun onAnimationStart(animation: Animator?) {
                    if (SharedPrefsApp.userSwipedUpFileViewer) {
                        webView.clearAnimation()
                        valueAnimator.end()
                    }
                }

            })
            
            if (!SharedPrefsApp.userSwipedUpFileViewer) {
                valueAnimator.start()
            }

        }, 3000)
    }


    /**
     * This class contains all the callbacks that the webView
     * can call. It is basically a bridge from javascript to Kotlin code.
     */
    @Suppress("unused")
    inner class JavaScriptInterface {
        @JavascriptInterface
        fun getCode(): String = fileContent

        @JavascriptInterface
        fun onWebViewReady() = checkAndSetDarkMode()

        @JavascriptInterface
        fun onDarkModeChecked() = callback.onEditorReady()
    }

    /**
     * Implement this interface to get notified as soon as the editor
     * in the WebView is completely loaded and ready to take instructions.
     */
    interface Callback {
        fun onEditorReady()
    }
}