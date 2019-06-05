package com.appsbysha.ohboy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import com.appsbysha.ohboy.entities.Saying;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddSayingActivity extends AppCompatActivity {

  private SayingViewModel sayingViewModel;
  int childId;
  TextView editTextTitle;
  TextView editTextDescription;
  String sayingDate;
  DatePicker datePicker;
  ImageView addPhotoFromGallery;
  ImageView takePhoto;
  ImageView sayingImage;
  byte[] sayingByteImage;
  public static final int GET_FROM_GALLERY = 3;
  public static final int CAMERA_PIC_REQUEST = 200;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_saying);
    editTextTitle = findViewById(R.id.edit_text_title);
    editTextDescription = findViewById(R.id.edit_text_description);
    datePicker = findViewById(R.id.calendar_picker);
    addPhotoFromGallery = findViewById(R.id.upload_photo);
    takePhoto = findViewById(R.id.take_photo);
    sayingImage = findViewById(R.id.saying_image);

    addPhotoFromGallery.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivityForResult(new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
      }
    });

    takePhoto.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
      }
    });

    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    setTitle("Add Saying");

    Intent intent = getIntent();
    childId = intent.getIntExtra(MainActivity.extra_childId, 0);

    sayingViewModel = ViewModelProviders
        .of(this, new SayingViewModelFactory(this.getApplication(), childId))
        .get(SayingViewModel.class);


  }

  private void saveSaying() {
    String title = editTextTitle.getText().toString();
    String description = editTextDescription.getText().toString();
    int day = datePicker.getDayOfMonth();
    int month = datePicker.getMonth();
    int year = datePicker.getYear();
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    sayingDate = format.format(calendar.getTime());

    if (description.trim().isEmpty()) {
      Toast.makeText(this, "Please insert saying in description", Toast.LENGTH_SHORT).show();
      return;
    }

    Saying saying = new Saying(childId, title, description, sayingDate, sayingByteImage);
    sayingViewModel.insert(saying);
    finish();

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.add_saying_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save_saying:
        saveSaying();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Bitmap bitmap = null;
    //Detects request codes
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case GET_FROM_GALLERY:
          Uri selectedImage = data.getData();
          try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
          } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
          break;
        case CAMERA_PIC_REQUEST:
          bitmap = (Bitmap) data.getExtras().get("data");
          break;
      }
      if (bitmap != null) {
        sayingImage.setImageBitmap(bitmap);
        sayingImage.setVisibility(View.VISIBLE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        sayingByteImage = stream.toByteArray();
      }
    }
  }
}



