package com.appsbysha.ohboy;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appsbysha.ohboy.adapters.ChildAdapter;
import com.appsbysha.ohboy.adapters.ChildAdapter.OnItemClickListener;
import com.appsbysha.ohboy.database.ChildViewModel;
import com.appsbysha.ohboy.entities.Child;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ChildListActivity extends AppCompatActivity {

  View addView;

  private ChildViewModel childViewModel;
  private ImageView childImage;
  public static final int GET_FROM_GALLERY = 3;
  private byte[] childByteImage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_children);

    childImage = findViewById(R.id.child_image);
    final ImageView addChild = findViewById(R.id.add_child_fb);
    final View doneAddingView= findViewById(R.id.finish_add_view);
    final ImageView doneAdding = findViewById(R.id.done_add_child_fb);
    final ImageView cancelAdding = findViewById(R.id.cancel_add_child_fb);
    final EditText addName = findViewById(R.id.text_view_name);
    final EditText dd = findViewById(R.id.text_view_dd);
    final EditText MM = findViewById(R.id.text_view_MM);
    final EditText yyyy = findViewById(R.id.text_view_yyyy);
    final TextView firstChild = findViewById(R.id.first_child_text);
    addView = findViewById(R.id.add_child_view);

    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setHasFixedSize(true);

    final ChildAdapter adapter = new ChildAdapter();

    recyclerView.setAdapter(adapter);

    childViewModel = ViewModelProviders.of(this).get(ChildViewModel.class);
    childViewModel.getAllChildren().observe(this, new Observer<List<Child>>() {
      @Override
      public void onChanged(List<Child> children) {
        adapter.setChildren(children);
        if(adapter.getItemCount() == 0)
        {
          firstChild.setVisibility(View.VISIBLE);
          addView.setVisibility(View.VISIBLE);
          addName.setText("");
          dd.setText("");
          MM.setText("");
          yyyy.setText("");
          addChild.setVisibility(View.GONE);
          doneAddingView.setVisibility(View.VISIBLE);}

      else {
          firstChild.setVisibility(View.GONE);
          addView.setVisibility(View.GONE);
          addChild.setVisibility(View.VISIBLE);
          doneAddingView.setVisibility(View.GONE);

        }}
    });




    childImage.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivityForResult(new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
      }
    });

    addChild.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        addView.setVisibility(View.VISIBLE);
        addName.setText("");
        dd.setText("");
        MM.setText("");
        yyyy.setText("");
        addChild.setVisibility(View.GONE);
        doneAddingView.setVisibility(View.VISIBLE);
      }
    });

    doneAdding.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String ddString = dd.getText().toString();
        String MMString = MM.getText().toString();
        String yyyyString = yyyy.getText().toString();

        if(ddString.length() == 1)
          ddString = "0" + ddString;

        if(MMString.length() == 1)
          MMString = "0" + MMString;

            ;
        if(addName.getText().toString().isEmpty() ||
        ddString.isEmpty() ||
            MMString.isEmpty() ||
            yyyyString.isEmpty() ||
            Integer.parseInt(ddString) >31 ||
            Integer.parseInt(ddString) == 0 ||
            Integer.parseInt(MMString)>12 ||
            Integer.parseInt(MMString) == 0 ||
            yyyyString.length()<4

        )
        {
          Toast.makeText(ChildListActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
          return;
        }

        String newDob = ddString + "/" + MMString + "/" + yyyyString;
        Child addNewChild = new Child(addName.getText().toString(), newDob, childByteImage);
        childViewModel.insert(addNewChild);
        addView.setVisibility(View.GONE);
        addChild.setVisibility(View.VISIBLE);
        doneAddingView.setVisibility(View.GONE);
      }
    });

    cancelAdding.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        addView.setVisibility(View.GONE);
        addChild.setVisibility(View.VISIBLE);
        doneAddingView.setVisibility(View.GONE);
      }
    });


    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(Child child) {
        Intent intent = new Intent(ChildListActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.extra_childId, child.getId());
        intent.putExtra(MainActivity.extra_childname, child.getName());
        intent.putExtra(MainActivity.extra_childdob, child.getDob());
        startActivity(intent);

      }
    });
    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final RecyclerView.ViewHolder newViewHolder = viewHolder;
        AlertDialog.Builder builder = new AlertDialog.Builder(ChildListActivity.this);
        builder.setMessage("Are you sure you want to delete child?").setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                childViewModel.delete(adapter.getChildAt(newViewHolder.getAdapterPosition()));
                Toast.makeText(ChildListActivity.this, "Child deleted", Toast.LENGTH_SHORT).show();
              }
            })
            .setNegativeButton("No", null).show();

      }
    }).attachToRecyclerView(recyclerView);

  }  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //Detects request codes
    if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
      Uri selectedImage = data.getData();
      Bitmap bitmap = null;
      try {
        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

        /*Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f);
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, 20, 20, matrix, true);
        */
        childImage.setImageBitmap(bitmap);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        childByteImage = stream.toByteArray();

      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}