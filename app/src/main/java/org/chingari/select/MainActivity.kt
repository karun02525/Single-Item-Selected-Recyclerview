package org.chingari.select

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_item.view.*
import org.chingari.select.MainActivity.Companion.checkedPosition
import java.util.HashSet


class MainActivity : AppCompatActivity() {

    private val mAdapter by lazy { OutgoingAdapter() }
    private val list = mutableListOf<OutgoingModel>()
    lateinit var sharedPref: SharedPreferences


    companion object{
        var checkedPosition = -2

        //-2 means default unselects
        //-1 select one
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = this.getSharedPreferences("PREF_NAME", 0)



        list.run {
            add(OutgoingModel(0,"Your Favourite Chingari stars", "See Chingari stars you are following on outgoing calls", true))
            add(OutgoingModel(1,"See Trending Videos on Chingari", "See your favourite Chingari stars you are following on outgoing calls", false))
            add(OutgoingModel(2,"See my home feed videos", "See your favourite Chingari stars you are following on outgoing calls", false))
        }
        mAdapter.list = list
        rv_outgoing.adapter = mAdapter


        //Get sharedPref
        val pos=sharedPref.getInt("position",-2)
        Log.d("TAGS","===> getInt=> $pos")
        checkedPosition=pos
        mAdapter.notifyDataSetChanged()




        btnSubmit.setOnClickListener {
            for (i in list) {
                if (i.isSelected) {
                    Log.d("TAGS","===> id=>  ${i.id} "+i.name +"  And => "+i.isSelected)
                    saveSelectValue(i.id)
                }
            }
        }

    }


    private fun saveSelectValue(position: Int) {
        Log.d("TAGS","===> SaveInt=> $position")
        val editor = sharedPref.edit()
        editor.putInt("position", position)
        editor.apply()
    }

}

 //***************Adapter***************
data class OutgoingModel(val id:Int,val name: String, val mess: String, var isSelected: Boolean)

class OutgoingAdapter(var list: List<OutgoingModel> = listOf()) : RecyclerView.Adapter<OutgoingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
        R.layout.adapter_item, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindItems(model: OutgoingModel) {

            itemView.apply {
               // Glide.with(context).load(model.avatar).into(ivItemGridImage)
                tvFavourite.text=model.name
                tvMess.text=model.mess

                if (checkedPosition == -1) {
                    ivMark.setBackgroundResource(R.drawable.ic_incoming_uncheck)
                    model.isSelected = true
                } else {
                    if (checkedPosition == absoluteAdapterPosition) {
                        ivMark.setBackgroundResource(R.drawable.ic_incoming_checked)
                        model.isSelected = true
                    } else {
                        ivMark.setBackgroundResource(R.drawable.ic_incoming_uncheck)
                        model.isSelected = false
                    }
                }
            }.setOnClickListener {
                model.isSelected = true
                itemView.ivMark.setBackgroundResource(R.drawable.ic_incoming_checked)
                if (checkedPosition != absoluteAdapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = absoluteAdapterPosition
                }
            }

        }
    }
}