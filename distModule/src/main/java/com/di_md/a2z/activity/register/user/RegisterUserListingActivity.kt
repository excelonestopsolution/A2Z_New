package com.di_md.a2z.activity.register.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.di_md.a2z.adapter.user.RegistrationUserTabAdapter
import com.di_md.a2z.databinding.ActivityRegisterUserBinding
import com.di_md.a2z.util.ents.setupToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterUserListingActivity : AppCompatActivity() {


    private lateinit var binding : ActivityRegisterUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar,"Users Created By You")


        renderViewPager()
    }

    private fun renderViewPager() {

        val viewPagerAdapter = RegistrationUserTabAdapter(supportFragmentManager)
        binding.viewpager.adapter = viewPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewpager)
    }

}