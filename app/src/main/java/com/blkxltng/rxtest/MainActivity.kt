package com.blkxltng.rxtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

const val LOG_TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        initLayout()
    }

    private fun initLayout() {
        sugar()
//        noSugar()
        map()
        mapAndFilter()
    }

    private fun noSugar() {
        val emitter = PublishSubject.create<View>()
        button.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                emitter.onNext(v!!)
            }
        })

        val consumer = object: Consumer<View> {
            override fun accept(t: View?) {
                incrementCounter2()
            }
        }

        emitter.map(object: Function<View, View> {
            override fun apply(t: View): View {
                incrementCounter1()
                return t
            }
        })
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            .subscribe(consumer)
    }

    private fun sugar() {
        button.clicks()
            .map {
                incrementCounter1()
            }
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            .subscribe {
                incrementCounter2()
            }
    }

    private fun incrementCounter1() {
        var newVal = counter1.text.toString().toInt()
        newVal++
        counter1.text = newVal.toString()
    }

    private fun incrementCounter2() {
        var newVal = counter2.text.toString().toInt()
        newVal++
        counter2.text = newVal.toString()
    }

    private fun map() {
        Observable.just(1, 3, 10, 20)
            .map {
                it * 3
            }
            .subscribe {
                Log.i(LOG_TAG, "Number is $it")
            }
    }

    private fun mapAndFilter() {
        Observable.just(1, 3, 10, 20)
            .map {
                it * 3
            }
            .filter {
                it % 2 == 0
            }
            .subscribe {
                Log.i(LOG_TAG, "Filtered number is $it")
            }
    }
}
