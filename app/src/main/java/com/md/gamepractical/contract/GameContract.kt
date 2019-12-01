package com.md.gamepractical.contract

open class GameContract {

    interface Presenter  {
        fun getSignificantWords(list : ArrayList<String> , index : Int ) :  ArrayList<String>

    }
}