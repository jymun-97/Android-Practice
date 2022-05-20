package mjy.pictureframe

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PictureFrameActivity: AppCompatActivity() {
    private lateinit var timer: Timer
    private var currentIndex: Int = 0
    private var nextIndex: Int = 1

    private val imageViewBack: ImageView by lazy {
        findViewById(R.id.imageViewBack)
    }
    private val imageViewFront: ImageView by lazy {
        findViewById(R.id.imageViewFront)
    }
    private val pictureList: MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pictureframe)

        getPictureList()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun onStart() {
        super.onStart()
        run()
    }

    private fun getPictureList() {
        val size = intent.getIntExtra("size", 0)
        for (i in 0..size) {
            intent.getStringExtra("picture$i")?.let {
                pictureList.add(Uri.parse(it))
            }
        }
    }

    private fun run() {
        timer = timer(period = 3 * 1000) {
            runOnUiThread {
                nextIndex = if (currentIndex + 1 >= pictureList.size) 0 else currentIndex + 1

                imageViewBack.setImageURI(pictureList[nextIndex])
                imageViewFront.apply {
                    alpha = 1f
                    setImageURI(pictureList[currentIndex])
                    animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .start()
                }
                currentIndex = nextIndex
            }
        }
    }
}