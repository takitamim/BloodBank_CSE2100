/*package com.android.iunoob.bloodbank.viewmodels;

public class DonorData {

    private int TotalDonate;
    private String LastDonate, Name, Contact, UID, Address;


    public DonorData() {

    }

    public DonorData(int totalDonate, String lastDonate, String Name, String Contact, String Address, String UID) {
        this.TotalDonate = totalDonate;
        this.LastDonate = lastDonate;
        this.Name = Name;
        this.Contact = Contact;
        this.UID = UID;
        this.Address = Address;
    }

    public int getTotalDonate() {
        return TotalDonate;
    }

    public void setTotalDonate(int totalDonate) {
        this.TotalDonate = totalDonate;
    }

    public String getLastDonate() {
        return LastDonate;
    }

    public void setLastDonate(String lastDonate) {
        this.LastDonate = lastDonate;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setDonorName(String donorName) {
        this.Name = Name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String donorContact) {
        this.Contact = Contact;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}*/
package com.android.iunoob.bloodbank.viewmodels;
import android.os.Parcel;
import android.os.Parcelable;

public class DonorData implements Parcelable {
    private int TotalDonate;
    private String LastDonate, Name, Contact, UID, Address;

    public DonorData() {
    }

    public DonorData(int totalDonate, String lastDonate, String Name, String Contact, String Address, String UID) {
        this.TotalDonate = totalDonate;
        this.LastDonate = lastDonate;
        this.Name = Name;
        this.Contact = Contact;
        this.UID = UID;
        this.Address = Address;
    }

    public int getTotalDonate() {
        return TotalDonate;
    }

    public void setTotalDonate(int totalDonate) {
        this.TotalDonate = totalDonate;
    }

    public String getLastDonate() {
        return LastDonate;
    }

    public void setLastDonate(String lastDonate) {
        this.LastDonate = lastDonate;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setDonorName(String donorName) {
        this.Name = Name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String donorContact) {
        this.Contact = Contact;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    // Parcelable implementation
    protected DonorData(Parcel in) {
        TotalDonate = in.readInt();
        LastDonate = in.readString();
        Name = in.readString();
        Contact = in.readString();
        UID = in.readString();
        Address = in.readString();
    }

    public static final Creator<DonorData> CREATOR = new Creator<DonorData>() {
        @Override
        public DonorData createFromParcel(Parcel in) {
            return new DonorData(in);
        }

        @Override
        public DonorData[] newArray(int size) {
            return new DonorData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(TotalDonate);
        dest.writeString(LastDonate);
        dest.writeString(Name);
        dest.writeString(Contact);
        dest.writeString(UID);
        dest.writeString(Address);
    }
}
