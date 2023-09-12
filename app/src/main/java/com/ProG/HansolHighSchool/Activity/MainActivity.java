package com.ProG.HansolHighSchool.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.ProG.HansolHighSchool.Fragment.HomeFragment;
import com.ProG.HansolHighSchool.Fragment.MealFragment;
import com.ProG.HansolHighSchool.Fragment.NoticeFragment;
import com.ProG.HansolHighSchool.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        {
            Intent loading = new Intent(MainActivity.this, LoadingActivity.class);
            startActivity(loading);
        }
        /* 로딩화면 호출 */

        bottomNavigationView = findViewById(R.id.bottomNavi);
        viewPager = findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new MealFragment());
        pagerAdapter.addFragment(new HomeFragment());
        pagerAdapter.addFragment(new NoticeFragment());

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.meal) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.home) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (itemId == R.id.notice) {
                viewPager.setCurrentItem(2);
                return true;
            } else {
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    public static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        public PagerAdapter(@NonNull FragmentManager fragmentManager) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
