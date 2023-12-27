package com.bennane.jaii;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PwActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private long studentId;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Intent intent = getIntent();
        studentId = intent.getLongExtra("STUDENT_ID", -1);
        Log.d("id", "" + studentId);


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_edit_profile) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent editProfileIntent = new Intent(this, EditProfileActivity.class);
                editProfileIntent.putExtra("STUDENT_ID", studentId);
                startActivity(editProfileIntent);
                return true;
            } if (id == R.id.nav_logout) {
                // Handle logout button click
                logout();
                return true;
            }



            return false;
        });

        listView = findViewById(R.id.listView); // Assuming you have a ListView in your layout
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);


        fetchData(studentId);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void fetchData(long studentId) {
        String url = "http://10.0.2.2:8080/api/v1/studentpws/student/" + studentId; // Replace with your actual API endpoint

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error fetching data: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void parseResponse(JSONArray response) {
        try {
            ArrayList<String> dataList = new ArrayList<>();

            for (int i = 0; i < response.length(); i++) {
                // Assuming your JSON has a "pw" object with a "title" field
                String title = response.getJSONObject(i).getJSONObject("pw").getString("title");
                dataList.add(title);
            }

            adapter.addAll(dataList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                // Get the selected item data
                String selectedPwTitle = adapter.getItem(position);

                try {
                    int pwId = response.getJSONObject(position).getJSONObject("pw").getInt("id");
                    String toothName = response.getJSONObject(position).getJSONObject("pw").getJSONObject("tooth").getString("name");


                    // Assuming you have a MainActivity class
                    Intent mainActivityIntent = new Intent(PwActivity.this, MainActivity.class);

                    // Pass the studentId and pwId to MainActivity
                    mainActivityIntent.putExtra("STUDENT_ID", studentId);
                    mainActivityIntent.putExtra("PW_ID", pwId);
                    mainActivityIntent.putExtra("toothName",toothName);

                    // Start the MainActivity
                    startActivity(mainActivityIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void logout() {

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);

        finish();
    }
}
