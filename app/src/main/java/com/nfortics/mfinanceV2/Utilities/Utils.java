package com.nfortics.mfinanceV2.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.DataBase.DaoAccess;
import com.nfortics.mfinanceV2.Handlers.BlobHandler;
import com.nfortics.mfinanceV2.Models.Agent;
import com.nfortics.mfinanceV2.Models.AppInstanceSettings;
import com.nfortics.mfinanceV2.Models.Collection;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.OnBoardModel;
import com.nfortics.mfinanceV2.Request.BlobRequest;
import com.nfortics.mfinanceV2.Services.BlobService;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobile2i.api.Printer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import slam.ajni.AJniMethod;

/**
 * Created by bigfire on 10/16/2015.
 */
public class Utils {


    // GeneralSettings  generalSettings;

    public static boolean deviceIsPhone() {
        if (Application.deviceType.equalsIgnoreCase("phone")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean deviceIsM50() {
        if (Application.deviceType.equalsIgnoreCase("m50")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean deviceIsC180() {
        if (Application.deviceType.equalsIgnoreCase("c180")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean deviceIsGrabba() {
        if (Application.deviceType.equalsIgnoreCase("grabba")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean deviceIsTT35A() {
        if (Application.deviceType.equalsIgnoreCase("tt35a")) {
            return true;
        } else {
            return false;
        }
    }
    public static void printInvoice(String msg) {
        if (Utils.deviceIsPhone()) {
            printBluetoothPrinterInvoice(msg);
           // printM35Invoice(msg);
        } else if (Utils.deviceIsC180()) {
            printC180Invoice(msg);
        } else if (Utils.deviceIsM50()) {
            printM50Invoice(msg);
        }
    }
    public static void printBluetoothPrinterInvoice(String msg) {
        Log.i(">>>> INFO Content bluetooth Printer ", msg);
        byte[] byteString = (msg + " ").getBytes();
//    senddatatodevice();

        write(byteString);
    }
    public static void write(byte[] bytes) {
        try {


            //Bitmap bitmap = ((BitmapDrawable) Application.getAppContext().getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
            //generalSettings.getOutStream().write(getBytesFromBitmap(bitmap));
            Application.getOutStream().write(bytes);
            Application.getOutStream().flush();
          //  Application.getOutStream().close();
           // Application.setOutStream(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void printC180Invoice(String msg) {
        try {
            Log.i(">>>> INFO Content NFORTICS Printer ", msg);
            Application application = (Application) Application.getAppContext();
            application.setActivity(application.getCurrentActivity());
            application.Printf(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void printM50Invoice(String content) {
        try {
            Printer printer = new Printer(new Printer.OnStateChangeListener() {
                public void onBusyChange(boolean busy) {
                    Log.d("Printer", "onBusyChange, busy: " + busy);
                }

                public void onPrintingChange(boolean printing) {
                    Log.d("Printer", "onPrintingChange, printing: " + printing);
                }

                public void onNoPaperChange(boolean noPaper) {
                    Log.d("Printer", "onNoPaperChange, noPaper: " + noPaper);
                }
            });
            printer.enable();
            printer.open();
            printer.printText(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printM35Invoice(String content) {
        try {
            Log.d(">>> Content of invoice ", content);
            AJniMethod mJni = new AJniMethod();

            if (mJni.Mini_Init() == -1) {
                mJni = null;
                return;
            }
            // get printer status
            int res = mJni.Mini_printer_status_get();
            if (res != 0x00) {
                if (res == 0x62) {
                    // MessageAlert.s
                    Toast.makeText(Application.getAppContext(), "Printer has no paper", Toast.LENGTH_LONG).show();
                    System.out.println("no paper");
                } else if (res == 0x63) {
                    System.out.println("low power");
                    Toast.makeText(Application.getAppContext(), "Printer has low power", Toast.LENGTH_LONG).show();
                } else if (res == 0x64) {
                    System.out.println("high temperature");
                    Toast.makeText(Application.getAppContext(), "Printer has a high temperature", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("failed");
                    Toast.makeText(Application.getAppContext(), "Printer failed", Toast.LENGTH_LONG).show();
                }
                return;
            }

            System.out.println("printer ok");

            // get printer temperature
            int[] print_temp = new int[2];

            if (mJni.Mini_printer_TempDet(print_temp) == -1) {
                System.out.println("failed");
            } else {
                String strTemp = String.format("temperature: %d", print_temp[0]);
                System.out.println(strTemp);
            }

            print_temp = null;

            // make sure that each print content does not exceed 2K
            byte[] params = new byte[7];
            String tempstr = "";

            // Params-total 7 bytes, of which only params [3] work, other byte
            // temporarily disabled
            // Params [3] take 0x00 ~ 0x03, representing 12,16,24,32 font
            params[3] = 0x02;
            // Set printer
            mJni.Mini_printer_parameter_set(params);
            // Print content, Mini_printer_font_print 1st parameter is font,
            // 0x01 is font, 0x00 is Print blank line
            tempstr = content;
            mJni.Mini_printer_font_print((byte) 0x01, tempstr.getBytes("GBK"));
            // tempstr = "Transaction Type: Buy recharge cards\n"
            // + "Card number:2012 8888 8888 8\n"
            // + "Password:1234 5678 9012 888\n" + "Cost:�500\n"
            // + "Valid until:2012/12/12\n";
            // if font is 24point-line blank point line
            mJni.Mini_printer_font_print((byte) 0x00, null);
            mJni.Mini_printer_font_print((byte) 0x00, null);
            mJni.Mini_printer_font_print((byte) 0x00, null);
            // Start printing
            if (mJni.Mini_printer_start() == -1) {
                System.out.println("Printer failed");
            } else {
                System.out.println("Printed successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean deviceIsSecugen() {
        if (Application.deviceType.equalsIgnoreCase("secu")) {
            return true;
        } else {
            return false;
        }
    }
    public static String getCollectionTypeCode(String type) {

        String t = "";
        if (type.equals("Account Number")) {
            t = "t:0";
        } else if (type.equals("Phone Number")) {
            t = "t:1";
        } else if (type.equals("Customer ID")) {
            t = "t:2";
        }

        return t;
    }
    public static String generateColletionSMSSynthax(Collection collection) {
        //collection.
        Log.d("oxinbo","collection Type Code = "+getCollectionTypeCode(collection.getType())+" Collection Type "+collection.getType());
        String synthax = "";
        synthax = "C a:" + collection.getAgent_msisdn() +
                getCollectionTypeCode(collection.getType())+"num:" + collection.getNumber() +
                " amt:" + collection.getAmount() +
                " acc:" + collection.getAccount_code() +
                " s:" + collection.getSignature();
        return synthax;
    }
    public static String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }

    public static void populateSpinner(Context context, Spinner spinner, String[] haystack) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, haystack);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public static String getBase64Bytes(Bitmap bitmap) {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            bitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);

            byte[] ba = bao.toByteArray();

            return Base64.encodeBytes(ba);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String formatMsisdn(String msisdn) {
        // if (msisdn.startsWith("0") && msisdn.length() < 12) {
        // msisdn = "233" + msisdn.substring(1, msisdn.length());
        // }

        return msisdn;
    }
    public static void setImage(String blobId, final ImageView image) {
        try {
            String avatarId = blobId;
            if (avatarId != null && avatarId.trim().length() > 0) {
                BlobHandler handler = new BlobHandler(Application.getAppContext()) {
                    @Override
                    protected void processMessage(Message message) {
                        image.setImageDrawable((Drawable) message.obj);
                    }
                };
                System.out.println("INFO HEIGHT/3 = " + Application.screenheight / 8);
                new BlobService(handler, Application.getAppContext()).processRequest(new BlobRequest(avatarId, null, Application.screenheight / 8));
            }
        } catch (Exception e) {

        }
    }

    public static int getAPIStatus(JSONObject jsonObject) {
        // the default value should never be the same any of the http status
        // codes returned by the API.
        int statusCode = 12;
        try {
            statusCode = jsonObject.getInt("status");
            Log.i("oxinbo ", String.valueOf(statusCode));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return statusCode;
    }
    public static String getAPIStatusMessage(JSONObject jsonObject) {
        // the default value should never be the same any of the http status
        // codes returned by the API.
        String getAPIStatusMessage = "";
        try {
            getAPIStatusMessage = jsonObject.getString("message");
           // Log.i("oxinbo ", String.valueOf(statusCode));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAPIStatusMessage;
    }
    public static void LogInfo(String className, String info) {
        Log.i(className, info);
    }


    public static String getMessageFromAPIStatus(int i) {

        String results = "";


        switch (i) {
            case 000:

                results = "Operation was successful.";
                break;
            case 100:

                results = "Operation failed..";
                break;
            case 101:

                results = "Customer was not found.";
                break;
            case 102:

                results = "Agent was not found";
                break;
            case 103:
                results = "Agent's bank could not be retrieved.";
                break;
            case 104:
                results = "Search returned no results. Optional message field may be present to\n" +
                        "provide description of result.";
                break;
            case 201:
                results = "Invalid agent_msisdn parameter. Either it was absent or in the wrong\n" +
                        "format";
                break;
            case 202:
                results = "Pin parameter not present.";
                break;
            case 203:
                results = "During search no query parameters (name, customer_msisdn or\n" +
                        "customer_account) were present. There should always be exactly one\n" +
                        "present";
                break;
            case 204:
                results = "Invalid value or absent type parameter. This parameter can only be 0, 1 or 2";
                break;
            case 205:
                results = "Invalid or absent amount parameter. Amount should be a number to 2\n" +
                        "decimal places.";
                break;

            case 206:
                results = "Number parameter absent";
                break;
            case 207:
                results = "Invalid or absent feedback parameter.";
                break;

            case 208:
                results = "One of firstname or lastname parameter absent..";
                break;
////start changng from here
            case 209:
                results = "Phone parameter absent.";
                break;
            case 210:
                results = "Phone number (phone) belongs to an existing customer";
                break;
            case 211:
                results = "Locality parameter absent.";
                break;
            case 212:
                results = "Invalid format submitted for date";
                break;
            case 213:
                results = "Missing from or to date parameters";
                break;
            case 214:
                results = "Missing remarks parameter.";
                break;
            case 301:
                results = "Missing address.";
                break;
            case 302:
                results = "Missing date of birth.";
                break;
            case 303:
                results = "Invalid or absent feedback parameter.";
                break;
            case 304:
                results = "Missing registration ID value.";
                break;
            case 305:
                results = "Missing source or destination account.";
                break;
            case 306:
                results = "Invalid SAP code.";
                break;
            case 307:
                results = "Missing SAP code.";
                break;
            case 308:
                results = "Expired SAP code.";
                break;
            case 309:
                results = "Invalid transaction stage.";
                break;
            case 310:
                results = "Invalid card transaction mode.";
                break;
            case 311:
                results = "Insufficient balance.";
                break;
            case 312:
                results = "Missing PAN.";
                break;
            case 313:
                results = "Missing account or phone number.";
                break;
            case 314:
                results = "Missing track2 data.";
                break;
            case 315:
                results = "Missing expiry date.";
                break;
            case 316:
                results = "Device ID already set.";
                break;
            case 317:
                results = "Missing bill reference.";
                break;
            case 318:
                results = "Missing merchant code.";
                break;

        }


        return results;
    }



    public static String[] getMerchantsAsArray() {

        final List<Merchant> merchantList = Merchant.getAll();
        String[] object = new String[merchantList.size()];
        for (int i = 0; i < merchantList.size(); i++) {
            object[i] = merchantList.get(i).getName();
            Log.d("oxinbo","object "+ object[i]);
        }

        return object;
    }


    public static void showMerchantsDialog() {
        final CharSequence[] items = getMerchantsAsArray();
        final AlertDialog.Builder builder = new AlertDialog.Builder(Application.getAppContext());
        builder.setTitle("Set Merchant");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // userInfoSettings.setCurrentMerchant(userInfoSettings.getMerchants().get(which));

                // String selectedItem= ((AlertDialog)dialog).getListView().getSelectedItem().toString();
                // Log.d("oxinbo","selected merchant "+items[which]);

                try {


                    String selectedMerchant = items[which].toString();

                    DaoAccess.setActiveMerchant(selectedMerchant);

                } catch (Exception e) {


                }

                // setMerchant();
                // saveSettingsInstance(userInfoSettings, USerInfoSettings.class);
                builder.create().dismiss();
            }
        });
        AlertDialog createOptions = builder.create();
        createOptions.show();
    }

    public AlertDialog RetryAlertDialog(Activity context, String message, String title) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        // alertDialogBuilder.
        // alertDialogBuilder.setTitle(title);

        // set dialog message


        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        //
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        return alertDialog;

    }

    public AlertDialog ErrorAlertDialog(Context ctx, String message, String title) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ctx);

        // set title
        // alertDialogBuilder.
        // alertDialogBuilder.setTitle(title);

        // set dialog message

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        //

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        return alertDialog;

    }

    public static void ToastLongmessage(String message) {

        Toast.makeText(Application.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    public static void ToastShortMessage(String message) {

        Toast.makeText(Application.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    public static String convertStreamToString(InputStream is) throws Exception {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Log.i(">>> oxinbo ", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isCCML() {



        Merchant merchant=Merchant.getActiveMerchant("true");
        if (merchant.getCode().equalsIgnoreCase("ccml") || merchant.getCode().equalsIgnoreCase("ccml-test")) {
            return true;
        } else {
            return false;
        }
    }



    public static void RetryDialog(String message,Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("ERROR !!");
        builder.setMessage("Message :\n"+message);



        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                //runTask();
            }
        });
        builder.setNegativeButton("Sync Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void saveHashToFile(Map<String,Map<String,String>> unscyncedMap) throws IOException {
        String fpath = "/sdcard/Unsycnedfile.txt";
        File file=null;
        FileOutputStream f=null;
        ObjectOutputStream s=null;
        try{
            Log.d("oxinbo", "save file stated") ;
          //  String filePath = Application.getAppContext().getFilesDir().getPath().toString() + "/your.properties";
            // File file = new File(filePath);
            file =new File(Application.getAppContext().getExternalFilesDir(null), "maps.txt");
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

             f = new FileOutputStream(file);
             s = new ObjectOutputStream(f);
            s.writeObject(unscyncedMap);




        }catch (Exception e){

            e.printStackTrace();

        }finally {

            if(s!=null){

                s.flush();
                s.close();
            }
        }
        // File file = new File(fpath);



    }
    public static void insertIn2MapDb(OnBoardModel onBoardModel) throws IOException {

        File file=null;
        FileOutputStream f=null;
        ObjectOutputStream s=null;
        try{
            Log.d("oxinbo", "OnBoard save file stated") ;
            //  String filePath = Application.getAppContext().getFilesDir().getPath().toString() + "/your.properties";
            // File file = new File(filePath);
            file =new File(Application.getAppContext().getExternalFilesDir(null), "mapsDb.txt");
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            f = new FileOutputStream(file);
            s = new ObjectOutputStream(f);
            s.writeObject(onBoardModel);

            Log.d("oxinbo", "OnBoard Data saved to file") ;


        }catch (Exception e){

            e.printStackTrace();

        }finally {

            if(s!=null){

                s.flush();
                s.close();
            }
        }
        // File file = new File(fpath);



    }

    public static void insertAgentData(Agent agent) throws IOException {

        File file=null;
        FileOutputStream f=null;
        ObjectOutputStream s=null;
        try{
            Log.d("oxinbo", "Agent save file stated") ;
            //  String filePath = Application.getAppContext().getFilesDir().getPath().toString() + "/your.properties";
            // File file = new File(filePath);
            file =new File(Application.getAppContext().getExternalFilesDir(null), "agentDb.txt");
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            f = new FileOutputStream(file);
            s = new ObjectOutputStream(f);
            s.writeObject(agent);

            Log.d("oxinbo", "Agent Data saved to file") ;


        }catch (Exception e){

            e.printStackTrace();

        }finally {

            if(s!=null){

                s.flush();
                s.close();
            }
        }
        // File file = new File(fpath);



    }
    public static String getFullName(Map<String,String> entry){


        String firstName=entry.get("first_name");

        String lastname=entry.get("last_name");



        if(firstName!=null && lastname!=null)
            return firstName+"  "+lastname;
        else
            return "";

    }
    public static Map<String,Map<String,String>> unscyncedMapFromFile() throws OptionalDataException, ClassNotFoundException, IOException,EOFException{

        FileInputStream f=null;
        File file =null;
        ObjectInputStream s=null;
        Map<String,Map<String,String>>  hashFromFile=new TreeMap<>();
        String fpath = "/sdcard/Unsycnedfile.txt";

        try{
            file =new File(Application.getAppContext().getExternalFilesDir(null), "maps.txt");
            f = new FileInputStream(file);

            s = new ObjectInputStream(f);

            hashFromFile=( Map<String,Map<String,String>>) s.readObject();

            // Log.e("hashfromfileLOCAL", "" + hashFromFile);

        }catch (Exception e){



        }finally {
            if(s!=null){
                s.close();

            }
        }


        return hashFromFile;
    }
    public static OnBoardModel MapsDb( ) throws OptionalDataException, ClassNotFoundException, IOException,
            EOFException{

        FileInputStream f=null;
        File file =null;
        ObjectInputStream s=null;
        OnBoardModel onBoardModels  =new OnBoardModel();


        try{
            file =new File(Application.getAppContext().getExternalFilesDir(null), "mapsDb.txt");
            f = new FileInputStream(file);

            s = new ObjectInputStream(f);

            onBoardModels=(OnBoardModel) s.readObject();


        }catch (Exception e){

            Utils.log("error occured while reading map ");
     e.printStackTrace();

        }finally {
            if(s!=null){
                s.close();

            }
        }


        return onBoardModels;
    }


    public static Agent AgentData( )
            throws
            OptionalDataException,
            ClassNotFoundException,
            IOException,
            EOFException{

        FileInputStream f=null;
        File file =null;
        ObjectInputStream s=null;
        Agent agent  =new Agent();


        try{
            file =new File(Application.getAppContext().getExternalFilesDir(null), "agentDb.txt");
            f = new FileInputStream(file);

            s = new ObjectInputStream(f);

            agent=(Agent) s.readObject();


        }catch (Exception e){

            Utils.log("error occured while reading map ");
            e.printStackTrace();

        }finally {
            if(s!=null){
                s.close();

            }
        }


        return agent;
    }


    public static AppInstanceSettings ApplicationSettings()  throws
            OptionalDataException,
            ClassNotFoundException,
            IOException,
            EOFException{

        FileInputStream f=null;
        File file =null;
        ObjectInputStream s=null;
        AppInstanceSettings appInstanceSettings  =new AppInstanceSettings();


        try{
            file =new File(Application.getAppContext().getExternalFilesDir(null), "ApplicationSettings.txt");
            f = new FileInputStream(file);

            s = new ObjectInputStream(f);

            appInstanceSettings=(AppInstanceSettings) s.readObject();


        }catch (Exception e){

            Utils.log("error occured while reading map ");
            e.printStackTrace();

        }finally {
            if(s!=null){
                s.close();

            }
        }


        return appInstanceSettings;
    }
    public static void insertAppInstanceSettings(AppInstanceSettings appInstanceSettings) throws IOException {

        File file=null;
        FileOutputStream f=null;
        ObjectOutputStream s=null;
        try{
            Log.d("oxinbo", "Agent save file stated") ;
            //  String filePath = Application.getAppContext().getFilesDir().getPath().toString() + "/your.properties";
            // File file = new File(filePath);
            file =new File(Application.getAppContext().getExternalFilesDir(null), "ApplicationSettings.txt");
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            f = new FileOutputStream(file);
            s = new ObjectOutputStream(f);
            s.writeObject(appInstanceSettings);

            Log.d("oxinbo", "Agent Data saved to file") ;


        }catch (Exception e){

            e.printStackTrace();

        }finally {

            if(s!=null){

                s.flush();
                s.close();
            }
        }
        // File file = new File(fpath);



    }
    public static List<String> getAllTransactionsAccounts() {
        List<String> accounts = new ArrayList<String>();

        accounts.add("R-regular Account");
        accounts.add("S-regular Account");
        accounts.add("I-Regular Account");
        accounts.add("M-regular Account");
        accounts.add("Semanhyia Account");
        accounts.add("Masetenapa Account");
        accounts.add("R-regular Savings");
        accounts.add("S-regular Savings");
        accounts.add("I-Regular Savings");
        accounts.add("M-regular Savings");
        accounts.add("Semanhyia Savings");
        accounts.add("Masetenapa Savings");
        accounts.add("E- Regular Savings Account");
        accounts.add("I- Regular Savings");
        accounts.add("Masetenapa Savings");
        accounts.add("M-Regular Savings Ac");
        accounts.add("R- Regular Savings");
        accounts.add("Semanhyia Susu");
        accounts.add("S-Regular Savings");
        accounts.add("Staff Regular Plus Savings");
        accounts.add("Staff Regular Savings");

        accounts.add("R regular Account");
        accounts.add("S regular Account");
        accounts.add("I Regular Account");
        accounts.add("M regular Account");
        accounts.add("R regular Savings");
        accounts.add("S regular Savings");
        accounts.add("I Regular Savings");
        accounts.add("M regular Savings");
        accounts.add("E Regular Savings Account");
        accounts.add("I Regular Savings");
        accounts.add("M Regular Savings Ac");
        accounts.add("R Regular Savings");
        accounts.add("S Regular Savings");

        accounts.add("Regular Savings");
        accounts.add("Semanhyia Susu");
        accounts.add("Masetenapa Savings");
        accounts.add("R- Regular Savings");
        accounts.add("M-Regular Savings Ac");
        accounts.add("Staff Everyday Account");
        accounts.add("Everyday Account Basic Micro Banking");
        accounts.add("Everyday Account Plus Micro Banking");
        accounts.add("Everyday Account Star Micro Banking");
        accounts.add("Everyday Account Basic Consumer Banking");
        accounts.add("Everyday Account Plus Consumer Banking");
        accounts.add("Everyday Account Star Consumer Banking");
        accounts.add("Everyday Account Basic Business Banking");
        accounts.add("Everyday Account Plus Business Banking");
        accounts.add("Everyday Account Star Business Banking");

        return accounts;
    }
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Application.getAppContext().getExternalFilesDir(null), albumName);
        if (!file.mkdirs()) {
            Log.e("oxinbo", "Directory not created");
        }
        return file;
    }

    public  static void log(String log){

        Log.d("oxinbo",log);
    }
    public static List<String> getBalanceStatementAccounts() {
        List<String> accounts = new ArrayList<String>();

        accounts.add("R-regular Account");
        accounts.add("S-regular Account");
        accounts.add("I-Regular Account");
        accounts.add("M-regular Account");
        accounts.add("Semanhyia Account");
        accounts.add("Masetenapa Account");
        accounts.add("R-regular Savings");
        accounts.add("S-regular Savings");
        accounts.add("I-Regular Savings");
        accounts.add("M-regular Savings");
        accounts.add("Semanhyia Savings");
        accounts.add("Masetenapa Savings");
        accounts.add("Medaakypa");
        accounts.add("Mebotae");
        accounts.add("Big Dream");
        accounts.add("Combo reserve");
        accounts.add("E- Regular Savings Account");
        accounts.add("I- Regular Savings");
        accounts.add("Masetenapa Savings");
        accounts.add("M-Regular Savings Ac");
        accounts.add("R- Regular Savings");
        accounts.add("Semanhyia Susu");
        accounts.add("S-Regular Savings");
        accounts.add("Staff Regular Plus Savings");
        accounts.add("Staff Regular Savings");
        accounts.add("Medaakyepa Savings");
        accounts.add("Mebotae  Savings Account");
        accounts.add("Big Dream");
        accounts.add("Big Dream Savings");
        accounts.add("Combo reserve");

        accounts.add("R regular Account");
        accounts.add("S regular Account");
        accounts.add("I Regular Account");
        accounts.add("M regular Account");
        accounts.add("R regular Savings");
        accounts.add("S regular Savings");
        accounts.add("I Regular Savings");
        accounts.add("M regular Savings");
        accounts.add("E Regular Savings Account");
        accounts.add("I Regular Savings");
        accounts.add("M Regular Savings Ac");
        accounts.add("R Regular Savings");
        accounts.add("S Regular Savings");

        accounts.add("Staff Reserve Account");
        accounts.add("Reserve Account");
        accounts.add("Uncommon Call Account");

        return accounts;
    }
}