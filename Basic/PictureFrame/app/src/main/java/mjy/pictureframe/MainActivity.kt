package mjy.pictureframe

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.isVisible
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val buttonAdd: Button by lazy {
        findViewById(R.id.buttonAdd)
    }
    private val buttonRun: Button by lazy {
        findViewById(R.id.buttonRun)
    }
    private val imageViewList: List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.imageView1))
            add(findViewById(R.id.imageView2))
            add(findViewById(R.id.imageView3))
            add(findViewById(R.id.imageView4))
            add(findViewById(R.id.imageView5))
            add(findViewById(R.id.imageView6))
        }
    }
    private val imageUriList: MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    navigatePictures()
                }
            }
            else -> {
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            2000 -> {
                val selectImageUri = data?.data
                if (selectImageUri != null) {
                    if (imageUriList.size >= 6) {
                        Toast.makeText(this, "더이상 사진을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    imageUriList.add(selectImageUri)
                    imageViewList[imageUriList.size - 1].setImageURI(selectImageUri)
                } else {
                    Toast.makeText(this, "사진을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                return
            }
        }
    }

    private fun initViews() {
        buttonAdd.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    navigatePictures()
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }

                else -> {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1000
                    )
                }
            }
        }

        buttonRun.setOnClickListener {
            val intent = Intent(this, PictureFrameActivity::class.java)
            imageUriList.forEachIndexed { index, uri ->
                intent.putExtra("picture$index", uri.toString())
            }
            intent.putExtra("size", imageUriList.size)
            startActivity(intent)
        }
    }

    private fun navigatePictures() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("전자액자 앱에서 사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }
}
