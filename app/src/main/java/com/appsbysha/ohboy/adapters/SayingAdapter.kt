package com.appsbysha.ohboy.adapters

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appsbysha.ohboy.R
import com.appsbysha.ohboy.adapters.SayingAdapter.SayingHolder
import com.appsbysha.ohboy.database.OhBoyRepository
import com.appsbysha.ohboy.entities.Line
import com.appsbysha.ohboy.entities.Saying
import com.appsbysha.ohboy.interfaces.DataReadyListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

class SayingAdapter(private val activity: Activity?, private val context: Context) : RecyclerView.Adapter<SayingHolder>() {
    private var sayings: List<Saying> = ArrayList()
    private var dob: String? = null
    private var listener: OnItemClickListener? = null
    private var longClickListener: OnLongClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SayingHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.saying_item, parent, false)
        return SayingHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SayingHolder, position: Int) {
        val currentSaying = sayings[position]
        holder.textViewTitle.text = currentSaying.title
        if (currentSaying.title.isNullOrEmpty()) {
            holder.textViewTitle.visibility = View.GONE
        }
        val sayingDate = currentSaying.date
        sayingDate?.let {
            holder.textViewDate.text = sayingDate
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            try {
                val date = sdf.parse(sayingDate)
                val childDob = sdf.parse(dob)
                val cal = Calendar.getInstance()
                cal.time = date
               /* cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);*/
                val calDob = Calendar.getInstance()
                calDob.time = childDob
   /*             calDob.set(Calendar.MILLISECOND, 0);
                calDob.set(Calendar.SECOND, 0);
                calDob.set(Calendar.MINUTE, 0);
                calDob.set(Calendar.HOUR_OF_DAY, 0);*/
                val sayingDate: LocalDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                val dateOfBirth: LocalDate = LocalDate.of(calDob.get(Calendar.YEAR), calDob.get(Calendar.MONTH), calDob.get(Calendar.DAY_OF_MONTH))
                val msdiff = Period.between(dateOfBirth, sayingDate)
                //cal.timeInMillis = msdiff
                val diffYears = msdiff.years// (cal[Calendar.YEAR] - 1970)
                val diffMonths = msdiff.months//cal[Calendar.MONTH]
                val diffDays = msdiff.days//cal[Calendar.DAY_OF_MONTH]
                val age = (if(diffYears != 0) "$diffYears year" else "") +
                        if(diffYears>1)"s" else "" +
                        (if(diffMonths != 0 && diffDays == 0) " and" else "") +
                        (if(diffMonths != 0) " $diffMonths month" else "") +
                         if(diffMonths>1)"s" else "" +
                        (if(diffDays != 0 && (diffMonths != 0 || diffYears != 0)) " and" else "") +
                         (if(diffDays != 0) " $diffDays day" else "") +
                        if(diffDays>1)"s" else "" +
                        " old"
                holder.textViewAge.text =    age
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        if (holder.expand) { //todo expand rv min/max height
            holder.recyclerView.visibility = View.VISIBLE
            holder.expander.setImageDrawable(context.getDrawable(R.drawable.expand_less_foreground))

        } else {
            holder.recyclerView.visibility = View.GONE
            holder.expander.setImageDrawable(context.getDrawable(R.drawable.expand_more_foreground))


        }
        /*      currentSaying.photo?.let {
                   val bitmap = BitmapFactory
                           .decodeByteArray(currentSaying.photo, 0, it.size)
                   holder.photo.setImageBitmap(bitmap)
               }*/

        //todo add loading bar
        OhBoyRepository(activity?.application, currentSaying.childId, currentSaying.sayingId).getAllLinesView(object : DataReadyListener{
            override fun onSuccess(list: List<Line>) {
                super.onSuccess(list)
                holder.adapter.setLines(list as MutableList<Line>, false)
            }
        })

     holder.recyclerView.adapter = holder.adapter
        holder.recyclerView.layoutManager = LinearLayoutManager(context)
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
        val expander: ImageView = itemView.findViewById(R.id.expanderIcon)

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