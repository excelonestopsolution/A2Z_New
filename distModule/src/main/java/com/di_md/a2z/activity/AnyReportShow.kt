package com.di_md.a2z.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.di_md.a2z.R
import com.di_md.a2z.fragment.MatmReportFragment
import com.di_md.a2z.fragment.ReportAepsFragment
import com.di_md.a2z.fragment.report.LedgerReportFragment
import com.di_md.a2z.util.AppConstants
import com.di_md.a2z.util.ents.setupToolbar
import com.di_md.a2z.util.enums.ReportTypeName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnyReportShow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_any_report_show)

       // setFragment(ReportAepsFragment.Companion.newInstance())



        val reportName = intent.getSerializableExtra(AppConstants.DATA) as ReportTypeName

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        when(reportName){
            ReportTypeName.LADGER_REPORT -> {
                setupToolbar(toolbar, "Ledger Report")
                setFragment(LedgerReportFragment.Companion.newInstance())
            }
            ReportTypeName.AEPS_REPORT -> {
                setupToolbar(toolbar, "AEPS Report")
                setFragment(ReportAepsFragment.Companion.newInstance())
            }
            ReportTypeName.MATAM_REPORT -> {
                setupToolbar(toolbar, "Matm/Mpos Report")
                setFragment(MatmReportFragment.Companion.newInstance())
            }
            //ReportTypeName.PAYMENTGATEWAY_REPORT -> setFragment(LedgerReportFragment.Companion.newInstance())
            else -> {}
        }

    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frag_container, fragment).commit()
    }
}