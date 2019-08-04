package app.com.diucanteenapp.SharedModel;

import android.os.Parcel;
import android.os.Parcelable;


public class FoodItemModel implements Parcelable {

    private String itemName,itemCategory;
    private double itemPrice;
    private String itemIcon;
    private int itemStockAvailability;

    public FoodItemModel() {
    }

    protected FoodItemModel(Parcel in) {
        itemName = in.readString();
        itemCategory = in.readString();
        itemPrice = in.readDouble();
        itemIcon = in.readString();
        itemStockAvailability = in.readInt();
    }

    public static final Creator<FoodItemModel> CREATOR = new Creator<FoodItemModel>() {
        @Override
        public FoodItemModel createFromParcel(Parcel in) {
            return new FoodItemModel(in);
        }

        @Override
        public FoodItemModel[] newArray(int size) {
            return new FoodItemModel[size];
        }
    };

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }

    public int getItemStockAvailability() {
        return itemStockAvailability;
    }

    public void setItemStockAvailability(int itemStockAvailability) {
        this.itemStockAvailability = itemStockAvailability;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemName);
        parcel.writeString(itemCategory);
        parcel.writeDouble(itemPrice);
        parcel.writeString(itemIcon);
        parcel.writeInt(itemStockAvailability);
    }
}
