package com.example.myapplication

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class  FirstScreen : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_screen)
        supportActionBar?.hide()


        val relativeLayout = findViewById<RelativeLayout>(R.id.layout1)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        val btn = findViewById<Button>(R.id.button)
        val anim1 = findViewById<RelativeLayout>(R.id.xyz)
        val anim2 = findViewById<RelativeLayout>(R.id.abc)
        val anim3 = findViewById<RelativeLayout>(R.id.anim3)



        val animationDrawable = relativeLayout.background as AnimationDrawable
        addAnimation(animationDrawable)

        viewPager=findViewById(R.id.viewpager)
        viewPagerAdapter= ViewPagerAdapter( this)
        viewPager.adapter =viewPagerAdapter


        dotsIndicator.setViewPager(viewPager)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                btn.visibility = View.INVISIBLE
                anim1.visibility= View.INVISIBLE
                anim2.visibility= View.INVISIBLE
                anim3.visibility= View.INVISIBLE
                dotsIndicator.visibility=View.VISIBLE

                if(position==0){
                    relativeLayout.setBackgroundResource(R.drawable.gradient_animation)
                    val animationDrawable1 = relativeLayout.background as AnimationDrawable
                    addAnimation(animationDrawable1)
                    anim1.visibility= View.VISIBLE
                }
                if(position==1){
                    relativeLayout.setBackgroundResource(R.drawable.gradient_animation2)
                    val animationDrawable2 = relativeLayout.background as AnimationDrawable
                    addAnimation(animationDrawable2)
                    anim2.visibility= View.VISIBLE
                }


                if(position==2){
                    relativeLayout.setBackgroundResource(R.drawable.gradient_animations3)
                    val animationDrawable3 = relativeLayout.background as AnimationDrawable
                    addAnimation(animationDrawable3)
                    btn.visibility = View.VISIBLE
                    anim3.visibility= View.VISIBLE
                    dotsIndicator.visibility=View.INVISIBLE
                    btn.setOnClickListener{
                        val intent= Intent(this@FirstScreen,MainActivity::class.java)
                        startActivity(intent)
                    }

                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        }
        )
    }
    fun addAnimation (animationDrawable: AnimationDrawable){
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(1000)
        animationDrawable.start()
    }
}