/*package com.android.iunoob.bloodbank.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.DonorData;

import java.util.List;


public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.PostHolder> {


    private List<DonorData> postLists;

    public class PostHolder extends RecyclerView.ViewHolder
    {
        TextView Name, Address, contact, posted, totaldonate;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.donorName);
            contact = itemView.findViewById(R.id.donorContact);
            totaldonate = itemView.findViewById(R.id.totaldonate);
            Address = itemView.findViewById(R.id.donorAddress);
            posted = itemView.findViewById(R.id.lastdonate);

        }
    }

    public SearchDonorAdapter(List<DonorData> postLists)
    {
        this.postLists = postLists;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_donor_item, viewGroup, false);

        return new PostHolder(listitem);
    }

    @Override
    public void onBindViewHolder(PostHolder postHolder, int i) {

        if(i%2==0)
        {
            postHolder.itemView.setBackgroundColor(Color.parseColor("#C13F31"));
        }
        else
        {
            postHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        DonorData donorData = postLists.get(i);
        postHolder.Name.setText("Name: "+donorData.getName());
        postHolder.contact.setText(donorData.getContact());
        postHolder.Address.setText("Address: "+donorData.getAddress());
        postHolder.totaldonate.setText("Total Donation: "+donorData.getTotalDonate()+" times");
        postHolder.posted.setText("Last Donation: "+donorData.getLastDonate());


    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }
}*/
/*package com.android.iunoob.bloodbank.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.DonorData;

import java.util.List;

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.PostHolder> {

    private List<DonorData> screenDataList; // This will hold screen data

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView Name, Address, contact, posted, totaldonate;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.donorName);
            contact = itemView.findViewById(R.id.donorContact);
            totaldonate = itemView.findViewById(R.id.totaldonate);
            Address = itemView.findViewById(R.id.donorAddress);
            posted = itemView.findViewById(R.id.lastdonate);
        }
    }

    public SearchDonorAdapter(List<DonorData> screenDataList) {
        this.screenDataList = screenDataList;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_donor_item, viewGroup, false);

        return new PostHolder(listitem);
    }

    @Override
    public void onBindViewHolder(PostHolder postHolder, int i) {
        DonorData donorData = screenDataList.get(i);
        postHolder.Name.setText("Name: " + donorData.getName());
        postHolder.contact.setText(donorData.getContact());
        postHolder.Address.setText("Address: " + donorData.getAddress());
        postHolder.totaldonate.setText("Total Donation: " + donorData.getTotalDonate() + " times");
        postHolder.posted.setText("Last Donation: " + donorData.getLastDonate());
    }

    @Override
    public int getItemCount() {
        return screenDataList.size();
    }
}*/
/*package com.android.iunoob.bloodbank.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.viewmodels.DonorData;

import java.util.List;

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.PostHolder> {

    private List<DonorData> screenDataList; // This will hold screen data
    private Context context;

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView Name, Address, contact, posted, totaldonate;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            Name = itemView.findViewById(R.id.donorName);
            contact = itemView.findViewById(R.id.donorContact);
            totaldonate = itemView.findViewById(R.id.totaldonate);
            Address = itemView.findViewById(R.id.donorAddress);
            posted = itemView.findViewById(R.id.lastdonate);

            // Set an onClickListener for the contact TextView to allow calling the donor
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phoneNumber = contact.getText().toString();
                    if (!phoneNumber.isEmpty()) {
                        // Remove any non-numeric characters from the phone number
                        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
                        // Create an intent to dial the phone number
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public SearchDonorAdapter(List<DonorData> screenDataList) {
        this.screenDataList = screenDataList;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_donor_item, viewGroup, false);

        return new PostHolder(listitem);
    }

    @Override
    public void onBindViewHolder(PostHolder postHolder, int i) {
        DonorData donorData = screenDataList.get(i);
        postHolder.Name.setText("Name: " + donorData.getName());

        // Make the name text clickable to view the donor's profile
     /*   postHolder.Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement the logic to view the donor's profile here
                // You can start a new activity to display the profile details.
                Intent intent = new Intent(context, DonorProfileActivity.class);
                intent.putExtra("donorData", donorData); // Pass the donor data to the profile activity
                context.startActivity(intent);
            }
        });

        postHolder.contact.setText(donorData.getContact());
        postHolder.Address.setText("Address: " + donorData.getAddress());
        postHolder.totaldonate.setText("Total Donation: " + donorData.getTotalDonate() + " times");
        postHolder.posted.setText("Last Donation: " + donorData.getLastDonate());
    }

    @Override
    public int getItemCount() {
        return screenDataList.size();
    }
}*/
/*package com.android.iunoob.bloodbank.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.activities.DonorProfileActivity;
import com.android.iunoob.bloodbank.viewmodels.DonorData;

import java.util.List;

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.PostHolder> {

    private List<DonorData> screenDataList; // This will hold screen data
    private Context context;

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView Name, Address, contact, posted, totaldonate;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            Name = itemView.findViewById(R.id.donorName);
            contact = itemView.findViewById(R.id.donorContact);
            totaldonate = itemView.findViewById(R.id.totaldonate);
            Address = itemView.findViewById(R.id.donorAddress);
            posted = itemView.findViewById(R.id.lastdonate);

            // Set an onClickListener for the contact TextView to allow calling the donor
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phoneNumber = contact.getText().toString();
                    if (!phoneNumber.isEmpty()) {
                        // Remove any non-numeric characters from the phone number
                        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
                        // Create an intent to dial the phone number
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public SearchDonorAdapter(List<DonorData> screenDataList) {
        this.screenDataList = screenDataList;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_donor_item, viewGroup, false);

        return new PostHolder(listitem);
    }

    @Override
    public void onBindViewHolder(PostHolder postHolder, int i) {
        DonorData donorData = screenDataList.get(i);
        postHolder.Name.setText("Name: " + donorData.getName());

        // Make the name text clickable to view the donor's profile
        postHolder.Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement the logic to view the donor's profile here
                // Start the DonorProfileActivity and pass the donor data
                Intent intent = new Intent(context, DonorProfileActivity.class);
                intent.putExtra("donorData", donorData); // Pass the donor data to the profile activity
                context.startActivity(intent);
            }
        });

        postHolder.contact.setText(donorData.getContact());
        postHolder.Address.setText("Address: " + donorData.getAddress());
        postHolder.totaldonate.setText("Total Donation: " + donorData.getTotalDonate() + " times");
        postHolder.posted.setText("Last Donation: " + donorData.getLastDonate());
    }

    @Override
    public int getItemCount() {
        return screenDataList.size();
    }
}*/
package com.android.iunoob.bloodbank.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.iunoob.bloodbank.R;
import com.android.iunoob.bloodbank.activities.DonorProfileActivity;
import com.android.iunoob.bloodbank.viewmodels.DonorData;

