/*package com.android.iunoob.bloodbank.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.UserData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText inputemail, inputpassword, retypePassword, fullName, address, contact;
    private FirebaseAuth mAuth;
    private Button btnSignup;
    private ProgressDialog pd;
    private Spinner gender, bloodgroup, division;

    private boolean isUpdate = false;

    private DatabaseReference db_ref, donor_ref;
    private FirebaseDatabase db_User;
    private CheckBox isDonor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        setContentView(R.layout.activity_profile);

        db_User = FirebaseDatabase.getInstance();
        db_ref = db_User.getReference("users");
        donor_ref = db_User.getReference("donors");
        mAuth = FirebaseAuth.getInstance();

        inputemail = findViewById(R.id.input_userEmail);
        inputpassword = findViewById(R.id.input_password);
        retypePassword = findViewById(R.id.input_password_confirm);
        fullName = findViewById(R.id.input_fullName);
        gender = findViewById(R.id.gender);
        address = findViewById(R.id.inputAddress);
        division = findViewById(R.id.inputDivision);
        bloodgroup = findViewById(R.id.inputBloodGroup);
        contact = findViewById(R.id.inputMobile);
        isDonor = findViewById(R.id.checkbox);

        btnSignup = findViewById(R.id.button_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mAuth.getCurrentUser() != null) {

            inputemail.setVisibility(View.GONE);
            inputpassword.setVisibility(View.GONE);
            retypePassword.setVisibility(View.GONE);
            btnSignup.setText("Update Profile");
            pd.dismiss();
            /// getActionBar().setTitle("Profile");
            getSupportActionBar().setTitle("Profile");
            findViewById(R.id.image_logo).setVisibility(View.GONE);
            isUpdate = true;

            Query Profile = db_ref.child(mAuth.getCurrentUser().getUid());
            Profile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    UserData userData = dataSnapshot.getValue(UserData.class);

                    if (userData != null) {
                        pd.show();
                        fullName.setText(userData.getName());
                        gender.setSelection(userData.getGender());
                        address.setText(userData.getAddress());
                        contact.setText(userData.getContact());
                        bloodgroup.setSelection(userData.getBloodGroup());
                        division.setSelection(userData.getDivision());
                        Query donor = donor_ref.child(division.getSelectedItem().toString())
                                .child(bloodgroup.getSelectedItem().toString())
                                .child(mAuth.getCurrentUser().getUid());

                        donor.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {
                                    isDonor.setChecked(true);
                                    isDonor.setText("Unmark this to leave from donors");
                                }
                                else
                                {
                                    Toast.makeText(ProfileActivity.this, "Your are not a donor! Be a donor and save life by donating blood.",
                                            Toast.LENGTH_LONG).show();
                                }
                                pd.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("User", databaseError.getMessage());
                            }

                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("User", databaseError.getMessage());
                }
            });


        } else pd.dismiss();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputemail.getText().toString();
                final String password = inputpassword.getText().toString();
                final String ConfirmPassword = retypePassword.getText().toString();
                final String Name = fullName.getText().toString();
                final int Gender = gender.getSelectedItemPosition();
                final String Contact = contact.getText().toString();
                final int BloodGroup = bloodgroup.getSelectedItemPosition();
                final String Address = address.getText().toString();
                final int Division = division.getSelectedItemPosition();
                final String blood = bloodgroup.getSelectedItem().toString();
                final String div   = division.getSelectedItem().toString();

                try {

                    if (Name.length() <= 2) {
                        ShowError("Name");
                        fullName.requestFocusFromTouch();
                    } else if (Contact.length() < 11) {
                        ShowError("Contact Number");
                        contact.requestFocusFromTouch();
                    } else if (Address.length() <= 2) {
                        ShowError("Address");
                        address.requestFocusFromTouch();
                    } else {
                        if (!isUpdate) {
                            if (email.length() == 0) {
                                ShowError("Email ID");
                                inputemail.requestFocusFromTouch();
                            } else if (password.length() <= 5) {
                                ShowError("Password");
                                inputpassword.requestFocusFromTouch();
                            } else if (password.compareTo(ConfirmPassword) != 0) {
                                Toast.makeText(ProfileActivity.this, "Password did not match!", Toast.LENGTH_LONG)
                                        .show();
                                retypePassword.requestFocusFromTouch();
                            } else {
                                pd.show();
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(ProfileActivity.this, "Registration failed! try agian.", Toast.LENGTH_LONG)
                                                            .show();
                                                    Log.v("error", task.getException().getMessage());
                                                } else {
                                                    String id = mAuth.getCurrentUser().getUid();
                                                    db_ref.child(id).child("Name").setValue(Name);
                                                    db_ref.child(id).child("Gender").setValue(Gender);
                                                    db_ref.child(id).child("Contact").setValue(Contact);
                                                    db_ref.child(id).child("BloodGroup").setValue(BloodGroup);
                                                    db_ref.child(id).child("Address").setValue(Address);
                                                    db_ref.child(id).child("Division").setValue(Division);

                                                    if(isDonor.isChecked())
                                                    {
                                                        donor_ref.child(div).child(blood).child(id).child("UID").setValue(id).toString();
                                                        donor_ref.child(div).child(blood).child(id).child("LastDonate").setValue("Don't donate yet!");
                                                        donor_ref.child(div).child(blood).child(id).child("TotalDonate").setValue(0);
                                                        donor_ref.child(div).child(blood).child(id).child("Name").setValue(Name);
                                                        donor_ref.child(div).child(blood).child(id).child("Contact").setValue(Contact);
                                                        donor_ref.child(div).child(blood).child(id).child("Address").setValue(Address);

                                                    }

                                                    Toast.makeText(getApplicationContext(), "Welcome, your account has been created!", Toast.LENGTH_LONG)
                                                            .show();
                                                    Intent intent = new Intent(ProfileActivity.this, Dashboard.class);
                                                    startActivity(intent);

                                                    finish();
                                                }
                                                pd.dismiss();

                                            }

                                        });
                            }

                        } else {

                            String id = mAuth.getCurrentUser().getUid();
                            db_ref.child(id).child("Name").setValue(Name);
                            db_ref.child(id).child("Gender").setValue(Gender);
                            db_ref.child(id).child("Contact").setValue(Contact);
                            db_ref.child(id).child("BloodGroup").setValue(BloodGroup);
                            db_ref.child(id).child("Address").setValue(Address);
                            db_ref.child(id).child("Division").setValue(Division);

                            if(isDonor.isChecked())
                            {
                                donor_ref.child(div).child(blood).child(id).child("UID").setValue(id).toString();
                                donor_ref.child(div).child(blood).child(id).child("LastDonate").setValue("Don't donate yet!");
                                donor_ref.child(div).child(blood).child(id).child("TotalDonate").setValue(0);
                                donor_ref.child(div).child(blood).child(id).child("Name").setValue(Name);
                                donor_ref.child(div).child(blood).child(id).child("Contact").setValue(Contact);
                                donor_ref.child(div).child(blood).child(id).child("Address").setValue(Address);

                            }
                            else
                            {

                                donor_ref.child(div).child(blood).child(id).removeValue();

                            }
                            Toast.makeText(getApplicationContext(), "Your account has been updated!", Toast.LENGTH_LONG)
                                    .show();
                            Intent intent = new Intent(ProfileActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        pd.dismiss();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void ShowError(String error) {

        Toast.makeText(ProfileActivity.this, "Please, Enter a valid "+error,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}*/
