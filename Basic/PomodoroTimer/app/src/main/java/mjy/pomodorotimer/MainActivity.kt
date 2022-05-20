package mjy.pomodorotimer

import android.content.IntentSender
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val remainMinutesTextView: TextView by lazy {
        findViewById(R.id.textViewMinutes)
    }
    private val remainSecondsTextView: TextView by lazy {
        findViewById(R.id.textViewSeconds)
    }
    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekBar)
    }
    private val stopButton: AppCompatButton by lazy {
        findViewById(R.id.buttonStop)
    }
    private var currentCountDownTimer: CountDownTimer? = null

    private val soundPool = SoundPool.Builder().build()
    private var tickingSoundID: Int? = null
    private var bellSoundID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        initSounds()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        updateRemainTime(progress * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    stopCountDown()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar ?: return

                    if (seekBar.progress == 0) {
                        stopCountDown()
                    }
                    else {
                        startCountDown()
                    }
                }
            }
        )
    }

    private fun initSounds() {
        tickingSoundID = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundID = soundPool.load(this, R.raw.timer_bell, 1)
    }

    private fun createCountDownTimer(initialMillis: Long) =
        object: CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }

    private fun startCountDown() {
        currentCountDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()

        tickingSoundID?.let { soundID ->
            soundPool.play(soundID, 1F, 1F, 0, -1, 1F)
        }
    }

    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / 1000 / 60).toInt()
    }

    private fun completeCountDown() {
        updateRemainTime(0)
        updateSeekBar(0)

        soundPool.autoPause()
        bellSoundID?.let { soundID ->
            soundPool.play(soundID, 1F, 1F, 0, -1, 1F)
        }
        activateStopButton()
    }

    private fun stopCountDown() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        soundPool.autoPause()
    }

    private fun activateStopButton() {
        stopButton.isVisible = true
        stopButton.isEnabled = true
        stopButton.isClickable = true

        stopButton.setOnClickListener {
            stopCountDown()
            hideStopButton()
        }
    }

    private fun hideStopButton() {
        stopButton.isVisible = false
        stopButton.isEnabled = false
        stopButton.isClickable = false
    }
}