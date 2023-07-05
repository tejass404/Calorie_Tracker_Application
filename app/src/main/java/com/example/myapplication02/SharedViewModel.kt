package com.example.myapplication02

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var userName: String? = null
    var Name: String? =null
    var Date:String? =null
    var Cout:String?=null
    var Cin:String?=null
    var Bmr:String?=null
    var NetCal:String?=null

    var summaryList: MutableList<SummarizedViewData> = mutableListOf()
}