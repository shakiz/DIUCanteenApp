package app.com.diucanteenapp.Student.Ac.Activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.com.diucanteenapp.Admin.DatabaseHelper.StoreFoodItemData;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.SharedDatabaseClasses.DatabaseHelperPlaceOrder;
import app.com.diucanteenapp.SharedModel.OrderItemModel;
import app.com.diucanteenapp.Student.Ac.DatabaseHelper.DatabaseHelperSaveCartDetails;

public class PaymentAndOrderActivity extends AppCompatActivity {

    private String TAG = "PaymentOrderActivity";
    private ImageView qrCodeImageView;
    private TextView timer,time,totalAmountTXT;
    private Button orderAfterPayment;
    private String itemName;
    private Integer itemQuantity,stock,totalAmount=0;
    private StoreFoodItemData storeFoodItemData;
    private  long startTime = 0;
    private ArrayList<String> paymentMethodsArrayList;
    private DatabaseHelperPlaceOrder databaseHelperPlaceOrder;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<OrderItemModel> orderItemModelList;
    private Spinner paymentMethodSpinner;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            time.setText("Your food will be delivered : "+String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_and_order);
        //This method is used to initialize the attributes with xml
        init();
        //This method will be used to get data from previous activity or fragment
        getAndSetData();

        setSpinnerAdapter();
        //Setting the on click listener for order button which will pop up a QR code for the order
        orderAfterPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here we are telling that after placing the order a qr code will also be generated
                //and later it will show on a pop dialog
                //this  posts a message to the main thread from our timertask
                //and updates the text field
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                final Dialog dialog=new Dialog(PaymentAndOrderActivity.this);
                dialog.setContentView(R.layout.custom_layout_for_qr_code);
                //This method helps to initialize the dialog components with xml
                initDialogComponents(dialog);
                //This method generates QR code and set it to imageView
                generateQRCodeAndSetToImageView(totalAmount);
                dialog.setTitle("QR code");
                try{
                    dialog.show();
                }
                catch (Exception e){
                    Log.v(TAG,e.getMessage());
                }
//                if (storeFoodItemData.updateItemStock(itemName,(stock-itemQuantity))==true){
//                    Toast.makeText(getApplicationContext(),"Updated stock",Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(),"Update failed",Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }



    public void setSpinnerAdapter(){
        setSpinnerData();
        spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,paymentMethodsArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(spinnerAdapter);
    }

    private void setSpinnerData(){
        paymentMethodsArrayList.add("Select payment method");
        paymentMethodsArrayList.add("Rocket");
        paymentMethodsArrayList.add("bKash");
        paymentMethodsArrayList.add("TCash");
        paymentMethodsArrayList.add("SureCash");
    }

    private void getAndSetData() {
        orderItemModelList = databaseHelperPlaceOrder.getAllFoodOrders();
        for(int start=0;start<orderItemModelList.size();start++){
            Log.v(TAG,"Name  : "+orderItemModelList.get(start).getItemName()+ " :: Amount : "+orderItemModelList.get(start).getAmount());
            totalAmount += orderItemModelList.get(start).getAmount();
        }

        totalAmountTXT.setText("Total amount = "+totalAmount);

    }

    private void initDialogComponents(Dialog dialog) {
        qrCodeImageView=dialog.findViewById(R.id.qrCodeImageXML);
    }

    public void init(){
        orderAfterPayment=findViewById(R.id.orderButtonXMLPayment);
        storeFoodItemData=new StoreFoodItemData(getApplicationContext());
        databaseHelperPlaceOrder = new DatabaseHelperPlaceOrder(this);
        paymentMethodSpinner = findViewById(R.id.paymentMethodsSpinner);
        totalAmountTXT = findViewById(R.id.TotalAmount);
        paymentMethodsArrayList = new ArrayList<>();
        orderItemModelList = new ArrayList<>();
        time=findViewById(R.id.time);
    }

    private void generateQRCodeAndSetToImageView(int totalAmount) {
        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try{
            //First we are creating a bitMatrix by the help of multiFormatter
            //Second we need a barcodeEncoder
            //Third we are encoding a bitMap by the help of barcodeEncoder
            //And finally setting the bitMap to our imageView for showing the barCode
            BitMatrix bitMatrix = multiFormatWriter.encode(""+totalAmount, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            Log.v(TAG,e.getMessage());
        }
    }



}
