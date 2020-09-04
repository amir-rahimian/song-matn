package com.rahimian.matnsong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.irozon.sneaker.Sneaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ScrollView scrollView ;
    private ConstraintLayout constraintLayout ;
    private EditText name , song ;
    private TextView text ,toptxt , welcom;
    private Button btnFind ;

    String url;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.scroll);
        constraintLayout = findViewById(R.id.btm_araa);
        name = findViewById(R.id.edt_name);
        song = findViewById(R.id.edit_song);
        text = findViewById(R.id.songtext);
        toptxt = findViewById(R.id.toptxt);
        welcom = findViewById(R.id.welcom);
        btnFind = findViewById(R.id.btn_find);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView != null) {
                    if (scrollView.getScrollY()>20) {
                        toptxt.animate().translationY(0);
                        constraintLayout.setVisibility(View.GONE);
                    }
                    else {
                        constraintLayout.setVisibility(View.VISIBLE);
                        toptxt.animate().translationY(-50);
                    }
                }
            }
        });



        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://api.lyrics.ovh/v1/" + name.getText().toString() + "/" + song.getText().toString();
                url =  url.replaceAll(" " ,"%20");

                btnFind.setVisibility(View.INVISIBLE);
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                try {
                                    btnFind.setVisibility(View.VISIBLE);
                                    welcom.animate().translationY(-30).alpha(0f);
                                    JSONObject jsonObject = new JSONObject(response.toString());
                                    text.setText(jsonObject.getString("lyrics"));
                                    toptxt.setText(name.getText().toString()+" - "+song.getText().toString());


                                } catch (JSONException e) {
                                    Sneaker.with(MainActivity.this)
                                            .setTitle(""+e.getMessage())
                                            .setDuration(5000)
                                            .setIcon(R.drawable.ic_icons8_warning_shield,R.color.colorAccent)
                                            .sneak(R.color.colorPrimaryDark);
                                    btnFind.setVisibility(View.VISIBLE);
                                }

                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Sneaker.with(MainActivity.this)
                                        .setTitle(""+error.getMessage())
                                        .setDuration(5000)
                                        .setIcon(R.drawable.ic_icons8_warning_shield,R.color.colorAccent)
                                        .sneak(R.color.colorPrimaryDark);
                                btnFind.setVisibility(View.VISIBLE);
                            }
                        });

                requestQueue.add(jsonObjectRequest);
            }
        });

    }
}