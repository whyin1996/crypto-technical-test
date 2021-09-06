package edu.self.cryptotechnicaltest.currencylist

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import edu.self.cryptotechnicaltest.databinding.FragmentCurrencyListBinding
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CurrencyListFragment : Fragment() {

    @Parcelize
    data class Request(val id: String = UUID.randomUUID().toString()) : Parcelable

    companion object {
        private const val ARG_REQUEST = "ARG_REQUEST"
        fun newInstance(request: Request): CurrencyListFragment {
            return CurrencyListFragment().apply {
                arguments = bundleOf(ARG_REQUEST to request)
            }
        }
    }

    private lateinit var binding: FragmentCurrencyListBinding

    private val viewModel: CurrencyListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCurrencyListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
    }
}