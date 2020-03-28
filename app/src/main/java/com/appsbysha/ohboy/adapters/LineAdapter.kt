package com.appsbysha.ohboy.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsbysha.ohboy.R
import com.appsbysha.ohboy.database.LineViewModel
import com.appsbysha.ohboy.entities.Line
import com.appsbysha.ohboy.entities.LineType
import kotlinx.android.synthetic.main.brackets_line.view.*
import kotlinx.android.synthetic.main.child_line.view.text_view_description
import kotlinx.android.synthetic.main.other_person_line.view.*
import kotlinx.android.synthetic.main.other_person_line.view.remove_line
import kotlinx.android.synthetic.main.saying_line.view.*

class LineAdapter() : RecyclerView.Adapter<LineAdapter.LineHolder>() {

    private var lines: MutableList<Line> = mutableListOf()
    private var isEditable: Boolean = false
   // private var lineViewModel: LineViewModel? = null
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

    override fun onBindViewHolder(holder: LineHolder, position: Int) {

        lines[position].position = position
      //  lineViewModel?.update(lines[position])

        holder.itemView.child_line.visibility = View.GONE
        holder.itemView.brackets_line.visibility = View.GONE
        holder.itemView.other_person_line.visibility = View.GONE

        when (lines[position].type) {
            LineType.CHILD_LINE.value -> {
                holder.itemView.child_line.visibility = View.VISIBLE
                holder.itemView.child_line.text_view_description.setText(lines[position].description)
                holder.itemView.child_line.text_view_description.isEnabled = isEditable
            }
            LineType.BRACKETS.value -> {
                holder.itemView.brackets_line.visibility = View.VISIBLE
                holder.itemView.brackets_line.text_view_description.setText(lines[position].description)
                holder.itemView.brackets_line.text_view_description.isEnabled = isEditable
            }
            LineType.OTHER_PERSON_LINE.value -> {
                holder.itemView.other_person_line.visibility = View.VISIBLE
                holder.itemView.other_person_line.text_view_description.setText(lines[position].description)
                holder.itemView.other_person_line.text_view_name.setText(lines[position].other_person)
                holder.itemView.other_person_line.text_view_description.isEnabled = isEditable
                holder.itemView.other_person_line.text_view_name.isEnabled = isEditable
            }
        }
    }


    inner class LineHolder(itemView: View) : RecyclerView.ViewHolder(itemView), TextWatcher {



        init {
            //todo remove click
            itemView.child_line.text_view_description.addTextChangedListener(this)
            itemView.brackets_line.text_view_description.addTextChangedListener(this)
            itemView.other_person_line.text_view_description.addTextChangedListener(this)
            itemView.other_person_line.text_view_name.addTextChangedListener(this)

        }

        override fun afterTextChanged(s: Editable?) {
            if (s === itemView.other_person_line.text_view_name.editableText)
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
