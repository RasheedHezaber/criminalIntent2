package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat



class CrimeListFragment : Fragment() {
    private val crimeListViewModel: CrimeListViweModle by lazy {
        ViewModelProviders.of(this).get(CrimeListViweModle::class.java)
    }
    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        updateUI()

        return view
    }

    private fun updateUI() {
        val crimes =
            crimeListViewModel.crimes
        adapter = CrimeAdapter (crimes)
        crimeRecyclerView .adapter = adapter
    }
    abstract open  class CrimeHolder(view: View) : RecyclerView.ViewHolder(view){
        abstract open fun bind(item:Crime)

    }
    private inner class CrimeViweHolder(view: View) :  CrimeHolder(view), View.OnClickListener {
        private lateinit var crime: Crime
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)


        init { itemView.setOnClickListener(this) }



        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }
        override fun bind(crime: Crime) {

            this.crime = crime
           titleTextView.setText(crime.title)
           dateTextView.setText(crime.date.toString())
            solvedImageView.visibility=if(crime.isSolved){
                View.VISIBLE
            }
            else
                View.GONE }
    }


    private  inner class SecondCrimeHolder(view: View) : CrimeHolder(view){
        private lateinit var crime: Crime
        val requiredTextView: TextView = itemView.findViewById(R.id.rcrime_title)
        val requireddateTextView: TextView = itemView.findViewById(R.id.rcrime_date)


        override fun bind(crime: Crime) {
            this.crime = crime

            requiredTextView.setText(crime.title)
            requireddateTextView.text= DateFormat.getDateInstance(DateFormat.FULL).format(this.crime.date).toString()
        }

    }
    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val requiredCrime = 1
        val normalCrime = 2


        override fun getItemViewType(position: Int): Int {
            return if (crimes[position].requiredPolice == 1)
                return requiredCrime
            else
                return normalCrime

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: View?
            var recyclerViewholder: RecyclerView.ViewHolder
            when (viewType) {
                requiredCrime -> {

                    val view = layoutInflater.inflate(
                        R.layout.list_item_police,
                        parent,
                        false
                    )

                    recyclerViewholder = SecondCrimeHolder(view)
                }
                else -> {
                    val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                    recyclerViewholder = CrimeViweHolder(view)
                }

            }
            return recyclerViewholder

        }

        override fun getItemCount(): Int {
            return crimes.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val crime = crimes[position]
            if (holder is SecondCrimeHolder)
                holder.bind(crime)
            else
                if(holder is CrimeViweHolder)
                    holder.bind(crime)

        }
    }
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}