/*package com.android.iunoob.bloodbank.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.UserData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private EditText inputemail, inputpassword, retypePassword, fullName, address, contact;
    private FirebaseAuth mAuth;
    private Button btnSignup;
    private ProgressDialog pd;
    private Spinner gender, bloodgroup, division;
    private boolean isUpdate = false;
    private DatabaseReference db_ref, donor_ref;
    private FirebaseDatabase db_User;
    private CheckBox isDonor;
    private Location currentLocation;
    private String currentDivision = ""; // Store the tracked division name here

    private static final int Permission_Request = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        setContentView(R.layout.activity_profile);

        db_User = FirebaseDatabase.getInstance();
        db_ref = db_User.getReference("users");
        donor_ref = db_User.getReference("donors");
        mAuth = FirebaseAuth.getInstance();

        inputemail = findViewById(R.id.input_userEmail);
        inputpassword = findViewById(R.id.input_password);
        retypePassword = findViewById(R.id.input_password_confirm);
        fullName = findViewById(R.id.input_fullName);
        gender = findViewById(R.id.gender);
        address = findViewById(R.id.inputAddress);
        division = findViewById(R.id.inputDivision);
        bloodgroup = findViewById(R.id.inputBloodGroup);
        contact = findViewById(R.id.inputMobile);
        isDonor = findViewById(R.id.checkbox);

        btnSignup = findViewById(R.id.button_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the division spinner with data
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.division_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division.setAdapter(adapter);

        // Call the method to fetch and set the current location
        fetchAndSetCurrentLocation();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Existing code for signing up or updating profile
            }
        });
    }

    // Other methods...

    private void fetchAndSetCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        currentLocation = location;
                        String locationAddress = getAddressFromLocation(location);
                        if (locationAddress != null) {
                            address.setText(locationAddress);
                            // Set the division based on the tracked location
                            currentDivision = getDivisionFromLocation(locationAddress);
                            int divisionPosition = getDivisionPosition(currentDivision);
                            division.setSelection(divisionPosition);
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to get current location", Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss(); // Dismiss the progress dialog when location is fetched
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Permission_Request);
            pd.dismiss(); // Dismiss the progress dialog in case permissions are not granted
        }
    }

    private String getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDivisionFromLocation(String locationAddress) {
        // Implement logic to extract division name from the address
        // For example, you can use regex or string manipulation to identify the division
        // and return the division name.
        // This logic may vary based on the format of the address.
        // For this example, I'll assume that the division name is the first word in the address.
        String[] addressParts = locationAddress.split(","); // Split the address by comma
        if (addressParts.length > 0) {
            // Assuming the division name is the first part of the address
            return addressParts[0].trim();
        }
        return "";
    }

    private int getDivisionPosition(String divisionName) {
        // Implement logic to map division name to a position in the spinner
        // You can use a predefined list of divisions and find the position based on the name.
        // For example, you can create an array of division names and find the index of the name.
        String[] divisions = getResources().getStringArray(R.array.division_list); // Assuming you have an array resource for divisions
        for (int i = 0; i < divisions.length; i++) {
            if (divisions[i].equalsIgnoreCase(divisionName)) {
                return i;
            }
        }
        return 0; // Default to the first division if not found
    }
}*/
/*package com.android.iunoob.bloodbank.activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.iunoob.bloodbank.fragments.NearByHospitalActivity;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText inputemail, inputpassword, retypePassword, fullName, address, contact;
    private FirebaseAuth mAuth;
    private Button btnSignup;
    private ProgressDialog pd;
    private Spinner gender, bloodgroup, division;
    private boolean isUpdate = false;
    private DatabaseReference db_ref, donor_ref;
    private FirebaseDatabase db_User;
    private CheckBox isDonor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        setContentView(R.layout.activity_profile);

        db_User = FirebaseDatabase.getInstance();
        db_ref = db_User.getReference("users");
        donor_ref = db_User.getReference("donors");
        mAuth = FirebaseAuth.getInstance();

        inputemail = findViewById(R.id.input_userEmail);
        inputpassword = findViewById(R.id.input_password);
        retypePassword = findViewById(R.id.input_password_confirm);
        fullName = findViewById(R.id.input_fullName);
        gender = findViewById(R.id.gender);
        address = findViewById(R.id.inputAddress);
        division = findViewById(R.id.inputDivision);
        bloodgroup = findViewById(R.id.inputBloodGroup);
        contact = findViewById(R.id.inputMobile);
        isDonor = findViewById(R.id.checkbox);

        btnSignup = findViewById(R.id.button_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mAuth.getCurrentUser() != null) {
            inputemail.setVisibility(View.GONE);
            inputpassword.setVisibility(View.GONE);
            retypePassword.setVisibility(View.GONE);
            btnSignup.setText("Update Profile");
            pd.dismiss();
            getSupportActionBar().setTitle("Profile");
            isUpdate = true;

            Query profile = db_ref.child(mAuth.getCurrentUser().getUid());
            profile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserData userData = dataSnapshot.getValue(UserData.class);
                    if (userData != null) {
                        pd.show();
                        fullName.setText(userData.getName());
                        gender.setSelection(userData.getGender());
                        address.setText(userData.getAddress());
                        contact.setText(userData.getContact());
                        bloodgroup.setSelection(userData.getBloodGroup());
                        division.setSelection(userData.getDivision());
                        Query donor = donor_ref.child(division.getSelectedItem().toString())
                                .child(bloodgroup.getSelectedItem().toString())
                                .child(mAuth.getCurrentUser().getUid());
                        donor.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    isDonor.setChecked(true);
                                    isDonor.setText("Unmark this to leave from donors");
                                } else {
                                    Toast.makeText(ProfileActivity.this, "You are not a donor! Be a donor and save a life by donating blood.", Toast.LENGTH_LONG).show();
                                }
                                pd.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        } else {
            pd.dismiss();
        }

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputemail.getText().toString();
                final String password = inputpassword.getText().toString();
                final String confirmPassword = retypePassword.getText().toString();
                final String name = fullName.getText().toString();
                final int genderPosition = gender.getSelectedItemPosition();
                final String contactNumber = contact.getText().toString();
                final int bloodGroupPosition = bloodgroup.getSelectedItemPosition();
                final String userAddress = address.getText().toString();
                final int divisionPosition = division.getSelectedItemPosition();
                final String blood = bloodgroup.getSelectedItem().toString();
                final String div = division.getSelectedItem().toString();

                try {
                    if (name.length() <= 2) {
                        ShowError("Name");
                        fullName.requestFocusFromTouch();
                    } else if (contactNumber.length() < 11) {
                        ShowError("Contact Number");
                        contact.requestFocusFromTouch();
                    } else if (userAddress.length() <= 2) {
                        ShowError("Address");
                        address.requestFocusFromTouch();
                    } else {
                        if (!isUpdate) {
                            if (email.length() == 0) {
                                ShowError("Email ID");
                                inputemail.requestFocusFromTouch();
                            } else if (password.length() <= 5) {
                                ShowError("Password");
                                inputpassword.requestFocusFromTouch();
                            } else if (!password.equals(confirmPassword)) {
                                Toast.makeText(ProfileActivity.this, "Password did not match!", Toast.LENGTH_LONG).show();
                                retypePassword.requestFocusFromTouch();
                            } else {
                                pd.show();
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(ProfileActivity.this, "Registration failed! Try again.", Toast.LENGTH_LONG).show();
                                                    pd.dismiss();
                                                    return;
                                                }
                                                String id = mAuth.getCurrentUser().getUid();
                                                db_ref.child(id).child("Name").setValue(name);
                                                db_ref.child(id).child("Gender").setValue(genderPosition);
                                                db_ref.child(id).child("Contact").setValue(contactNumber);
                                                db_ref.child(id).child("BloodGroup").setValue(bloodGroupPosition);
                                                db_ref.child(id).child("Address").setValue(userAddress);
                                                db_ref.child(id).child("Division").setValue(divisionPosition);

                                                if (isDonor.isChecked()) {
                                                    donor_ref.child(div).child(blood).child(id).child("UID").setValue(id).toString();
                                                    donor_ref.child(div).child(blood).child(id).child("LastDonate").setValue("Don't donate yet!");
                                                    donor_ref.child(div).child(blood).child(id).child("TotalDonate").setValue(0);
                                                    donor_ref.child(div).child(blood).child(id).child("Name").setValue(name);
                                                    donor_ref.child(div).child(blood).child(id).child("Contact").setValue(contactNumber);
                                                    donor_ref.child(div).child(blood).child(id).child("Address").setValue(userAddress);
                                                } else {
                                                    donor_ref.child(div).child(blood).child(id).removeValue();
                                                }
                                                Toast.makeText(getApplicationContext(), "Your account has been updated!", Toast.LENGTH_LONG).show();
                                                pd.dismiss();
                                            }
                                        });
                            }
                        } else {
                            String id = mAuth.getCurrentUser().getUid();
                            db_ref.child(id).child("Name").setValue(name);
                            db_ref.child(id).child("Gender").setValue(genderPosition);
                            db_ref.child(id).child("Contact").setValue(contactNumber);
                            db_ref.child(id).child("BloodGroup").setValue(bloodGroupPosition);
                            db_ref.child(id).child("Address").setValue(userAddress);
                            db_ref.child(id).child("Division").setValue(divisionPosition);

                            if (isDonor.isChecked()) {
                                donor_ref.child(div).child(blood).child(id).child("UID").setValue(id).toString();
                                donor_ref.child(div).child(blood).child(id).child("LastDonate").setValue("Don't donate yet!");
                                donor_ref.child(div).child(blood).child(id).child("TotalDonate").setValue(0);
                                donor_ref.child(div).child(blood).child(id).child("Name").setValue(name);
                                donor_ref.child(div).child(blood).child(id).child("Contact").setValue(contactNumber);
                                donor_ref.child(div).child(blood).child(id).child("Address").setValue(userAddress);
                            } else {
                                donor_ref.child(div).child(blood).child(id).removeValue();
                            }
                            Toast.makeText(getApplicationContext(), "Your account has been updated!", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ShowError(String error) {
        Toast.makeText(ProfileActivity.this, "Please, Enter a valid " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}*/
