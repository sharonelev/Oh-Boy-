package com.appsbysha.ohboy;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appsbysha.ohboy.adapters.SayingAdapter;
import com.appsbysha.ohboy.adapters.SayingAdapter.OnItemClickListener;
import com.appsbysha.ohboy.adapters.SayingAdapter.OnLongClickListener;
import com.appsbysha.ohboy.database.SayingViewModel;
import com.appsbysha.ohboy.database.SayingViewModelFactory;
import com.appsbysha.ohboy.entities.Saying;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//saying activity of chosen child


  private SayingViewModel sayingViewModel;
  public static String extra_childId = "CHILDID";
  public static String extra_childdob = "CHILDDOB";
  public static String extra_childname = "CHILDNAME";
  public static String extra_childpic = "CHILDPIC";

  private int childId;
  private String childDob;
  private String childName;
  private Byte childPic;
  Intent intent;


  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if(intent == null) {
      intent = getIntent();
      childId = intent.getIntExtra(extra_childId, 0);
      childName = intent.getStringExtra(extra_childname);
      childDob = intent.getStringExtra(extra_childdob);
      //childPic = intent.getByteExtra(extra_childpic, (byte) -1);
    }

    setTitle(childName + "  " + childDob);

    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setHasFixedSize(true);

    final SayingAdapter adapter = new SayingAdapter(this, this);
    recyclerView.setAdapter(adapter);

    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(Saying saying) {
        adapter.notifyDataSetChanged();
      }
    });

    adapter.setLongClickListener(new OnLongClickListener() {
      @Override
      public void onLongClick(Saying saying) {
        Toast.makeText(MainActivity.this, "long",Toast.LENGTH_SHORT).show();
      }
    });


    FloatingActionButton buttonAddSaying = findViewById(R.id.add_saying_fb);
    buttonAddSaying.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, AddSayingActivity.class);
        intent.putExtra(extra_childId,childId);
        intent.putExtra(extra_childpic,childPic );
        startActivity(intent);
      }
    });


    sayingViewModel = new ViewModelProvider(this, new SayingViewModelFactory(this.getApplication(), childId)).get(SayingViewModel.class);

    //sayingViewModel = ViewModelProviders.of(this).get(SayingViewModel.class);
    sayingViewModel.getAllSayings().observe(this, new Observer<List<Saying>>() {
      @Override
      public void onChanged(List<Saying> sayings) {
        adapter.setSayings(sayings, childDob);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to delete saying?").setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                sayingViewModel.delete(adapter.getSayingAt(newViewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Saying deleted", Toast.LENGTH_SHORT).show();
              }
            })
            .setNegativeButton("No", null).show();

      }
    }).attachToRecyclerView(recyclerView);
  }
}
