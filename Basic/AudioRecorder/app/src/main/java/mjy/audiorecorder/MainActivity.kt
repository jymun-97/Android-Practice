package mjy.audiorecorder

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var state = State.BEFORE_RECORDING
        set(value) {
            field = value
            recordButton.updateIconWithState(field)
            resetButton.isEnabled = (value == State.AFTER_RECORDING || value == State.ON_PLAYING)
        }
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    private val requiredPermissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val recordButton: RecordButton by lazy {
        findViewById(R.id.recordButton)
    }
    private val resetButton: Button by lazy {
        findViewById(R.id.resetButton)
    }
    private val countUpTextView: CountUpTextView by lazy {
        findViewById(R.id.countUpTextView)
    }
    private val soundVisualizerView: SoundVisualizerView by lazy {
        findViewById(R.id.soundVisualizerView)
    }
    private val recordingFilePath: String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
        initViews()
        bindViews()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                    return
            }
            else -> {
                return
            }
        }
    }

    private fun isGrantedPermission(): Boolean = ContextCompat.checkSelfPermission(
        this,
        requiredPermissions[0]
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestAudioRecordPermissionAgain() {
        AlertDialog.Builder(this)
            .setTitle("알림")
            .setMessage("녹음 권한이 거부되었습니다. 사용을 원하시면 설정에서 직접 권한을 허용해야 합니다.")
            .setNeutralButton("설정") { _, _ ->
                startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${packageName}"))
                )
            }
            .setPositiveButton("확인") { _, _ -> }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun initViews() {
        recordButton.updateIconWithState(state)
        resetButton.isEnabled = false
    }

    private fun bindViews() {
        soundVisualizerView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude ?: 0
        }
        recordButton.setOnClickListener {
            if (!isGrantedPermission()) {
                requestAudioRecordPermissionAgain()
                return@setOnClickListener
            }

            when (state) {
                State.BEFORE_RECORDING -> {
                    startRecording()
                }
                State.ON_RECORDING -> {
                    stopRecording()
                }
                State.AFTER_RECORDING -> {
                    startPlaying()
                }
                State.ON_PLAYING -> {
                    stopPlaying()
                }
            }
        }

        resetButton.setOnClickListener {
            stopPlaying()
            state = State.BEFORE_RECORDING
            soundVisualizerView.clearVisualizeing()
            countUpTextView.clearCountUp()
        }
    }

    private fun startRecording() {
        state = State.ON_RECORDING
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }
        recorder?.start()
        soundVisualizerView.startVisualizing(false)
        countUpTextView.startCountUp()
    }

    private fun stopRecording() {
        state = State.AFTER_RECORDING
        recorder?.run {
            stop()
            release()
        }
        recorder = null
        soundVisualizerView.stopVisualizing()
        countUpTextView.stopCountUp()
    }

    private fun startPlaying() {
        state = State.ON_PLAYING
        player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            setOnCompletionListener {
                stopPlaying()
            }
            prepare()
        }
        player?.start()
        soundVisualizerView.startVisualizing(true)
        countUpTextView.startCountUp()
    }

    private fun stopPlaying() {
        state = State.AFTER_RECORDING
        player?.run {
            stop()
            release()
        }
        player = null
        soundVisualizerView.stopVisualizing()
        countUpTextView.stopCountUp()
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 1000
    }
}