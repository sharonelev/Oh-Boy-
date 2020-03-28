package com.appsbysha.ohboy.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appsbysha.ohboy.R
import com.appsbysha.ohboy.adapters.SayingAdapter.SayingHolder
import com.appsbysha.ohboy.database.LineViewModel
import com.appsbysha.ohboy.database.LineViewModelFactory
import com.appsbysha.ohboy.database.OhBoyRepository
import com.appsbysha.ohboy.entities.Line
import com.appsbysha.ohboy.entities.Saying
import com.appsbysha.ohboy.interfaces.DataReadyListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SayingAdapter(val activity: Activity?) : RecyclerView.Adapter<SayingHolder>() {
    private var sayings: List<Saying> = ArrayList()
    private var dob: String? = null
    private var listener: OnItemClickListener? = null
    private var longClickListener: OnLongClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SayingHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.saying_item, parent, false)
        return SayingHolder(itemView)
    }

    override fun onBindViewHolder(holder: SayingHolder, position: Int) {
        val currentSaying = sayings[position]
        holder.textViewTitle.text = currentSaying.title
        if (currentSaying.title.isNullOrEmpty()) {
            holder.textViewTitle.visibility = View.GONE
        }
        val sayingDate = currentSaying.date
        sayingDate?.let {
            holder.textViewDate.text = sayingDate
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            try {
                val date = sdf.parse(sayingDate)
                val childDob = sdf.parse(dob)
                val cal = Calendar.getInstance()
                cal.time = date
                val calDob = Calendar.getInstance()
                calDob.time = childDob
                val msdiff = cal.timeInMillis - calDob.timeInMillis
                cal.timeInMillis = msdiff
                val diffYears = (cal[Calendar.YEAR] - 1970).toString()
                val diffMonths = cal[Calendar.MONTH].toString()
                val diffDays = cal[Calendar.DAY_OF_MONTH].toString()
                holder.textViewAge.text = "$diffYears years, $diffMonths months and $diffDays days old"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        if (holder.expand) { //todo expand rv min/max height
            //    holder.textViewDescription.maxLines = Int.MAX_VALUE
            //   holder.textViewDescription.ellipsize = null
        } else {
            //  holder.textViewDescription.maxLines = 3
            // holder.textViewDescription.ellipsize = TruncateAt.END
        }
        /*      currentSaying.photo?.let {
                   val bitmap = BitmapFactory
                           .decodeByteArray(currentSaying.photo, 0, it.size)
                   holder.photo.setImageBitmap(bitmap)
               }*/

        //todo add loading bar
        OhBoyRepository(activity!!.application, currentSaying.childId, currentSaying.sayingId).getAllLinesView(object : DataReadyListener{
            override fun onSuccess(list: List<Line>) {
                super.onSuccess(list)
                holder.adapter.setLines(list as MutableList<Line>, false)
            }
        })

     holder.recyclerView.adapter = holder.adapter
        holder.recyclerView.layoutManager = LinearLayoutManager(activity)
        holder.recyclerView.setHasFixedSize(true)


    }

    override fun getItemCount(): Int {
        return sayings.size
    }

    fun setSayings(sayings: List<Saying>, dob: String?) {
        this.sayings = sayings
        this.dob = dob
        notifyDataSetChanged()
    }

    inner class SayingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.linesRecyclerView)
        val adapter: LineAdapter = LineAdapter()
        val textViewDate: TextView = itemView.findViewById(R.id.text_view_date)
        val textViewAge: TextView = itemView.findViewById(R.id.text_view_dob)

        //  val photo: ImageView = itemView.findViewById(R.id.child_image)
        var expand = false

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                listener?.let {
                    if (position != RecyclerView.NO_POSITION) {
                        it.onItemClick(sayings[position])
                    }
                }
                expand = !expand
            }
            itemView.setOnLongClickListener {
                val position = adapterPosition
                longClickListener?.let {
                    if (position != RecyclerView.NO_POSITION) {
                        it.onLongClick(sayings[position])
                    }
                }
                false
            }

        }
    }

    fun getSayingAt(position: Int): Saying {
        return sayings[position]
    }

    interface OnItemClickListener {
        fun onItemClick(saying: Saying?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnLongClickListener {
        fun onLongClick(saying: Saying?)
    }

    fun setLongClickListener(longClickListener: OnLongClickListener?) {
        this.longClickListener = longClickListener
    }
}