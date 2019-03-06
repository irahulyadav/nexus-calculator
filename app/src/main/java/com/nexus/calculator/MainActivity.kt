package com.nexus.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerViewListener {

    val expression = Expression()
    val numberAdapter = NumberAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(recyclerView) {
            layoutManager = GridLayoutManager(context, 4)
            adapter = numberAdapter
        }
    }

    override fun onItemClick(holder: NumberHolder, value: NumberItem) {
        expression.add(info.text.toString(), value) { text ->
            info.text = text
        }
    }

    override fun onItemLongClick(holder: NumberHolder, value: NumberItem) {
        if (value.value == ' ') {
            onItemClick(holder, NumberItem('C'))
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {

    }

    companion object {
        var expressionCount = 0
        fun addNumber(text: String, value: NumberItem) {

        }
    }
}

