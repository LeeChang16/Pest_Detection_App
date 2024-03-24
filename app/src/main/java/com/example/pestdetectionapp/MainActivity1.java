package com.example.pestdetectionapp;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.pestdetectionapp.customview.OverlayView;
import com.example.pestdetectionapp.env.ImageUtils;
import com.example.pestdetectionapp.env.Logger;
import com.example.pestdetectionapp.tflite.Classifier;
import com.example.pestdetectionapp.tflite.YoloV5Classifier;
import com.example.pestdetectionapp.tracking.MultiBoxTracker;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity1 extends AppCompatActivity {


    private MeowBottomNavigation bottomNavigation;
    RelativeLayout Home, Gallery, Capture, Profile, Setting;
    private final static int home =1;
    private final static int capture =2;
    private final static int menu =3;
    int user_universal_id;
    private static final int TIME_INTERVAL = 2000; // Desired time to wait between back presses, in milliseconds
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


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

        Intent intent1 = new Intent(this, DetectorActivity.class);

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // Initialize Fragments according to its IDs.



                    if (model.getId() == 1 || model.getId() == 3) {
                        Fragment fragment = null;
                        if (model.getId() == 1) {

                            fragment = new HomeFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Id", user_universal_id); // replace 'id' with your actual ID variable
                            fragment.setArguments(bundle);

                            //                } else if (model.getId() == 2) {
                            ////                   fragment = new CaptureFragment();
                            //                    startActivity(intent1);


                        } else if (model.getId() == 3) {
                            fragment = new MenuFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Id", user_universal_id); // replace 'id' with your actual ID variable
                            fragment.setArguments(bundle);
                        }


                        LoadAndReplaceFragment(fragment);


                    } else if (model.getId() == 2) {

                        startActivity(intent1);
                    }

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

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            Runtime.getRuntime().exit(1);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Double-tap to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }





    //Start Of Realtime Utilities

    private static final Logger LOGGER = new Logger();

    public static final int TF_OD_API_INPUT_SIZE = 640;

    private static final boolean TF_OD_API_IS_QUANTIZED = false;

    private static final String TF_OD_API_MODEL_FILE = "best-fp16.tflite";
    private static final String TF_OD_API_LABELS_FILE = "customclasses.txt";
    // Minimum detection confidence to track a detection.
    private static final boolean MAINTAIN_ASPECT = true;
    private Integer sensorOrientation = 90;
    private Classifier detector;
    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;
    private MultiBoxTracker tracker;
    private OverlayView trackingOverlay;
    protected int previewWidth = 0;
    protected int previewHeight = 0;
//    private Bitmap sourceBitmap;
//    private Bitmap cropBitmap;
//    private Button cameraButton, detectButton;
//    private ImageView imageView;
    public static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;



    private void initBox() {
        previewHeight = TF_OD_API_INPUT_SIZE;
        previewWidth = TF_OD_API_INPUT_SIZE;
        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        tracker = new MultiBoxTracker(this);
        trackingOverlay = findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                canvas -> tracker.draw(canvas));

        tracker.setFrameConfiguration(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, sensorOrientation);

        try {
            detector =
                    YoloV5Classifier.create(
                            getAssets(),
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_IS_QUANTIZED,
                            TF_OD_API_INPUT_SIZE);
        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.e(e, "Exception initializing classifier!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }


    private void handleResult(Bitmap bitmap, List<Classifier.Recognition> results) {
        final Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);

        final List<Classifier.Recognition> mappedRecognitions =
                new LinkedList<Classifier.Recognition>();

        for (final Classifier.Recognition result : results) {
            final RectF location = result.getLocation();
            if (location != null && result.getConfidence() >= MINIMUM_CONFIDENCE_TF_OD_API) {
                canvas.drawRect(location, paint);
//                cropToFrameTransform.mapRect(location);
//
//                result.setLocation(location);
//                mappedRecognitions.add(result);
            }
        }
//        tracker.trackResults(mappedRecognitions, new Random().nextInt());
//        trackingOverlay.postInvalidate();
//        imageView.setImageBitmap(bitmap);
    }
    //End of Real TIme Utilities

}