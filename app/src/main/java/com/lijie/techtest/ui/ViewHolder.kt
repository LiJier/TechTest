package com.lijie.techtest.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class ViewHolder<V : ViewBinding>(val itemViewBinding: V) :
    RecyclerView.ViewHolder(itemViewBinding.root)