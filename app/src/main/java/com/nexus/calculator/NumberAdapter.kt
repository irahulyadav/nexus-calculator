package com.nexus.calculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class NumberAdapter(val recyclerListener: RecyclerViewListener) : RecyclerView.Adapter<NumberHolder>() {
    private val list = arrayListOf(
        arrayListOf('9', '8', '7', '/'),
        arrayListOf('6', '5', '4', '+'),
        arrayListOf('3', '2', '1', '-'),
        arrayListOf('C', '0', '.', '*'),
        arrayListOf(' ', '(', ')', '=')
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberHolder {
        return NumberHolder.get(parent, recyclerListener)
    }

    override fun getItemCount(): Int {
        var count = 0
        list.forEach { t: ArrayList<Char>? -> count += t!!.size }
        return count
    }

    private fun getPosition(position: Int): Pair<Int, Int> {
        return Pair(position / 4, position % 4)
    }

    public fun getPosition(char: Char): Int {
        if (char != ' ') {
            var i = 0
            list.forEach {
                it.forEach {
                    if (char == it) {
                        return i
                    }
                    i++
                }
            }
        }
        return -1
    }

    fun getItem(position: Int): NumberItem {
        val pair = getPosition(position)
        return NumberItem(list[pair.first][pair.second])
    }

//    fun getSpanSize(position: Int): Int {
//        val pair = getPosition(position)
//        return list[pair.first].size
//    }

    override fun onBindViewHolder(holder: NumberHolder, position: Int) {
        holder.item = getItem(position)

    }
}

class NumberHolder(view: View, val recyclerListener: RecyclerViewListener) : RecyclerView.ViewHolder(view) {

    companion object {
        fun get(parent: ViewGroup, recyclerListener: RecyclerViewListener): NumberHolder {
            return NumberHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.button_item, parent, false),
                recyclerListener
            )
        }
    }

    val button = itemView.findViewById<AppCompatButton>(R.id.button)

    init {
        button.setOnClickListener {
            recyclerListener.onItemClick(this, item!!)
        }

        itemView.setOnClickListener { button.performClick() }

        button.setOnLongClickListener {
            recyclerListener.onItemLongClick(this, item!!)
            return@setOnLongClickListener true
        }

    }

    var item: NumberItem? = null
        set(item) {
            if (item != null) {
                button.text = if (item.isBack) "<<" else item.value.toString()
                field = item
            }
        }

}

class NumberItem(val value: Char) {

    val isNumber: Boolean
        get() = value == '.' || isOnlyNumber

    val isOnlyNumber: Boolean
        get() = value in '0'..'9'

    val isOperation: Boolean
        get() = value == '*' || value == '/' || value == '-' || value == '+'

    val isClean: Boolean
        get() = value == 'C'

    val isBack: Boolean
        get() = value == ' '

    val showResult: Boolean
        get() = value == '='

    val type: NumberType
        get() {
            if (isOperation)
                return NumberType.operation
            if (isClean)
                return NumberType.clean
            if (isBack)
                return NumberType.back
            if (showResult)
                return NumberType.result
            return NumberType.number
        }

}

enum class NumberType {
    number, operation, clean, back, result
}


interface RecyclerViewListener : RecyclerView.RecyclerListener {
    fun onItemClick(holder: NumberHolder, value: NumberItem)

    fun onItemLongClick(holder: NumberHolder, value: NumberItem)

    override fun onViewRecycled(holder: RecyclerView.ViewHolder)
}

