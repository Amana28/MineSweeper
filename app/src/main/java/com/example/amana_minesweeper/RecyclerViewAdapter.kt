package com.example.amana_minesweeper

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private var cells: List<Cell>, private var listener: ListenerInterface) :
    RecyclerView.Adapter<RecyclerViewAdapter.MineGridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MineGridViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.minesweeper_cell, parent, false)
        return MineGridViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MineGridViewHolder, position: Int) {
        holder.bind(cells[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return cells.size
    }

    fun setCells(cells: List<Cell>) {
        this.cells = cells
        notifyDataSetChanged()
    }

    inner class MineGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var valueTextView: TextView = itemView.findViewById(R.id.cell_value)

        fun bind(cell: Cell) {
            itemView.setBackgroundColor(Color.GRAY)

            itemView.setOnClickListener {
                listener.clickCell(cell)
            }

            if (cell.isRevealed()) {
                if (cell.getValue() == Cell.BOMB) {
                    valueTextView.setText(R.string.bomb)
                } else if (cell.getValue() == Cell.EMPTY) {
                    valueTextView.setText("")
                    itemView.setBackgroundColor(Color.WHITE)
                } else {
                    valueTextView.setText(cell.getValue().toString())
                    when (cell.getValue()) {
                        1 -> valueTextView.setTextColor(Color.BLUE)
                        2 -> valueTextView.setTextColor(Color.GREEN)
                        3 -> valueTextView.setTextColor(Color.RED)
                    }
                }
            } else if (cell.isFlagged()) {
                valueTextView.setText(R.string.flag)
            }
        }
    }
}
