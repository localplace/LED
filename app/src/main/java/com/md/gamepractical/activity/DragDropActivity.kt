package com.md.gamepractical.activity

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import android.view.DragEvent
import android.widget.*
import com.md.gamepractical.R
import com.md.gamepractical.Utils
import com.md.gamepractical.main.DragShadowBuilder
import com.md.gamepractical.presenters.GamePresenter
import java.lang.StringBuilder


class DragDropActivity : AppCompatActivity() {

    @BindView(R.id.layout_txt_question)
    lateinit var txt_question: LinearLayout;

    @BindView(R.id.nextButton)
    lateinit var nextButton: Button

    @BindView(R.id.answer_1)
    lateinit var answer_1 : TextView

    @BindView(R.id.answer_2)
    lateinit var answer_2: TextView

    @BindView(R.id.answer_3)
    lateinit var answer_3: TextView

    var significantWords = ArrayList<String>();

    var count = 0;

    private var gamePresenter : GamePresenter? = null;

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val i = intent
        val array = i.extras!!.getStringArrayList(Utils.DRAG_DROP)
        setContentView(R.layout.game_activity)
        ButterKnife.bind(this)

        gamePresenter = GamePresenter(this);

        significantWords = gamePresenter!!.getSignificantWords(array, count)

        nextButton.setOnClickListener {
            if(count < array.size) {
                var text = array.get(count);
                significantWords = gamePresenter!!.getSignificantWords(array, count )
                createGame(array, text,count);
                count++;
            }
        }
        var text = array.get(count);
        createGame(array, text,count);
        count++;

    }

    fun addListeners( v : TextView): Boolean {
        val item = ClipData.Item(v.text as? CharSequence)
        val dragData = ClipData(
            v.text as? CharSequence,
            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
            item)
        val myShadow = DragShadowBuilder(v)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v.startDragAndDrop(dragData, myShadow, null, 0)
        } else {
            v.startDrag(dragData, myShadow, null, 0)
        }

        return true

    }

    fun addQuestion(StringText : List<String>){
        txt_question.removeAllViews()
        var question  =  StringBuilder();
        for( m in StringText) {
            if (significantWords.contains(m)) {
                var actualString = question.toString() +" "+m
                question.append("_______________")
                var view = createQuestionView(question.toString())
                view.setPadding(0,10,0,0)
                view.setTag(view.id,actualString);
                view.setOnDragListener(onDragListener)
                txt_question.addView(view)
                question.clear()
            }else {
                question.append(m +" ");
            }
        }
        txt_question.addView(createQuestionView(question.toString()))
    }

    fun  createQuestionView(question : String) : TextView {
        var view = TextView(this);
        view.setText(question)
        view.setTextSize(20f)
        view.setTextColor(Color.BLACK)
        return view
    }

    fun addAnswers(array : List<String>, count : Int) {
        if(significantWords.size >= 3) {
            createAnswer(answer_1,significantWords.get(0))
            createAnswer(answer_2,significantWords.get(1))
            createAnswer(answer_3,significantWords.get(2))
        }else {
            if(significantWords.size >= 2) {
                createAnswer(answer_1, significantWords.get(0))
                createAnswer(answer_2, significantWords.get(1))
                createAnswer(answer_3, "")
            }
        }
    }

    fun createAnswer(answerView : TextView , answers : String) {
        answerView.setText(answers);
        answerView.setTag(answerView.id,answers)
        answerView.setOnLongClickListener {
            addListeners(answerView)
        }
    }

    fun createGame(array : ArrayList<String>, text : String ,count : Int) {
        var StringText = text?.split(" ");
        addQuestion(StringText)
        addAnswers(array, count);
    }

    private val onDragListener = View.OnDragListener { view, dragEvent ->
        (view as? TextView)?.let {
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DROP -> {
                    val item: ClipData.Item = dragEvent.clipData.getItemAt(0)
                    var value = it.getTag(it.id);
                    if(value != null && value.toString().contains(item.text)) {
                        it.setText(value.toString())
                        it.setTextColor(ContextCompat.getColor(this, R.color.green))
                    }
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    return@OnDragListener true
                }
                else -> return@OnDragListener false
            }
        }
        false
    }

}