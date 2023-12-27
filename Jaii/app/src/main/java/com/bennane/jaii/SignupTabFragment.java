package com.bennane.jaii;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bennane.jaii.entities.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignupTabFragment extends Fragment {

    private Spinner spinnerGroups;
    private ArrayAdapter<Group> groupsAdapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        spinnerGroups = view.findViewById(R.id.spinner_groups);

        groupsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        groupsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroups.setAdapter(groupsAdapter);

        loadGroups();
        EditText passwordEditText = view.findViewById(R.id.signup_password);
        EditText confirmPasswordEditText = view.findViewById(R.id.signup_confirm);

        // Add a TextWatcher to the confirm password EditText
        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (passwordEditText.getText().toString().equals(editable.toString())) {
                    confirmPasswordEditText.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                } else {
                    confirmPasswordEditText.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }
        });

        Button signupButton = view.findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group selectedGroup = (Group) spinnerGroups.getSelectedItem();
                Log.d("TAG","id"+selectedGroup.getId());

                String firstName = ((EditText) view.findViewById(R.id.firstname)).getText().toString();
                String lastName = ((EditText) view.findViewById(R.id.lastname)).getText().toString();
                String number = ((EditText) view.findViewById(R.id.number)).getText().toString();
                String email = ((EditText) view.findViewById(R.id.signup_email)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.signup_password)).getText().toString();
                String confirmPassword = ((EditText) view.findViewById(R.id.signup_confirm)).getText().toString();

                performSignup(firstName, lastName, number, email, password, confirmPassword, selectedGroup);
            }
        });

        return view;
    }

    private void loadGroups() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        String apiUrl = "http://10.0.2.2:8080/api/v1/groupes";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Group> groups = parseGroupsFromResponse(response);

                        groupsAdapter.addAll(groups);
                        groupsAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
    private List<Group> parseGroupsFromResponse(JSONArray response) {
        List<Group> groups = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject groupObject = response.getJSONObject(i);
                int groupId = groupObject.getInt("id");
                String groupName = groupObject.getString("code");
                groups.add(new Group(groupId, groupName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return groups;
    }


    private void performSignup(String firstName, String lastName, String number,
                               String email, String password, String confirmPassword,
                               Group selectedGroup) {
        if (!password.equals(confirmPassword)) {
            Toast.makeText(requireContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        String apiUrl = "http://10.0.2.2:8080/api/v1/students/signup";

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("firstName", firstName);
            requestBody.put("lastName", lastName);
            requestBody.put("number", number);
            requestBody.put("login", email);
            requestBody.put("password", password);
            requestBody.put("confirmPassword", confirmPassword);
            JSONObject groupObject = new JSONObject();
            groupObject.put("id", selectedGroup.getId());
            requestBody.put("group", groupObject);
            requestBody.put("role", "student");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(requireContext(), "Signup successful!", Toast.LENGTH_SHORT).show();

                        clearFormFields();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
    private void clearFormFields() {
        EditText firstNameEditText = view.findViewById(R.id.firstname);
        EditText lastNameEditText = view.findViewById(R.id.lastname);
        EditText numberEditText = view.findViewById(R.id.number);
        EditText emailEditText = view.findViewById(R.id.signup_email);
        EditText passwordEditText = view.findViewById(R.id.signup_password);
        EditText confirmPasswordEditText = view.findViewById(R.id.signup_confirm);

        firstNameEditText.setText("");
        lastNameEditText.setText("");
        numberEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        confirmPasswordEditText.setBackgroundResource(R.drawable.edittext_bkg);

        spinnerGroups.setSelection(0);
    }

}
