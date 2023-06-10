package com.example.species

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.example.species.databinding.ActivityIntroBinding
import com.example.species.databinding.ActivitySignInBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    lateinit var preference : SharedPreferences
    val pref_show_intro = "Intro"

    private val introSlideAdapter = IntroSlideAdapter(
        listOf(
            IntroSlide(
                "Identify Plants",
                "You can identify the plants you don't know through your camera",
                R.drawable.intro1
            ),
            IntroSlide(
                "Learn Many Plants Species",
                "Let's learn about the many plant species that exist in this world",
                R.drawable.intro2
            ),
            IntroSlide(
                "Read Many Articles About Plant",
                "Let's learn more about beautiful plants and read many articles about plants and gardening",
                R.drawable.intro3
            )
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = getSharedPreferences("IntroSlider", Context.MODE_PRIVATE)

        if (!preference.getBoolean(pref_show_intro, true)) {
            Intent(applicationContext, SignInActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        binding.introSliderViewPager.adapter = introSlideAdapter
        setupIndicator()
        setCurrentIndicator(0)
        binding.introSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
            })
        binding.buttonNext.setOnClickListener {
            if (binding.introSliderViewPager.currentItem + 1 < introSlideAdapter.itemCount) {
                binding.introSliderViewPager.currentItem += 1
            } else {
                Intent(applicationContext, SignInActivity::class.java).also{
                    startActivity(it)
                    finish()
                    val editor = preference.edit()
                    editor.putBoolean(pref_show_intro, false)
                    editor.apply()
                }
            }
        }
    }

    private fun setupIndicator() {
        val indicators = arrayOfNulls<ImageView>(introSlideAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorsContainer.get(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }
}