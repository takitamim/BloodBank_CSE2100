/*package com.android.iunoob.bloodbank.fragments;
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
}*/
package com.android.iunoob.bloodbank.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.GetNearbyPlacesData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class NearByHospitalActivity extends Fragment implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    View view;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    Location lastlocation;
    private Marker currentLocationmMarker = null;
    private static final int Permission_Request = 99;
    int PROXIMITY_RADIUS = 10000;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.near_by_hospitals, container, false);
        getActivity().setTitle("Nearest Areas");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        else
        {
            Toast.makeText(getActivity(), "MapFragment is null, why?", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Permission_Request:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.setTrafficEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(true);

        }

    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(getActivity().getApplicationContext()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                ShowHospitals(location.getLatitude(), location.getLongitude());
            }
        });

    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlaceUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=").append(nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyAmeQ8IwQWBcmFLRpKARu7LM1TlShQKmfg");

        Log.d("NearbyHospitalActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Permission_Request);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Permission_Request);
            }
            return false;

        } else
            return true;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastlocation = location;
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationmMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(0));

    }

    @Override
    public void onStart() {
        super.onStart();

        if(client!=null)
        {
            client.connect();
        }
    }

    public void ShowHospitals(double latitude, double longitude)
    {
        mMap.clear();
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        String url = getUrl(latitude, longitude, "hospital");
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(getContext(), "Showing Nearby Areas", Toast.LENGTH_SHORT).show();
    }
}