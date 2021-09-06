package edu.self.cryptotechnicaltest.currencylist

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import edu.self.cryptotechnicaltest.core.database.model.CurrencyInfo
import edu.self.cryptotechnicaltest.core.event.observeEvent
import edu.self.cryptotechnicaltest.databinding.FragmentCurrencyListBinding
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class CurrencyListFragment : Fragment() {

    @Parcelize
    data class Request(val id: String = UUID.randomUUID().toString()) : Parcelable

    @Parcelize
    data class Response(val clickedItem: CurrencyInfo) : Parcelable

    companion object {
        private const val ARG_REQUEST = "ARG_REQUEST"
        const val ARG_RESPONSE = "ARG_RESPONSE"
        fun newInstance(request: Request): CurrencyListFragment {
            return CurrencyListFragment().apply {
                arguments = bundleOf(ARG_REQUEST to request)
            }
        }
    }

    private val request: Request?
        get() = arguments?.getParcelable(ARG_REQUEST)

    private lateinit var binding: FragmentCurrencyListBinding

    private val viewModel: CurrencyListViewModel by sharedViewModel {
        parametersOf(request)
    }

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
        with(binding.vList) {
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = CurrencyListAdapter {
                viewModel.onAdapterDelegate(this)
            }
        }
        viewModel.listOfVM.observe(viewLifecycleOwner) {
            (binding.vList.adapter as? CurrencyListAdapter?)?.submitList(it)
        }
        viewModel.actionOfCommit.observeEvent(viewLifecycleOwner) {
            val (request, response) = it
            //CurrencyListFragment should provide a hook of item click listener to the parent
            //here we are using FragmentResultApi to delegate the desired response to the fragment caller
            //no more setTargetFragment or setItemCallback
            //these solution cannot survive in all lifecycle case
            setFragmentResult(request.id, bundleOf(ARG_RESPONSE to response))
        }
    }
}

fun Bundle.toCurrencyListFragmentResponse(): CurrencyListFragment.Response? {
    return getParcelable(CurrencyListFragment.ARG_RESPONSE)
}