package com.android.iunoob.bloodbank.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText inputemail, inputpassword, retypePassword, fullName, address, contact;
    private FirebaseAuth mAuth;
    private Button btnSignup;
    private ProgressDialog pd;
    private Spinner gender, bloodgroup, division;
    private boolean isUpdate = false;
    private DatabaseReference db_ref, donor_ref;
    private FirebaseDatabase db_User;
    private CheckBox isDonor;

    // Extra field to hold the current location
    private String currentLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        setContentView(R.layout.activity_profile);

        db_User = FirebaseDatabase.getInstance();
        db_ref = db_User.getReference("users");
        donor_ref = db_User.getReference("donors");
        mAuth = FirebaseAuth.getInstance();

        inputemail = findViewById(R.id.input_userEmail);
        inputpassword = findViewById(R.id.input_password);
        retypePassword = findViewById(R.id.input_password_confirm);
        fullName = findViewById(R.id.input_fullName);
        gender = findViewById(R.id.gender);
        address = findViewById(R.id.inputAddress);
        division = findViewById(R.id.inputDivision);
        bloodgroup = findViewById(R.id.inputBloodGroup);
        contact = findViewById(R.id.inputMobile);
        isDonor = findViewById(R.id.checkbox);

        btnSignup = findViewById(R.id.button_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check if the location data is passed from NearByHospitalActivity
        Intent intent = getIntent();
        if (intent.hasExtra("currentLocation")) {
            currentLocation = intent.getStringExtra("currentLocation");
        }

        // Set the address field to the current location
        address.setText(currentLocation);

        if (mAuth.getCurrentUser() != null) {
            inputemail.setVisibility(View.GONE);
            inputpassword.setVisibility(View.GONE);
            retypePassword.setVisibility(View.GONE);
            btnSignup.setText("Update Profile");
            pd.dismiss();
            getSupportActionBar().setTitle("Profile");
            isUpdate = true;

            Query profile = db_ref.child(mAuth.getCurrentUser().getUid());
            profile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserData userData = dataSnapshot.getValue(UserData.class);
                    if (userData != null) {
                        pd.show();
                        fullName.setText(userData.getName());
                        gender.setSelection(userData.getGender());
                        address.setText(userData.getAddress());
                        contact.setText(userData.getContact());
                        bloodgroup.setSelection(userData.getBloodGroup());
                        division.setSelection(userData.getDivision());
                        Query donor = donor_ref.child(division.getSelectedItem().toString())
                                .child(bloodgroup.getSelectedItem().toString())
                                .child(mAuth.getCurrentUser().getUid());
                        donor.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    isDonor.setChecked(true);
                                    isDonor.setText("Unmark this to leave from donors");
                                } else {
                                    Toast.makeText(ProfileActivity.this, "You are not a donor! Be a donor and save a life by donating blood.", Toast.LENGTH_LONG).show();
                                }
                                pd.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        } else {
            pd.dismiss();
        }

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputemail.getText().toString();
                final String password = inputpassword.getText().toString();
                final String confirmPassword = retypePassword.getText().toString();
                final String name = fullName.getText().toString();
                final int genderPosition = gender.getSelectedItemPosition();
                final String contactNumber = contact.getText().toString();
                final int bloodGroupPosition = bloodgroup.getSelectedItemPosition();
                final String userAddress = address.getText().toString();
                final int divisionPosition = division.getSelectedItemPosition();
                final String blood = bloodgroup.getSelectedItem().toString();
                final String div = division.getSelectedItem().toString();

                try {
                    if (name.length() <= 2) {
                        ShowError("Name");
                        fullName.requestFocusFromTouch();
                    } else if (contactNumber.length() < 11) {
                        ShowError("Contact Number");
                        contact.requestFocusFromTouch();
                    } else if (userAddress.length() <= 2) {
                        ShowError("Address");
                        address.requestFocusFromTouch();
                    } else {
                        if (!isUpdate) {
                            if (email.length() == 0) {
                                ShowError("Email ID");
                                inputemail.requestFocusFromTouch();
                            } else if (password.length() <= 5) {
                                ShowError("Password");
                                inputpassword.requestFocusFromTouch();
                            } else if (!password.equals(confirmPassword)) {
                                Toast.makeText(ProfileActivity.this, "Password did not match!", Toast.LENGTH_LONG).show();
                                retypePassword.requestFocusFromTouch();
                            } else {
                                pd.show();
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(ProfileActivity.this, "Registration failed! Try again.", Toast.LENGTH_LONG).show();
                                                    pd.dismiss();
                                                    return;
                                                }
                                                String id = mAuth.getCurrentUser().getUid();
                                                db_ref.child(id).child("Name").setValue(name);
                                                db_ref.child(id).child("Gender").setValue(genderPosition);
                                                db_ref.child(id).child("Contact").setValue(contactNumber);
                                                db_ref.child(id).child("BloodGroup").setValue(bloodGroupPosition);
                                                db_ref.child(id).child("Address").setValue(userAddress);
                                                db_ref.child(id).child("Division").setValue(divisionPosition);

                                                if (isDonor.isChecked()) {
                                                    donor_ref.child(div).child(blood).child(id).child("UID").setValue(id).toString();
                                                    donor_ref.child(div).child(blood).child(id).child("LastDonate").setValue("Don't donate yet!");
                                                    donor_ref.child(div).child(blood).child(id).child("TotalDonate").setValue(0);
                                                    donor_ref.child(div).child(blood).child(id).child("Name").setValue(name);
                                                    donor_ref.child(div).child(blood).child(id).child("Contact").setValue(contactNumber);
                                                    donor_ref.child(div).child(blood).child(id).child("Address").setValue(userAddress);
                                                } else {
                                                    donor_ref.child(div).child(blood).child(id).removeValue();
                                                }
                                                Toast.makeText(getApplicationContext(), "Your account has been updated!", Toast.LENGTH_LONG).show();
                                                pd.dismiss();
                                            }
                                        });
                            }
                        } else {
                            String id = mAuth.getCurrentUser().getUid();
                            db_ref.child(id).child("Name").setValue(name);
                            db_ref.child(id).child("Gender").setValue(genderPosition);
                            db_ref.child(id).child("Contact").setValue(contactNumber);
                            db_ref.child(id).child("BloodGroup").setValue(bloodGroupPosition);
                            db_ref.child(id).child("Address").setValue(userAddress);
                            db_ref.child(id).child("Division").setValue(divisionPosition);

                            if (isDonor.isChecked()) {
                                donor_ref.child(div).child(blood).child(id).child("UID").setValue(id).toString();
                                donor_ref.child(div).child(blood).child(id).child("LastDonate").setValue("Don't donate yet!");
                                donor_ref.child(div).child(blood).child(id).child("TotalDonate").setValue(0);
                                donor_ref.child(div).child(blood).child(id).child("Name").setValue(name);
                                donor_ref.child(div).child(blood).child(id).child("Contact").setValue(contactNumber);
                                donor_ref.child(div).child(blood).child(id).child("Address").setValue(userAddress);
                            } else {
                                donor_ref.child(div).child(blood).child(id).removeValue();
                            }
                            Toast.makeText(getApplicationContext(), "Your account has been updated!", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ShowError(String error) {
        Toast.makeText(ProfileActivity.this, "Please, Enter a valid " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}