package app.com.diucanteenapp.Student.Ac.Activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import app.com.diucanteenapp.Admin.DatabaseHelper.StoreFoodItemData;
import app.com.diucanteenapp.R;

public class PaymentAndOrderActivity extends AppCompatActivity {

    private String TAG = "PaymentOrderActivity";
    private ImageView qrCodeImageView;
    private Button orderAfterPayment;
    private String itemName;
    private Integer itemQuantity,stock;
    private StoreFoodItemData storeFoodItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_and_order);
        //This method is used to initialize the attributes with xml
        init();
        //This method will be used to get data from previous activity or fragment
        getIntentData();
        //Setting the on click listener for order button which will pop up a QR code for the order
        orderAfterPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here we are telling that after placing the order a qr code will also be generated
                //and later it will show on a pop dialog
                final Dialog dialog=new Dialog(PaymentAndOrderActivity.this);
                dialog.setContentView(R.layout.custom_layout_for_qr_code);
                //This method helps to initialize the dialog components with xml
                initDialogComponents(dialog);
                //This method generates QR code and set it to imageView
                generateQRCodeAndSetToImageView(itemName,itemQuantity);
                dialog.setTitle("QR code");
                try{
                    dialog.show();
                }
                catch (Exception e){
                    Log.v(TAG,e.getMessage());
                }
                if (storeFoodItemData.updateItemStock(itemName,(stock-itemQuantity))==true){
                    Toast.makeText(getApplicationContext(),"Updated stock",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Update failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getIntentData() {
        itemName=getIntent().getStringExtra("name");
        itemQuantity=getIntent().getIntExtra("quantity",0);
        stock=getIntent().getIntExtra("stock",0);
        Log.v(TAG,""+itemName+" : "+itemQuantity + " : "+stock);
    }

    private void initDialogComponents(Dialog dialog) {
        qrCodeImageView=dialog.findViewById(R.id.qrCodeImageXML);
    }

    public void init(){
        orderAfterPayment=findViewById(R.id.orderButtonXMLPayment);
        storeFoodItemData=new StoreFoodItemData(getApplicationContext());
    }

    private void generateQRCodeAndSetToImageView(String itemName,int itemQuantity) {
        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try{
            //First we are creating a bitMatrix by the help of multiFormatter
            //Second we need a barcodeEncoder
            //Thirs we are encoding a bitMap by the help of barcodeEncoder
            //And finally setting the bitMap to our imageView for showing the barCode
            BitMatrix bitMatrix = multiFormatWriter.encode(itemName+itemQuantity, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            Log.v(TAG,e.getMessage());
        }
    }
}
