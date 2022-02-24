package com.example.matchmaker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matchmaker.Fragments.HomeFragment
import com.example.matchmaker.Fragments.Matches
import com.example.matchmaker.Fragments.UserDetails
import kotlinx.android.synthetic.main.match_item_rows.view.*
import kotlinx.android.synthetic.main.match_member_card.view.*
import kotlinx.android.synthetic.main.member_list.view.*

class UserAdapterMAtchesOne(private val fragment: Fragment, val likeCatagory: List<LikeCatagory>,val onClickListnerMatch: OnClickListnerMatch)
    :RecyclerView.Adapter<UserAdapterMAtchesOne.ViewHolderOne>(){

    inner class ViewHolderOne (itemView: View): RecyclerView.ViewHolder(itemView){

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind (likeCatagories:LikeCatagory){
            itemView.cat_title!!.text = likeCatagories.liketype

        }

        val itemRecycler:RecyclerView
        init {
            itemRecycler =itemView.findViewById(R.id.rvRowItems)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOne {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.match_item_rows, parent,false)
        return ViewHolderOne(view)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolderOne, position: Int) {
        holder.bind(likeCatagory[position])
        holder.itemView.setOnClickListener{
            onClickListnerMatch.onListItemClickListnerTwo(position)
        }
        setMatchItemRecycler(holder.itemRecycler, likeCatagory[position].userListMatches )

    }

    override fun getItemCount()= likeCatagory.size

    private fun setMatchItemRecycler(recyclerView: RecyclerView, users:List<User> ){

        recyclerView.adapter = MatchItemAdapter(fragment,users,onClickListnerMatch)
        recyclerView.isClickable=true
        recyclerView.layoutManager = LinearLayoutManager(fragment.context , RecyclerView.HORIZONTAL,false)


    }





}