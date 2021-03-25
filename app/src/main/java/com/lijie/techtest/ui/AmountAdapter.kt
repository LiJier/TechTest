package com.lijie.techtest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lijie.techtest.R
import com.lijie.techtest.databinding.LayoutAmountItemBinding
import com.lijie.techtest.entity.YearAmountData

class AmountAdapter(var amountDataList: List<YearAmountData>) :
    RecyclerView.Adapter<ViewHolder<LayoutAmountItemBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<LayoutAmountItemBinding> {
        val viewBinding =
            LayoutAmountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder<LayoutAmountItemBinding>, position: Int) {
        with(holder.itemViewBinding) {
            val yearAmountData = amountDataList[position]
            yearTextView.text = yearAmountData.year.toString()
            if (yearAmountData.hasDecline) {
                declineImageView.visibility = View.VISIBLE
                declineImageView.setOnClickListener {
                    Toast.makeText(
                        root.context,
                        R.string.has_decline_data,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                declineImageView.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return amountDataList.size
    }

}