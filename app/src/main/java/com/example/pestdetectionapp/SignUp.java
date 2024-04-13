package com.example.pestdetectionapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignUp extends AppCompatActivity {

    Bitmap bitmap;
    Bitmap reducedSize;
    byte [] image;
    ImageView profile_pic;
    EditText fullname,province,town,occupation,username,password,match;
    DatabaseHandler database;
    private apiset Apiset;
    baseUrl url = baseUrl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        profile_pic = findViewById(R.id.profile_pic);
        fullname = findViewById(R.id.fullname);
        province = findViewById(R.id.province);
        town = findViewById(R.id.town);
        occupation = findViewById(R.id.occupation);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        match = findViewById(R.id.matchpassword);

        database = new DatabaseHandler(getApplicationContext());

    }

    public void selectImage(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 2);


    }

    public void signUp(View view) throws NoSuchAlgorithmException {

        if (validate()) {

            String Fullname = fullname.getText().toString();
            String Province = province.getText().toString();
            String Town = town.getText().toString();
            String Occupation = occupation.getText().toString();
            String Username = username.getText().toString();
            String pass = password.getText().toString();

            // Hashing password using SHA256
            String Password = hash.hashString(pass);

            // converting image to bytearray
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            reducedSize.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] image = stream.toByteArray();

            // Convert image to base64 string (optional)
            String pictureString = Base64.encodeToString(image, Base64.DEFAULT);


            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[7];
                    field[0] = "Fullname";
                    field[1] = "Town";
                    field[2] = "Province";
                    field[3] = "Occupation";
                    field[4] = "Username";
                    field[5] = "Password";
                    field[6] = "picture";

                    String[] data = new String[7];
                    data[0] = Fullname;
                    data[1] = Town;
                    data[2] = Province;
                    data[3] = Occupation;
                    data[4] = Username;
                    data[5] = Password;
                    data[6] = pictureString;

                    PutData putData = new PutData(url.getUrl()+"Signup.php", "POST", field, data);

                    if (putData.startPut()) {

                        System.out.println("putData has started");

                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            System.out.println("Result:"+result);
                            System.out.println("Result:"+result);
                            System.out.println("Result:"+result);
                            System.out.println("Result:"+result);
                            System.out.println("Result:"+result);

                        }else{
                            String result = putData.getResult();
                            System.out.println("Result:"+result);
                            System.out.println("Result:"+result);
                            System.out.println("Result:"+result);
                            System.out.println("Result:"+result);
                            System.out.println("Result:"+result);

                        }
                    }
                }
            });

            //Inflating the customlayout
                LayoutInflater inflater0 = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView0 = inflater0.inflate(R.layout.dummy_popup, null);

                int windth0 = WindowManager.LayoutParams.MATCH_PARENT;
                int height0 = WindowManager.LayoutParams.MATCH_PARENT;
                PopupWindow dummyPopup = new PopupWindow(popupView0,windth0,height0,true);
                dummyPopup.showAtLocation(view, Gravity.CENTER, 0, 0);


                // ACTUAL POPUP WINDOW
                //Inflating the customlayout
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.pop_up_dialog, null);

                //Instantiating the views
                Button yes = popupView.findViewById(R.id.yes);
                Button no = popupView.findViewById(R.id.no);
                TextView title = popupView.findViewById(R.id.popup_title);
                TextView msg = popupView.findViewById(R.id.popup_msg);

                title.setText("Registered");
                msg.setText("Account successfuly created!\n You will be redirected to the login page. ");
                yes.setVisibility(View.GONE);
                no.setText("Ok");


                //Creating the pop up window
//                int width = ScrollView.LayoutParams.WRAP_CONTENT;
//                int height = ScrollView.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, 1022, 1636, true);
                //Show the popUpwindow
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if(dummyPopup.isShowing()){
                            dummyPopup.dismiss();
                        }
                    }
                });


                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);

                    }
                });




            }
        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                // Get the selected image and store it in a bitmap
                Uri dat = data.getData();
                try {
                    // Locating the image path using URI and store the image in bitmap
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            reducedSize = reduceImageSize(bitmap,240,240);
            profile_pic.setImageBitmap(reducedSize);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap reduceImageSize(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();
        // Calculate the correct scale size
        float scale = Math.min(((float)maxWidth / originalWidth), ((float)maxHeight / originalHeight));
        // Create a matrix for manipulation
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // Create a new bitmap with the same color model as the original bitmap, but scaled down.
        Bitmap scaledBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix, true);
        return scaledBitmap;
    }

    public boolean validate(){
        boolean valid = true;

        String name = fullname.getText().toString();
        String provinces = province.getText().toString();
        String towns = town.getText().toString();
        String occupations = occupation.getText().toString();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String confirm = match.getText().toString();


        if(TextUtils.isEmpty(name)){
            fullname.setError("This field is required!");
            valid = false;
        }else{fullname.setError(null);}

        if(TextUtils.isEmpty(provinces)){
            province.setError("This field is required!");
            valid = false;
        }else{province.setError(null);}

        if(TextUtils.isEmpty(towns)){
            town.setError("This field is required!");
            valid = false;
        }else{town.setError(null);}

        if(TextUtils.isEmpty(occupations)){
            occupation.setError("This field is required!");
            valid = false;
        }else{occupation.setError(null);}

        if(TextUtils.isEmpty(user)){
            username.setError("This field is required!");
            valid = false;
        }else{username.setError(null);}

        if(TextUtils.isEmpty(pass) || pass.length()<6){
            password.setError("Password must be at least 6 Characters");
            valid = false;
        }else{password.setError(null);}

        if(TextUtils.isEmpty(confirm)){
            match.setError("Please confirm password!");
            valid = false;
        } else if (!confirm.equals(pass)){
            match.setError("Passwords doesn't match!");
            valid = false;
        }else{match.setError(null);}

        if(bitmap == null){
            Toast.makeText(SignUp.this,"Please Upload an Image as Profile!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private Gson getLenientGson() {
        return new GsonBuilder().setLenient().create();
    }
}