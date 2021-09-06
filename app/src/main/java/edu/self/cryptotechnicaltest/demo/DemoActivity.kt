package edu.self.cryptotechnicaltest.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.self.cryptotechnicaltest.R
import edu.self.cryptotechnicaltest.currencylist.CurrencyListFragment
import edu.self.cryptotechnicaltest.currencylist.CurrencyListViewModel
import edu.self.cryptotechnicaltest.currencylist.toCurrencyListFragmentResponse
import edu.self.cryptotechnicaltest.databinding.ActivityDemoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoBinding

    private val viewModel: DemoViewModel by viewModel()

    private val currencyListViewModel: CurrencyListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vSortButton.setOnClickListener {
            //DemoActivity should provide 2 buttons to do the demo.
            //Second button for sorting currency list
            currencyListViewModel.sort()
        }

        binding.vLoadDataButton.setOnClickListener {
            //DemoActivity should provide 2 buttons to do the demo.
            //First button to load the data and display
            viewModel.loadData {
                currencyListViewModel.loadData(this)
            }
        }

        viewModel.page.observe(this) {
            val snapshot = supportFragmentManager
                .findFragmentById(R.id.vFrame)
            if (snapshot == null) {
                val fragment = CurrencyListFragment.newInstance(it)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.vFrame, fragment, null)
                    .commit()
            }
            //CurrencyListFragment should provide a hook of item click listener to the parent
            //then the parent, this activity will listen the delegation from CurrencyListFragment
            //to know which item is clicked(in this case)
            supportFragmentManager.setFragmentResultListener(it.id, this) { _, extras ->
                val response = extras.toCurrencyListFragmentResponse()
                if (response != null) {
                    val clickedModel = response.clickedItem
                    val message = "clickedModel=$clickedModel"
                    //here we know the model of clicked item
                    //just toast it here
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}