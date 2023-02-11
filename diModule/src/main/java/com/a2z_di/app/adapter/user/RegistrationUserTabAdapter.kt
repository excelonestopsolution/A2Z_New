package com.a2z_di.app.adapter.user

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.a2z_di.app.activity.register.user.RegisterUserListingFragment

class RegistrationUserTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = RegisterUserListingFragment.newInstance(0)
            1 -> fragment = RegisterUserListingFragment.newInstance(1)
        }
        return fragment!!
    }

    override fun getCount()= 2


    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when (position) {
            0 -> title = "Completed"
            1 -> title = "In-Completed"
        }
        return title
    }
}