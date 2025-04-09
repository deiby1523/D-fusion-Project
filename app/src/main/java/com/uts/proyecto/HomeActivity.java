package com.uts.proyecto;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // UI Components
    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private TextView navUserEmail;
    private View headerView;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initFirebase();
        initUI();
        setupToolbarAndDrawer();
        checkUserSession();
        setupNavigation(savedInstanceState);
        setupBottomNavigation();
        setupFab();
    }

    // ---------------------------
    // Inicializaciones
    // ---------------------------

    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private void initUI() {
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navUserEmail = headerView.findViewById(R.id.nav_userEmail);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbarAndDrawer() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_nav,
                R.string.close_nav
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // ---------------------------
    // Autenticación
    // ---------------------------

    private void checkUserSession() {
        if (user == null) {
            // Usuario no autenticado
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        } else {
            navUserEmail.setText(user.getEmail());
        }
    }

    // ---------------------------
    // Navegación
    // ---------------------------

    private void setupNavigation(Bundle savedInstanceState) {
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.first) {
                replaceFragment(new FirstFragment());
            } else if (itemId == R.id.second) {
                replaceFragment(new SecondFragment());
            } else if (itemId == R.id.third) {
                replaceFragment(new ThirdFragment());
            }
            return true;
        });
    }

    private void setupFab() {
        fab.setOnClickListener(view -> showBottomDialog());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) {
            auth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return true;
        }
        return false;
    }

    // ---------------------------
    // Funciones auxiliares
    // ---------------------------

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout element1 = dialog.findViewById(R.id.layoutElement1);
        LinearLayout element2 = dialog.findViewById(R.id.layoutElement2);
        LinearLayout element3 = dialog.findViewById(R.id.layoutElement3);
        ImageView cancel = dialog.findViewById(R.id.cancelButton);

        View.OnClickListener listener = v -> {
            int id = v.getId();
            String message = "";

            if (id == R.id.layoutElement1) message = "Evento para Elemento 1";
            else if (id == R.id.layoutElement2) message = "Evento para Elemento 2";
            else if (id == R.id.layoutElement3) message = "Evento para Elemento 3";

            Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        };

        element1.setOnClickListener(listener);
        element2.setOnClickListener(listener);
        element3.setOnClickListener(listener);
        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}