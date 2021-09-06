package edu.self.cryptotechnicaltest.currencylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.self.cryptotechnicaltest.databinding.ItemCurrencyInfoBinding

class CurrencyListAdapter(val onItemClick: Int.() -> Unit) :
    ListAdapter<CurrencyListAdapter.CurrencyInfoModel, CurrencyListAdapter.ViewHolder>(DIFF_UTIL) {

    data class CurrencyInfoModel(
        val id: String,
        val prefix: CharSequence?,
        val name: CharSequence?,
        val symbol: CharSequence?
    ) {
        data class Payload(
            val isPrefixChanged: Boolean,
            val isNameChanged: Boolean,
            val isSymbolChanged: Boolean
        )

        companion object {
            fun buildPayload(old: CurrencyInfoModel, new: CurrencyInfoModel): Payload {
                return Payload(
                    isNameChanged = old.name != new.name,
                    isPrefixChanged = old.prefix != new.prefix,
                    isSymbolChanged = old.symbol != new.symbol
                )
            }
        }
    }

    companion object {

        private val DIFF_UTIL = object : DiffUtil.ItemCallback<CurrencyInfoModel>() {
            override fun areItemsTheSame(
                oldItem: CurrencyInfoModel,
                newItem: CurrencyInfoModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CurrencyInfoModel,
                newItem: CurrencyInfoModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(
                oldItem: CurrencyInfoModel,
                newItem: CurrencyInfoModel
            ): Any {
                return CurrencyInfoModel.buildPayload(oldItem, newItem)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemCurrencyInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(model: CurrencyInfoModel?, position: Int, payloads: List<Any>) {
            model ?: return
            val payload = payloads.firstOrNull {
                it is CurrencyInfoModel.Payload
            } as? CurrencyInfoModel.Payload?
            if (payload == null) {
                bindPrefix(model)
                bindName(model)
                bindSymbol(model)
            } else {
                if (payload.isNameChanged) {
                    bindName(model)
                }
                if (payload.isPrefixChanged) {
                    bindPrefix(model)
                }
                if (payload.isSymbolChanged) {
                    bindSymbol(model)
                }
            }
        }

        fun unBind() {
            bindPrefix(null)
            bindName(null)
            bindSymbol(null)
        }

        private fun bindPrefix(model: CurrencyInfoModel?) {
            binding.vPrefix.text = model?.prefix
        }

        private fun bindName(model: CurrencyInfoModel?) {
            binding.vName.text = model?.name
        }

        private fun bindSymbol(model: CurrencyInfoModel?) {
            binding.vSymbol.text = model?.symbol
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCurrencyInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position, listOf())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bind(getItem(position), position, payloads)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.unBind()
    }
}