package net.onefivefour.android.bitpot.screens.file

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.SharedPrefsApp
import net.onefivefour.android.bitpot.customviews.AnimatedToggleImageView
import net.onefivefour.android.bitpot.databinding.ActivityFileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * This Activity downloads and shows the content of 1 file in text form.
 * The file content is loaded into a WebView which is running CodeMirror to
 * achieve syntax highlighting. More info on that in the [WebViewController].
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class FileActivity : AppCompatActivity(), WebViewController.Callback {
    
    private lateinit var binding : ActivityFileBinding

    /**
     * initializes the WebView showing the files content.
     * Use this field to make changes to its content.
     */
    private lateinit var webViewController: WebViewController

    private val args: FileActivityArgs by navArgs()

    private val fileViewModel: FileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.tvFileName.text = args.fileName
        webViewController = WebViewController(binding.wvFileContent, this, this)
        setupButtons()
        listenForSwipeGesture()
    }

    override fun onResume() {
        super.onResume()
        // enterImmersiveMode() must be executed in onResume to avoid losing
        // immersive mode when the user comes back from
        // the sharing feature.
        enterImmersiveMode()
    }

    private fun enterImmersiveMode() {
        // TODO Refactor to new methods of WindowCompat. Also see RepositoryActivity
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

    }

    private fun setupButtons() {
        binding.ivToggleLineNumbers.setOnClickListener {
            (it as AnimatedToggleImageView).toggle()
            fileViewModel.toggleLineNumbers()
        }
        binding.ivToggleLineWrap.setOnClickListener {
            (it as AnimatedToggleImageView).toggle()
            fileViewModel.toggleLineWrap()
        }
        binding.ivShare.setOnClickListener {
            webViewController.shareCurrentFile()
        }
    }

    private fun setupObservers() {
        fileViewModel.getFile(args.refHash, args.filePath, args.fileName).observe(this, Observer { fileContent ->
            if (fileContent.isNullOrEmpty()) return@Observer
            webViewController.updateContent(fileContent)
            webViewController.setCodeLanguage(args.fileName)
        })

        fileViewModel.areLineNumbersVisible().observe(this, Observer { isEnabled ->
            if (isEnabled == null) return@Observer
            webViewController.setLineNumbers(isEnabled)
            updateLineNumbersButton(isEnabled)
        })

        fileViewModel.isLineWrapEnabled().observe(this, Observer { isEnabled ->
            if (isEnabled == null) return@Observer
            webViewController.setLineWrap(isEnabled)
            updateLineWrapButton(isEnabled)
        })
    }

    private fun updateLineWrapButton(isEnabled: Boolean) {
        if (isEnabled) {
            binding.ivToggleLineWrap.disable()
        } else {
            binding.ivToggleLineWrap.enable()
        }
    }

    private fun updateLineNumbersButton(isEnabled: Boolean) {
        if (isEnabled) {
            binding.ivToggleLineNumbers.disable()
        } else {
            binding.ivToggleLineNumbers.enable()
        }
    }

    /**
     * Since it may be hard to figure out that the WebView can be pulled up,
     * we show a little animation to communicate this possibility to the user.
     * Using a TransitionListener, we set [SharedPrefsApp.userSwipedUpFileViewer] to true
     * as soon as the user completely pulled up the WebView on his own.
     */
    private fun listenForSwipeGesture() {
        if (SharedPrefsApp.userSwipedUpFileViewer) return

        binding.activityFileMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { /* do nothing */ }
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { /* do nothing */ }
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { /* do nothing */ }
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentStateid: Int) {
                if (currentStateid == R.id.end) {
                    SharedPrefsApp.userSwipedUpFileViewer = true
                    motionLayout.setTransitionListener(null)
                }
            }
        })
    }

    /**
     * Callback for the [WebViewController] to
     * tell us when the editor javascript was executed.
     */
    override fun onEditorReady() {
        Handler(mainLooper).post {
            setupObservers()
        }
    }
}