package com.example.matchmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matchmaker.Fragments.Adapter.ViewPagerAdapter
import com.example.matchmaker.Fragments.Chats
import com.example.matchmaker.Fragments.HomeFragment
import com.example.matchmaker.Fragments.Matches
import com.example.matchmaker.Fragments.Profile
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpTabs()

    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(),"")
        adapter.addFragment(Chats(),"")
        adapter.addFragment(Matches(),"")
        adapter.addFragment(Profile(),"")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)



        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_chat_bubble_24)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_favorite_24)
        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_person_24)
    }
}