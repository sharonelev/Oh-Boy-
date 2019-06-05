package com.appsbysha.ohboy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.appsbysha.ohboy.entities.Child;
import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildHolder> {

  private OnItemClickListener listener;
  private List<Child> children = new ArrayList<>();

  @NonNull
  @Override
  public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.child_item, parent, false);
    return new ChildHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull ChildHolder holder, int position) {
    Child currentChild = children.get(position);
    holder.textViewName.setText(currentChild.getName());
    holder.textViewAge.setText(currentChild.getDob());
    if (currentChild.getProfilePic() != null) {
      Bitmap bitmap = BitmapFactory
          .decodeByteArray(currentChild.getProfilePic(), 0, currentChild.getProfilePic().length);
      holder.childImage.setImageBitmap(bitmap);
    }
  }

  @Override
  public int getItemCount() {
    return children.size();
  }

  public void setChildren(List<Child> children) {
    this.children = children;
    notifyDataSetChanged();
  }

  class ChildHolder extends RecyclerView.ViewHolder {

    private TextView textViewName;
    private TextView textViewAge;
    private ImageView childImage;

    public ChildHolder(View itemView) {
      super(itemView);
      textViewName = itemView.findViewById(R.id.text_view_name);
      textViewAge = itemView.findViewById(R.id.text_view_dob);
      childImage = itemView.findViewById(R.id.child_image);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position = getAdapterPosition();
          if (listener != null && position != RecyclerView.NO_POSITION) {
            listener.onItemClick(children.get(position));
          }
        }
      });
    }
  }

  public Child getChildAt(int position) {
    return children.get(position);
  }


  public interface OnItemClickListener {

    void onItemClick(Child child);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }
}
