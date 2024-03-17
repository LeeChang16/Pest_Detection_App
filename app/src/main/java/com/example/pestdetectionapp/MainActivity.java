package com.example.pestdetectionapp;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.lang.reflect.Field;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {


    private MeowBottomNavigation bottomNavigation;
    RelativeLayout Home, Gallery, Capture, Profile, Setting;
    private final static int home =1;
    private final static int capture =2;
    private final static int menu =3;
    int user_universal_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent intent = getIntent();
        user_universal_id = intent.getIntExtra("IdValue",0);


        bottomNavigation = findViewById(R.id.bottom_navi);
//        Home = findViewById(R.id.bottom_navi);
//        Gallery = findViewById(R.id.bottom_navi);
//        Capture = findViewById(R.id.bottom_navi);
//        Profile = findViewById(R.id.bottom_navi);
//        Setting = findViewById(R.id.bottom_navi);


        bottomNavigation.add(new MeowBottomNavigation.Model(home, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(capture, R.drawable.ic_camera));
        bottomNavigation.add(new MeowBottomNavigation.Model(menu, R.drawable.baseline_menu_24));

        //set default selected fragment
        bottomNavigation.show(home, true);


        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // Make icon visible when clicked
            switch (model.getId()){

                case 2:
                    bottomNavigation.setCircleColor(Color.argb(0,0,0,0));


                    break;
                default:
                    bottomNavigation.setCircleColor(Color.parseColor("#047511"));

            }

                return null;
            }
        });

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // Initialize Fragments according to its IDs.

                Fragment fragment = null;
                if (model.getId() == 1) {

                    fragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Id", user_universal_id); // replace 'id' with your actual ID variable
                    fragment.setArguments(bundle);

                } else if (model.getId() == 2) {
                    fragment = new CaptureFragment();
                } else if (model.getId() == 3) {
                    fragment = new MenuFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Id", user_universal_id); // replace 'id' with your actual ID variable
                    fragment.setArguments(bundle);
                }
//                else if (model.getId() == 4) {
//                    fragment = new SettingsFragment();
//                } else if (model.getId() == 5) {
//                    fragment = new ProfileFragment();
//                }

                // Create Method for load and replace fragments

                LoadAndReplaceFragment(fragment);
                return null;
            }


        });
    }
        private void LoadAndReplaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,fragment,null)
                .commit();

    }
}