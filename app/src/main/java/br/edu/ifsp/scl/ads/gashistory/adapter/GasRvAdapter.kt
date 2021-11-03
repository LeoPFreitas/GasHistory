package br.edu.ifsp.scl.ads.gashistory.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.ads.gashistory.OnGasClickListener
import br.edu.ifsp.scl.ads.gashistory.R
import br.edu.ifsp.scl.ads.gashistory.databinding.LayoutGasBinding
import br.edu.ifsp.scl.ads.gashistory.model.Gas
import java.util.*

class GasRvAdapter(
    private val onGasClickListener: OnGasClickListener,
    private val gasList: MutableList<Gas>
) : RecyclerView.Adapter<GasRvAdapter.GasLayoutHolder>() {
    var elementPosition = -1

    inner class GasLayoutHolder(layoutGasBinding: LayoutGasBinding) :
        RecyclerView.ViewHolder(layoutGasBinding.root), View.OnCreateContextMenuListener {
        val gasValueTv: TextView = layoutGasBinding.gasValueTv
        val dateTv: TextView = layoutGasBinding.dateTv

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_main, menu)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GasLayoutHolder {
        val layoutGasBinding =
            LayoutGasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GasLayoutHolder(layoutGasBinding)
    }

    override fun onBindViewHolder(holder: GasLayoutHolder, position: Int) {
        val gas = gasList[position]

        with(holder) {
            gasValueTv.text = gas.value.toString()
            dateTv.text = toPrettyDate(gas.date)
            itemView.setOnClickListener {
                onGasClickListener.onGasClick(position)
            }
            itemView.setOnLongClickListener {
                elementPosition = position
                false
            }
        }
    }

    override fun getItemCount(): Int = gasList.size

    private fun toPrettyDate(date: Date): String {
        return date.date.toString() + "/" + date.month.toString() + "/" + date.year.toString()
    }
}
