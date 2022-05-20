package mjy.alarm

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    private val onOffSwitch: Switch by lazy {
        findViewById(R.id.onOffSwitch)
    }
    private val timeTextView: TextView by lazy {
        findViewById(R.id.timeTextView)
    }
    private val ampmTextView: TextView by lazy {
        findViewById(R.id.ampmTextView)
    }
    private val infoTextView: TextView by lazy {
        findViewById(R.id.infoTextView)
    }
    private lateinit var model: AlarmDisplayModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initViews()
        bindViews()
    }

    private fun initData() {
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val timeDBValue = sharedPreference.getString(ALARM_KEY, "00:00") ?: "00:00"
        val onOffDBValue = sharedPreference.getBoolean(ONOFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        model = AlarmDisplayModel(
            alarmData[0].toInt(),
            alarmData[1].toInt(),
            onOffDBValue
        )

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        if ((pendingIntent == null) and model.onOff)
            model.onOff = false
        else if ((pendingIntent != null) and model.onOff.not())
            pendingIntent.cancel()
    }

    private fun initViews() {
        timeTextView.text = model.timeText
        ampmTextView.text = model.ampmText
        infoTextView.text =
            if (model.onOff) "${model.ampmText} ${model.timeText}에 알람이 울립니다."
            else resources.getString(R.string.alarm_off)
        onOffSwitch.isChecked = model.onOff
    }

    private fun bindViews() {
        initOnOffSwitch()
        initTimeTextView()
    }

    private fun initOnOffSwitch() {
        onOffSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            when {
                isChecked -> {
                    saveAlarm()
                    initViews()
                }
                else -> {
                    infoTextView.text = resources.getString(R.string.alarm_off)
                    removeAlarm()
                }
            }
        }
    }

    private fun initTimeTextView() {
        timeTextView.setOnClickListener {
            if (onOffSwitch.isChecked) {
                AlertDialog.Builder(this)
                    .setTitle("${model.ampmText} ${model.timeText}에 알람이 이미 켜져 있습니다.")
                    .setMessage("기존의 알람을 삭제하시겠습니까?")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                        showTimePickerDialog()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                        return@OnClickListener
                    })
                    .show()
            }
            else {
                showTimePickerDialog()
            }
        }
    }

    private fun showTimePickerDialog() {
        val calender = Calendar.getInstance()
        TimePickerDialog(
            this, { _, hour, minute ->
                saveAlarmModel(hour, minute, true)
                saveAlarm()
                initViews()
            },
            calender.get(Calendar.HOUR_OF_DAY),
            calender.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun saveAlarmModel(
        hour: Int,
        minute: Int,
        onOff: Boolean
    ) {
        model = AlarmDisplayModel(
            hour,
            minute,
            onOff
        )
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        with(sharedPreference.edit()) {
            putString(ALARM_KEY, model.timeText)
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }
    }

    private fun removeAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.cancel()
    }

    private fun saveAlarm() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, model.hour)
            set(Calendar.MINUTE, model.minute)

            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(this, "알람이 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val ALARM_REQUEST_CODE = 1000
    }
}