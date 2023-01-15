package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
//import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import org.json.JSONArray
import org.json.JSONObject

class ViewPagerAdapter(val context: Context) : PagerAdapter() {

    var layoutInflater: LayoutInflater? = null


    val headArray = arrayOf(
        "JOIN IN!!!",
        "SEARCHING SOMEONE??",
        "  "
    )

    val arrayDescription = arrayOf(
        "Login To Access Member Data And Explore More",
        "Search Or Sort Based On\nYour Priority",
        "  "
    )

    override fun getCount(): Int {
        return headArray.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view == `object` as LinearLayout
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.slide,container,false)


        val txthead = view.findViewById<TextView>(R.id.txthead)
        val txtdescription = view.findViewById<TextView>(R.id.txtdescription)


        txthead.text = headArray[position]
        txtdescription.text = arrayDescription[position]

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
