package com.dicoding.submissionmystoryapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class MyEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
    private fun init (){
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().length < 8){
                    error = "Password tidak boleh kurang dari 8 karakter"
                } else{
                    error = null
                }
            }

            override fun afterTextChanged(p0: Editable) {

            }

        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}