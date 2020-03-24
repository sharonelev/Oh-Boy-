package com.appsbysha.ohboy

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.appsbysha.ohboy.entities.Saying
import com.appsbysha.ohboy.entities.SayingWithSentences
import com.appsbysha.ohboy.entities.Sentence
import kotlinx.android.synthetic.main.enter_date_layout.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddSayingActivity : AppCompatActivity() {
    private lateinit var sayingViewModel: SayingViewModel
    var childId = 0
    private lateinit var editTextTitle: TextView
    private lateinit var editTextDescription: TextView
    var sayingDate: String? = null
    private lateinit var addPhotoFromGallery: ImageView
    var sayingImage: ImageView? = null
    lateinit var sayingByteImage: ByteArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_saying)
        editTextTitle = findViewById(R.id.edit_text_title)
        addPhotoFromGallery = findViewById(R.id.upload_photo)
        addPhotoFromGallery.setOnClickListener(View.OnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY)
        })
        /*    takePhoto.setOnClickListener(View.OnClickListener {
                val cameraIntent = Intent("android.media.action.IMAGE_CAPTURE")
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
            })*/
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        title = "Add Saying"
        val intent = intent
        childId = intent.getIntExtra(MainActivity.extra_childId, 0)
        sayingViewModel = ViewModelProviders
                .of(this, SayingViewModelFactory(this.application, childId))
                .get(SayingViewModel::class.java)
    }

    private fun saveSaying() {
        val title = editTextTitle!!.text.toString()
        val saying = Saying(childId, title, sayingDate, sayingByteImage)


        val sentence = Sentence(0, description = editTextDescription!!.text.toString(), sentenceSayingId = saying.sayingId, isBracket = false)
        val sentences = ArrayList<Sentence>()  //1 sentence in list for now
        sentences.add(sentence)

        val sws: SayingWithSentences = SayingWithSentences(saying, sentences)
        var value = text_view_dd.text.toString()
        val day: Int = value.toInt()
        value = text_view_MM.text.toString()
        val month: Int = value.toInt()
        value = text_view_yyyy.text.toString()
        val year: Int= value.toInt()
        val calendar = Calendar.getInstance()
        calendar[year, month] = day
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sayingDate = format.format(calendar.time)
        /*  if (description.trim().isEmpty()) {
      Toast.makeText(this, "Please insert saying in description", Toast.LENGTH_SHORT).show();
      return;
    }*/
        sayingViewModel.insert(saying)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_saying_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_saying -> {
                saveSaying()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap: Bitmap? = null
        //Detects request codes
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GET_FROM_GALLERY -> {
                    val selectedImage = data!!.data
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                    } catch (e: FileNotFoundException) { // TODO Auto-generated catch block
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                CAMERA_PIC_REQUEST -> bitmap = data!!.extras["data"] as Bitmap
            }
            if (bitmap != null) {
                sayingImage!!.setImageBitmap(bitmap)
                sayingImage!!.visibility = View.VISIBLE
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                sayingByteImage = stream.toByteArray()
            }
        }
    }

    companion object {
        const val GET_FROM_GALLERY = 3
        const val CAMERA_PIC_REQUEST = 200
    }
}