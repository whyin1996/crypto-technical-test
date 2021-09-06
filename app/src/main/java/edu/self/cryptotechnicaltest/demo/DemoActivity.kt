package edu.self.cryptotechnicaltest.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.self.cryptotechnicaltest.R
import edu.self.cryptotechnicaltest.core.event.observeEvent
import edu.self.cryptotechnicaltest.currencylist.CurrencyListFragment
import edu.self.cryptotechnicaltest.databinding.ActivityDemoBinding
import org.koin.android.ext.android.inject

class DemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoBinding

    private val viewModel: DemoViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vSortButton.setOnClickListener {
            viewModel.sort()
        }

        binding.vLoadDataButton.setOnClickListener {
            viewModel.loadData()
        }

        viewModel.actionOfInit.observeEvent(this) {
            val request = CurrencyListFragment.Request()
            val fragment = CurrencyListFragment.newInstance(request)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.vFrame, fragment, null)
                .commit()
        }


    }
}