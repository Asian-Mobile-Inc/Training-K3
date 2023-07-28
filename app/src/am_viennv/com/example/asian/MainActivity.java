package com.example.asian;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private LinearLayout mLinearTopMenu;
    private Menu mMenu;
    private NavController mNavController;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initControlNav();
        action();
        handleClickMenu();
    }

    private void initControlNav() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        assert navHostFragment != null;
        mNavController = navHostFragment.getNavController();
    }

    private void handleClickMenu() {

        mLinearTopMenu.setOnClickListener(view -> {
            mToolBar.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
            removeActionItem();
            addActionItem(R.id.action_search, R.drawable.baseline_search_24);
            addActionItem(R.id.action_favorite, R.drawable.baseline_favorite_24);
            mToolBar.setTitle("Title");
        });

        mFloatingActionButton.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT).show());

    }

    private void action() {
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(view -> mDrawerLayout.openDrawer(GravityCompat.START));
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        this.mMenu = menu;
        removeActionItem();
        return true;
    }

    private void removeActionItem() {
        mMenu.removeItem(R.id.action_search);
        mMenu.removeItem(R.id.action_favorite);
    }

    private void addActionItem(int id, int drawable) {
        MenuItem searchItem = mMenu.add(Menu.NONE, id, Menu.NONE, "Search");
        searchItem.setIcon(drawable);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    private void initView() {
        mToolBar = findViewById(R.id.tb_app);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.navigationView);
        mLinearTopMenu = mNavigationView.getHeaderView(0).findViewById(R.id.ll_topmenu);
        mFloatingActionButton = findViewById(R.id.fab_action);
    }

}