import java.util.List;

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.PostHolder> {

    private List<DonorData> screenDataList; // This will hold screen data
    private Context context;

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView Name, Address, contact, posted, totaldonate;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            Name = itemView.findViewById(R.id.donorName);
            contact = itemView.findViewById(R.id.donorContact);
            totaldonate = itemView.findViewById(R.id.totaldonate);
            Address = itemView.findViewById(R.id.donorAddress);
            posted = itemView.findViewById(R.id.lastdonate);

            // Set an onClickListener for the contact TextView to allow calling the donor
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phoneNumber = contact.getText().toString();
                    if (!phoneNumber.isEmpty()) {
                        // Remove any non-numeric characters from the phone number
                        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
                        // Create an intent to dial the phone number
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Set an onClickListener for the Name TextView to view the donor's profile
            Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Implement the logic to view the donor's profile here
                    // You can start a new activity to display the profile details.
                    DonorData donorData = screenDataList.get(getAdapterPosition());
                    Intent intent = new Intent(context, DonorProfileActivity.class);
                    intent.putExtra("donorData", donorData); // Pass the donor data to the profile activity
                    context.startActivity(intent);
                }
            });
        }
    }

    public SearchDonorAdapter(List<DonorData> screenDataList) {
        this.screenDataList = screenDataList;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_donor_item, viewGroup, false);

        return new PostHolder(listitem);
    }

    @Override
    public void onBindViewHolder(PostHolder postHolder, int i) {
        DonorData donorData = screenDataList.get(i);
        postHolder.Name.setText("Name: " + donorData.getName());
        postHolder.contact.setText(donorData.getContact());
        postHolder.Address.setText("Address: " + donorData.getAddress());
        postHolder.totaldonate.setText("Total Donation: " + donorData.getTotalDonate() + " times");
        postHolder.posted.setText("Last Donation: " + donorData.getLastDonate());
    }

    @Override
    public int getItemCount() {
        return screenDataList.size();
    }
}
