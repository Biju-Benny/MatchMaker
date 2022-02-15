package com.example.matchmaker

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.match_member_card.view.*
import kotlinx.android.synthetic.main.member_list.view.*

class MatchItemAdapter(val fragment: Fragment, val matchUserList : List<User>):
    RecyclerView.Adapter<MatchItemAdapter.ViewHolderMatchItem>() {


    @RequiresApi(Build.VERSION_CODES.O)
    class ViewHolderMatchItem(itemView:View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.matchTvFN!!.text = user.firstName
            Glide.with(itemView).load(user.profilePicUrl).into(itemView.matchIV)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMatchItem {
        return ViewHolderMatchItem(LayoutInflater.from(fragment.context).inflate(R.layout.match_member_card,parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolderMatchItem, position: Int) {
        holder.bind(matchUserList[position])
    }

    override fun getItemCount()= matchUserList.size



}