package com.example.app;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class TutorialActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPageZero = new SliderPage();
        sliderPageZero.setTitle("Editor App");
        sliderPageZero.setDescription("Take Pictures. Write Text. Share with friends!");
        sliderPageZero.setImageDrawable(R.drawable.logo);
        sliderPageZero.setBgColor(getResources().getColor(R.color.sky_blue));
        addSlide(AppIntroFragment.newInstance(sliderPageZero));

        SliderPage sliderPageOne = new SliderPage();
        sliderPageOne.setTitle("First Time User");
        sliderPageOne.setDescription("Click on + icon to take picture");
        sliderPageOne.setImageDrawable(R.drawable.one);
        sliderPageOne.setBgColor(getResources().getColor(R.color.cyan));
        addSlide(AppIntroFragment.newInstance(sliderPageOne));


        SliderPage sliderPageTwo = new SliderPage();
        sliderPageTwo.setTitle("Take picture");
        sliderPageTwo.setDescription("Click on round button to snap a picture");
        sliderPageTwo.setImageDrawable(R.drawable.four);
        sliderPageTwo.setBgColor(getResources().getColor(R.color.blush));
        addSlide(AppIntroFragment.newInstance(sliderPageTwo));

        SliderPage sliderPageThree = new SliderPage();
        sliderPageThree.setTitle("Edit picture");
        sliderPageThree.setDescription("Write on the picture with bottom options and save");
        sliderPageThree.setImageDrawable(R.drawable.five);
        sliderPageThree.setBgColor(getResources().getColor(R.color.sky_blue));
        addSlide(AppIntroFragment.newInstance(sliderPageThree));

        SliderPage sliderPageFour = new SliderPage();
        sliderPageFour.setTitle("Delete Picture");
        sliderPageFour.setDescription("Delete a picture by holding it for long");
        sliderPageFour.setImageDrawable(R.drawable.three);
        sliderPageFour.setBgColor(getResources().getColor(R.color.green));
        addSlide(AppIntroFragment.newInstance(sliderPageFour));

        SliderPage sliderPageFive = new SliderPage();
        sliderPageFive.setTitle("View Pictures");
        sliderPageFive.setDescription("View all pictures here or in gallery");
        sliderPageFive.setImageDrawable(R.drawable.two);
        sliderPageFive.setBgColor(getResources().getColor(R.color.purple));
        addSlide(AppIntroFragment.newInstance(sliderPageFive));

        showSkipButton(true);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}