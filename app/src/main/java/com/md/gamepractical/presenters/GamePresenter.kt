package com.md.gamepractical.presenters

import com.md.gamepractical.Utils
import com.md.gamepractical.activity.DragDropActivity
import com.md.gamepractical.contract.GameContract.Presenter

class GamePresenter (var view: DragDropActivity) : Presenter {


    override fun getSignificantWords(list : ArrayList<String> , index : Int ) : ArrayList<String> {
        var significantWords = ArrayList<String>();
        var updatedSignificantWords = ArrayList<String>();
        var splitAnswers= list.get(index).split(" ") ;

        for(answer in splitAnswers) {
            if (answer.length > 4 && !Utils.getStopWords(answer.toLowerCase())) {
                significantWords.add(answer)
            }
        }
        significantWords.shuffle()

        if(significantWords.size >= 3) {
            for( i in 0..2) {
                updatedSignificantWords.add(significantWords.get(i))
            }
        }else if (significantWords.size >= 2) {
            for (i in 0..1) {
                updatedSignificantWords.add(significantWords.get(i))
            }
        } else {
            updatedSignificantWords.add("No Significant Words, Skip")
        }

        return updatedSignificantWords;

    }


}