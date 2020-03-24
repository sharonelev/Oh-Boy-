package com.appsbysha.ohboy;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.appsbysha.ohboy.entities.Saying;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SayingAdapter extends RecyclerView.Adapter<SayingAdapter.SayingHolder> {
    private List<Saying> sayings = new ArrayList<>();
    private String dob;
    private OnItemClickListener listener;
    private OnLongClickListener longClickListener;

    @NonNull
    @Override
    public SayingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.saying_item, parent, false);
      return new SayingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SayingHolder holder, int position) {
      Saying currentSaying = sayings.get(position);
      holder.textViewTitle.setText(currentSaying.getTitle());
      if(currentSaying.getTitle().isEmpty())
      {holder.textViewTitle.setVisibility(View.GONE);}
      String sayingDate = currentSaying.getDate();
      holder.textViewDate.setText(sayingDate);
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      try {
        Date date = sdf.parse(sayingDate);
        Date childDob = sdf.parse(dob);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar calDob = Calendar.getInstance();
        calDob.setTime(childDob);
        long msdiff =cal.getTimeInMillis() - calDob.getTimeInMillis();
        cal.setTimeInMillis(msdiff);
        String diffYears = String.valueOf(cal.get(Calendar.YEAR)-1970);
        String diffMonths = String.valueOf(cal.get(Calendar.MONTH));
        String diffDays = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        holder.textViewAge.setText(diffYears + " years, " + diffMonths + " months and " + diffDays + " days old");

      } catch (ParseException e) {
        e.printStackTrace();
      }
      if(holder.expand) {
        holder.textViewDescription.setMaxLines(Integer.MAX_VALUE);
        holder.textViewDescription.setEllipsize(null);
      }
      else
      {
        holder.textViewDescription.setMaxLines(3);
        holder.textViewDescription.setEllipsize(TruncateAt.END);
      }
      if(currentSaying.getPhoto() != null) {
        Bitmap bitmap = BitmapFactory
            .decodeByteArray(currentSaying.getPhoto(), 0, currentSaying.getPhoto().length);
        holder.photo.setImageBitmap(bitmap);
      }

    }

    @Override
    public int getItemCount() {
      return sayings.size();
    }

    public void setSayings(List<Saying> sayings, String dob) {
      this.sayings = sayings;
      this.dob = dob;
      notifyDataSetChanged();
    }

    class SayingHolder extends RecyclerView.ViewHolder {

      private TextView textViewTitle;
      private TextView textViewDescription;
      private TextView textViewDate;
      private TextView textViewAge;
      private ImageView photo;
      private boolean expand = false;

      public SayingHolder(final View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.text_view_title);
        textViewDescription = itemView.findViewById(R.id.text_view_description);
        textViewDate = itemView.findViewById(R.id.text_view_date);
        textViewAge = itemView.findViewById(R.id.text_view_dob);
        photo = itemView.findViewById(R.id.child_image);
        itemView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
           int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
              listener.onItemClick(sayings.get(position));
            }
            expand = !expand;
          }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            if (longClickListener != null && position != RecyclerView.NO_POSITION) {
              longClickListener.onLongClick(sayings.get(position));
            }
            return false;
          }
        });

      }
    }

  public Saying getSayingAt(int position)
  {
    return sayings.get(position);
  }

  public interface OnItemClickListener {
    void onItemClick(Saying saying);
  }
  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  public interface OnLongClickListener{
      void onLongClick(Saying saying);
  }

  public void setLongClickListener(OnLongClickListener longClickListener){
      this.longClickListener = longClickListener;
  }
  }


