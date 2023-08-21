package com.example.asian;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class TabLayoutRecyclerViewActivity extends AppCompatActivity {

    TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tlContainer);
        ViewPager viewPager = findViewById(R.id.vpContainer);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 121) {
            switch (mTabLayout.getSelectedTabPosition()) {
                case 0:
                    FragmentTabOne.removeItem(item.getGroupId());
                    break;
                case 1:
                    FragmentTabTwo.removeItem(item.getGroupId());
                    break;
                case 2:
                    FragmentTabThree.removeItem(item.getGroupId());
                    break;

            }
        }
        return super.onContextItemSelected(item);
    }
}
