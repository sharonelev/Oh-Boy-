package com.appsbysha.ohboy

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appsbysha.ohboy.UiUtils.Companion.dateFormatter
import com.appsbysha.ohboy.adapters.LineAdapter
import com.appsbysha.ohboy.database.*
import com.appsbysha.ohboy.entities.Line
import com.appsbysha.ohboy.entities.LineType
import com.appsbysha.ohboy.entities.Saying
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class AddSayingActivity : AppCompatActivity() {
    private lateinit var sayingViewModel: SayingViewModel
    private lateinit var lineViewModel: LineViewModel
    private lateinit var childViewModel: ChildViewModel
    private var childId = 0
    private var childPic: ByteArray? = null
    private lateinit var editTextTitle: TextView
    private lateinit var dd : TextView
    private lateinit var MM : TextView
    private lateinit var yyyy  : TextView
    private lateinit var editTextDescription: TextView
    private lateinit var addPhotoFromGallery: ImageView
    private var sayingImage: ImageView? = null
    private var sayingByteImage: ByteArray? = null
    lateinit var saying: Saying
    private lateinit var linesRecyclerView: RecyclerView
    private lateinit var linesAdapter: LineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_saying)

        editTextTitle = findViewById(R.id.edit_text_title)
        dd   = findViewById<EditText>(R.id.text_view_dd)
        MM   = findViewById<EditText>(R.id.text_view_MM)
        yyyy = findViewById<EditText>(R.id.text_view_yyyy)
        addPhotoFromGallery = findViewById(R.id.upload_photo)
        addPhotoFromGallery.visibility = View.GONE
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
    //    childPic = intent.getByteArrayExtra(MainActivity.extra_childpic)


        saying = Saying(sayingId = UUID.randomUUID().toString(), childId = childId)


        linesRecyclerView = findViewById(R.id.linesRecyclerView)
        linesRecyclerView.layoutManager = LinearLayoutManager(this)
        linesRecyclerView.setHasFixedSize(true)
        linesAdapter = LineAdapter()
        linesAdapter.isEditable = true
        //linesAdapter.setLines(listOfLines, true)
        linesRecyclerView.adapter = linesAdapter

        sayingViewModel = ViewModelProvider(this, SayingViewModelFactory(this.application, childId))
                .get(SayingViewModel::class.java)


         sayingViewModel.fetchChildData().observe(this){child->
            childPic = child.profilePic
        }

        setButtons()
        lineViewModel = ViewModelProvider(this, LineViewModelFactory(this.application, childId, saying))
                .get(LineViewModel::class.java)

        lineViewModel.allLines.observe(this, Observer { lines -> linesAdapter.setLines(lines, true) }) //was listOfLines


    }

    private fun setButtons() {
        val sayingChildButton = findViewById<ImageView>(R.id.saying_child_button)
        childPic?.let {
        val bitmap = BitmapFactory
            .decodeByteArray(it, 0, it.size)
        sayingChildButton.setImageBitmap(bitmap)}
        sayingChildButton.setOnClickListener {
            val newLine = Line(UUID.randomUUID().toString(), saying.sayingId, 0, "", LineType.CHILD_LINE.value, null, childPic)
            //     lineViewModel.insert(newLine)
       //     listOfLines.add(newLine)
            linesAdapter.addLine(newLine)
            //linesAdapter.setLines(listOfLines, true)
        }
        val bracketsButton = findViewById<TextView>(R.id.brackets_button)
        bracketsButton.setOnClickListener {
            val newLine = Line(UUID.randomUUID().toString(), saying.sayingId, 0, "", LineType.BRACKETS.value, null, null)
            //    lineViewModel.insert(newLine)
        //    listOfLines.add(newLine)
       //     linesAdapter.setLines(listOfLines, true)
            linesAdapter.addLine(newLine)

        }
        val otherPersonButton = findViewById<ImageView>(R.id.other_person_button)
        otherPersonButton.setOnClickListener {
            val newLine = Line(UUID.randomUUID().toString(), saying.sayingId, 0, "", LineType.OTHER_PERSON_LINE.value, "", null)
            //    lineViewModel.insert(newLine)
        //    listOfLines.add(newLine)
       //     linesAdapter.setLines(listOfLines, true)
            linesAdapter.addLine(newLine)
        }


    }

    private fun saveSaying() {

        val title = editTextTitle.text.toString()

        val sayingDate = dateFormatter(
            dd.getText().toString(),
            MM.getText().toString(),
            yyyy.getText().toString()
        )


        saying.title = title
        saying.date = sayingDate
        saying.photo = sayingByteImage

        //  val sws: SayingWithLines = SayingWithLines(saying, listOfLines)

        for (line in linesAdapter.lines) {
            lineViewModel.insert(line)
        }
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
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                GET_FROM_GALLERY -> {
                    val selectedImage = data?.data
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                    } catch (e: FileNotFoundException) { // TODO Auto-generated catch block
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                CAMERA_PIC_REQUEST -> bitmap = data?.extras?.get("data") as Bitmap
            }
            if (bitmap != null) {
                sayingImage?.setImageBitmap(bitmap)
                sayingImage?.visibility = View.VISIBLE
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