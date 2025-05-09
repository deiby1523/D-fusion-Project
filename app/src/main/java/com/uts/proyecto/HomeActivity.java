package com.uts.proyecto;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference dbRef;
    // UI Components
    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextView navUserEmail;
    private TextView navUserName;
    private View headerView;
    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;

    public static Drawable loadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    // ---------------------------
    // Inicializaciones
    // ---------------------------

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

    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private void initUI() {
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        circleImageView = headerView.findViewById(R.id.circularImageView);
        navUserEmail = headerView.findViewById(R.id.nav_userEmail);
        navUserName = headerView.findViewById(R.id.nav_userName);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        syncUserData();
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
        syncUserData();
    }

    // ---------------------------
    // Autenticación
    // ---------------------------

    private void syncUserData() {
        String uid = user.getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid);


        if (user.getPhotoUrl() != null) {
            Picasso.get()
                    .load(user.getPhotoUrl())  // ← Usa Uri directamente
                    .into(circleImageView);
        }

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userData = snapshot.getValue(User.class);
                if (userData != null) {
                    navUserEmail.setText(user.getEmail());
                    navUserName.setText(userData.getName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(findViewById(R.id.bottomAppBar), "Error en -> syncUserData()", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                        .setTextColor(getResources().getColor(R.color.black, getTheme()))
                        .show();
            }
        });
    }

    // ---------------------------
    // Navegación
    // ---------------------------

    private void checkUserSession() {
        if (user == null) {
            // Usuario no autenticado
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        } else {
            syncUserData();
        }
    }

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
                replaceFragment(new NoteFragment());
            }
            return true;
        });
    }

    private void setupFab() {
        fab.setOnClickListener(view -> showBottomDialog());
    }

    // ---------------------------
    // Funciones auxiliares
    // ---------------------------

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