/*package com.android.iunoob.bloodbank.activities; // Make sure the package declaration matches your project structure

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.DonorData;

public class DonorProfileActivity extends AppCompatActivity {

    private TextView donorNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);

        donorNameTextView = findViewById(R.id.donorNameTextView);

        // Get the donorData from the intent
        DonorData donorData = getIntent().getParcelableExtra("donorData");

        if (donorData != null) {
            // Now you have access to the donorData object
            String donorName = donorData.getName();

            // Set the donor's name in the TextView
            donorNameTextView.setText(donorName);

            // TODO: Fetch and display the rest of the donor's profile information
        }
    }
}*/
package com.android.iunoob.bloodbank.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.DonorData;

public class DonorProfileActivity extends AppCompatActivity {

    private TextView donorNameTextView, donorAddressTextView, donorContactTextView,
            donorTotalDonateTextView, donorLastDonateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);

        donorNameTextView = findViewById(R.id.donorNameTextView);
        donorAddressTextView = findViewById(R.id.donorAddressTextView);
        donorContactTextView = findViewById(R.id.donorContactTextView);
        donorTotalDonateTextView = findViewById(R.id.donorTotalDonateTextView);
        donorLastDonateTextView = findViewById(R.id.donorLastDonateTextView);

        // Get the donorData from the intent
        DonorData donorData = getIntent().getParcelableExtra("donorData");

        // Display the donor's information in the UI
        if (donorData != null) {
            donorNameTextView.setText("Name: " + donorData.getName());
            donorAddressTextView.setText("Address: " + donorData.getAddress());
            donorContactTextView.setText("Contact: " + donorData.getContact());
            donorTotalDonateTextView.setText("Total Donations: " + donorData.getTotalDonate() + " times");
            donorLastDonateTextView.setText("Last Donation: " + donorData.getLastDonate());
        } else {
            // Handle the case where donorData is null or not found
            // You can display an error message or handle it as per your requirements
            donorNameTextView.setText("Donor not found");
        }
    }
}
