package com.appsbysha.ohboy.adapters

import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsbysha.ohboy.R
import com.appsbysha.ohboy.entities.Line
import com.appsbysha.ohboy.entities.LineType


class LineAdapter : RecyclerView.Adapter<LineAdapter.LineHolder>() {

    var lines: MutableList<Line> = mutableListOf()
    var isEditable: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineHolder {

        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.saying_line, parent, false)
        return LineHolder(itemView)
    }

    override fun getItemCount(): Int {
        return lines.size
    }

    fun setLines(lines: MutableList<Line>, isEditable: Boolean) {
        this.lines = lines
        this.isEditable = isEditable
      //  this.lineViewModel = lineViewModel
        notifyDataSetChanged()
    }

    fun addLine(line: Line)
    {
        this.lines.add(line)
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: LineHolder, position: Int) {
        lines[position].position = position
        //  lineViewModel?.update(lines[position])
        lines[position].childPic?.let {
        val bitmap = BitmapFactory
            .decodeByteArray(it, 0, it.size)
        holder.childLinePic.setImageBitmap(bitmap)
    }
        if(!isEditable){
            holder.childRemoveIcon.visibility = View.GONE
            holder.bracketsRemoveIcon.visibility = View.GONE
            holder.otherPersonRemoveIcon.visibility = View.GONE
        }
        else{
            holder.childRemoveIcon.setOnClickListener {
            lines.removeAt(position)
                notifyDataSetChanged()
            }
            holder.bracketsRemoveIcon.setOnClickListener {
                lines.removeAt(position)
                notifyDataSetChanged()
            }
            holder.otherPersonRemoveIcon.setOnClickListener {
                lines.removeAt(position)
                notifyDataSetChanged()
            }
        }

        holder.childLine.visibility = View.GONE
        holder.bracketsLine.visibility = View.GONE
        holder.otherPersonLine.visibility = View.GONE

        when (lines[position].type) {
            LineType.CHILD_LINE.value -> {
                holder.childLine.visibility = View.VISIBLE
                holder.childLineDesc.setText(lines[position].description)
                holder.childLineDesc.isEnabled = isEditable
            }
            LineType.BRACKETS.value -> {
                holder.bracketsLine.visibility = View.VISIBLE
                holder.bracketsDesc.setText(lines[position].description)
                holder.bracketsDesc.isEnabled = isEditable
            }
            LineType.OTHER_PERSON_LINE.value -> {
                holder.otherPersonLine.visibility = View.VISIBLE
                holder.otherPersonDesc.setText(lines[position].description)
                holder.otherPersonName.setText(lines[position].other_person)
                holder.otherPersonDesc.isEnabled = isEditable
                holder.otherPersonName.isEnabled = isEditable
            }
        }

    }


    inner class LineHolder(itemView: View) : RecyclerView.ViewHolder(itemView), TextWatcher {

        val childLine: View = itemView.findViewById(R.id.child_line)
        val childLineDesc: EditText = childLine.findViewById(R.id.text_view_description)
        val childLinePic: ImageView = childLine.findViewById(R.id.child_image)
        val bracketsLine: View = itemView.findViewById(R.id.brackets_line)
        val bracketsDesc: EditText = bracketsLine.findViewById(R.id.text_view_description)
        val otherPersonLine : View = itemView.findViewById(R.id.other_person_line)
        val otherPersonDesc: EditText  = otherPersonLine.findViewById(R.id.text_view_description)
        val otherPersonName : EditText = otherPersonLine.findViewById(R.id.text_view_name)
        val childRemoveIcon: TextView = childLine.findViewById(R.id.remove_line)
        val bracketsRemoveIcon: TextView = bracketsLine.findViewById(R.id.remove_line)
        val otherPersonRemoveIcon: TextView = otherPersonLine.findViewById(R.id.remove_line)



        init {

            //todo remove click
            childLineDesc.addTextChangedListener(this)
            bracketsDesc.addTextChangedListener(this)
            otherPersonDesc.addTextChangedListener(this)
            otherPersonName.addTextChangedListener(this)

        }

        override fun afterTextChanged(s: Editable?) {
            if (s === otherPersonName.editableText)
                lines[adapterPosition].other_person = s.toString()
            else
                lines[adapterPosition].description = s.toString()

          //  lineViewModel?.update(lines[adapterPosition])
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }


}
