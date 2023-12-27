package com.bennane.jaii;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bennane.jaii.entities.Group;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Intent receivedIntent  = getIntent();
        long studentId = receivedIntent.getLongExtra("STUDENT_ID", -1);
        btnUpdate.setOnClickListener(view -> updateProfile(studentId));
        Log.d("id",""+studentId);
        fetchUserDetails(studentId);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {

                Intent intent = new Intent(EditProfileActivity.this, PwActivity.class);
                intent.putExtra("STUDENT_ID", studentId);
                startActivity(intent);

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }if (id == R.id.nav_logout) {
                // Handle logout button click
                logout();
                return true;
            }


            return false;
        });
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void fetchUserDetails(long studentId) {
        String url = "http://10.0.2.2:8080/api/v1/students/" + studentId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the JSON response to get user details
                            String firstName = response.getString("firstName");
                            String lastName = response.getString("lastName");
                            String email = response.getString("login");
                            String password = response.getString("password");
                            String number = response.getString("number");

                            String role = response.getString("role");


                            JSONObject groupObject = response.getJSONObject("group");
                            groupId = groupObject.getLong("id");
                            Log.d("role",role);
                            Log.d("id",""+groupId);
                            TextView textFirstName = findViewById(R.id.textFirstName);
                            textFirstName.setText(firstName);

                            TextView textLastName = findViewById(R.id.textLastName);
                            textLastName.setText(lastName);

                            TextView textEmail = findViewById(R.id.textLogin);
                            textEmail.setText(email);

                            TextView textNumber = findViewById(R.id.textNumber);
                            textNumber.setText(number);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                showToast("Error occurred. Please try again.");
                Log.e("FetchUserDetailsError", "Error: " + error.toString());
            }
        });


        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    private void updateProfile(long studentId) {
        // Retrieve updated data from EditText fields
        String updatedFirstName = ((EditText) findViewById(R.id.textFirstName)).getText().toString();
        String updatedLastName = ((EditText) findViewById(R.id.textLastName)).getText().toString();
        String updatedEmail = ((EditText) findViewById(R.id.textLogin)).getText().toString();
        String updatedPassword = ((EditText) findViewById(R.id.textPassword)).getText().toString();
        String updatedNumber = ((EditText) findViewById(R.id.textNumber)).getText().toString();
        long updatedGroupId = groupId;
        Log.d("oki",""+updatedGroupId);

        // Update the profile using Volley
        String url = "http://10.0.2.2:8080/api/v1/students/" + studentId;

        // Create the request parameters
        Map<String, Object> params = new HashMap<>();
        params.put("firstName", updatedFirstName);
        params.put("lastName", updatedLastName);
        params.put("login", updatedEmail);
        params.put("password", updatedPassword);
        params.put("number", updatedNumber);
        params.put("role", "student");
        Map<String, Long> groupMap = new HashMap<>();
        groupMap.put("id", updatedGroupId);

        // Add the group details to the params map
        params.put("group", groupMap);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                new JSONObject(params),  // Convert params to a JSONObject
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showToast("Profile updated successfully!");
                        // Optionally, you can redirect the user to another activity or perform any other action
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Error updating profile. Please try again.");
                        Log.e("UpdateProfileError", "Error: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Set the content type to JSON
                return headers;
            }
        };


        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void logout() {

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);

        finish();
    }

}