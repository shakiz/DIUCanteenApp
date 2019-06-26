package app.com.diucanteenapp.SharedModel;

public class FoodItemModel {

    private String itemName,itemCategory;
    private double itemPrice;
    private String itemIcon;
    private int itemStockAvailability;

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
}
