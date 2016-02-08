package com.nfortics.mfinanceV2.Application;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.cengalabs.flatui.FlatUI;
import com.grabba.Grabba;
import com.grabba.GrabbaDriverNotInstalledException;
import com.nfortics.mfinanceV2.Activities.ConnectActivity;
import com.nfortics.mfinanceV2.AsyncTask.AsynPrinter;
import com.nfortics.mfinanceV2.Models.Account;
import com.nfortics.mfinanceV2.Models.AppInstanceSettings;
import com.nfortics.mfinanceV2.Models.Branch;
import com.nfortics.mfinanceV2.Models.C_Branch;
import com.nfortics.mfinanceV2.Models.C_FingerPrintInfo;
import com.nfortics.mfinanceV2.Models.Collection;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.Favorites;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.Msisdn;
import com.nfortics.mfinanceV2.Models.Product;
import com.nfortics.mfinanceV2.Models.Projects;
import com.nfortics.mfinanceV2.Models.ServiceProvider;
import com.nfortics.mfinanceV2.Models.ThirdPartyIntegration;
import com.nfortics.mfinanceV2.Models.UnsyncedData;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Services.SyncDataService;
import com.nfortics.mfinanceV2.Services.VolleyServices;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Utilities.GPSTracker;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.Volley.VolleySingleton;
import com.nfortics.mfinanceV2.logic.BluetoothChatService;
import com.nfortics.mfinanceV2.logic.CommunicateManager;
import com.nfortics.mfinanceV2.logic.Printer;
import com.nfortics.mfinanceV2.logic.SerialPortService;
import com.nfortics.mfinanceV2.logic.batteryComponent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import roboguice.application.GuiceApplication;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by bigfire on 10/13/2015.
 */
public class Application  extends GuiceApplication {
    public static final DateFormat dateFormatDateOnlySlash = new SimpleDateFormat("dd/MM/yyyy");
    private static Context context;
    public final int NO_CONNECTED = 1000;
    public final int BLUETOOTH_CONNECTED = 1001;
    public final int SERIAL_CONNECTED = 1002;
    public static final String ERROR = "ERROR";
    public static String
            serverURL,
            serverURL2,
            serverURL3,
            nfspURL,
            deviceType,
            appVersion;

    public static FragmentManager fragmentManager;


    private static String currentActivityState;
    private static String pan="";
    private static String cardExpiryDate;
    private static String track2;
    private static String magCardCode1 = "";
    private static String magCardCode2 = "";
    private static String magCardCode3 = "";

    private AsynPrinter printer = null;
    private static OutputStream outStream;
    private static BluetoothSocket socket;
    private batteryComponent mbatteryComponent = null;
    private BluetoothChatService mBluetoothChatService = null;
    private SerialPortService serialPortService = null;
    private CommunicateManager communicateManager = null;
    private HandlerThread handlerThread;
    private Handler mHandler;
    private int isOpen = NO_CONNECTED;
    private static transient List<BluetoothDevice>bluetoothDevices = new ArrayList<BluetoothDevice>();
    public static int screenWidth, screenheight;
       public static String activeAgentMsisdn;
    public static String activeCustomerId;
    public static Map<String,Bitmap> galleryImages=new TreeMap<>();
    public static  Map<String,String> fingerPrintImages=new TreeMap<>();
    public static Map<String,String> signatureImages=new TreeMap<>();


    public static Map<String,String> CollectionItems=new HashMap<>();
    public static Map<String,String> CollectionItemsImages=new HashMap<>();

    public static String activeSignatureLabel;
    public static String activePictureLabel;

    public static List<String> listOfKeysChecked=new ArrayList<>();
    public static List<String> SignatureLabelsList=new ArrayList<>();
    public static List<String> PictureLabelsList=new ArrayList<>();


    public static Map<String,String> afistemplateList=new TreeMap<>();

    public static Map<String,String> checkedImageLabels=new HashMap<>();
    private static List<Map<String,Bitmap>> SignatureMap=new ArrayList<>();
    private static List<Map<String,Bitmap>> PicturesImageMap=new ArrayList<>();
    private static List<Map<String,Bitmap>> galleryImageList=new ArrayList<>();
    private static List<Map<String,Bitmap>> fingerPrintList=new ArrayList<>();
    public static Map<String,String> otherImages= new TreeMap<>();
    public static Map<String,String> basse64Images= new TreeMap<>();

    public static Map<String,String> FingerPrintbase64Images= new TreeMap<>();
    public static String ServerMode;
    UnsyncedData unsyncedData;

    public static VolleySingleton volleySingleton;
    public static RequestQueue requestQueue;

    GPSTracker gpsTracker;


   public  static  User ActiveAgent;

    public static Context getAppContext() {
        return context;
    }






    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DevicePredictor();

        try{

            ServerUrlPredictor();

        }catch (Exception e){



        }

        initializeDB();
        InitializeSettings();
        initializeDB();
        unsyncedData=UnsyncedData.getInstance();
        if (Utils.deviceIsGrabba()) {
            runGrabbaProcesses();
        }

        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();


        Application.setCurrentActivityState("Application");
        addContact();
        Log.d("oxinbo", "current Activity " + com.nfortics.mfinanceV2.Application.Application.getCurrentActivityState());

    }



    void addContact(){


        String DisplayName = "MfinanceSupportLine";
        String MobileNumber = "+233240088705";
        String HomeNumber = "0208897001";
        String WorkNumber = "2334567801";
        String emailID = "email@nfortics.com";
        String company = "nfortics";
        String jobTitle = "mobileEng";

        ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        if (HomeNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Work Numbers
        if (WorkNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if (emailID != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if (!company.equals("") && !jobTitle.equals("")) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void DevicePredictor(){

        AppInstanceSettings appInstanceSettings =null;


        try {
            try {
                appInstanceSettings=  Utils.ApplicationSettings();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();



        }finally {
            deviceType=appInstanceSettings.getDeviceType()==null?"phone":appInstanceSettings.getDeviceType();
        }


    }

    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);


        configurationBuilder.addModelClasses(Favorites.class);
        configurationBuilder.addModelClasses(Branch.class);
        configurationBuilder.addModelClasses(C_Branch.class);
        configurationBuilder.addModelClasses(Merchant.class);
        configurationBuilder.addModelClasses(Projects.class);
        configurationBuilder.addModelClasses(ServiceProvider.class);
        configurationBuilder.addModelClasses(Product.class);
        configurationBuilder.addModelClasses(User.class);
        configurationBuilder.addModelClasses(C_FingerPrintInfo.class);
        configurationBuilder.addModelClasses(Msisdn.class);
        configurationBuilder.addModelClasses(Account.class);
        configurationBuilder.addModelClasses(Customer.class);
        configurationBuilder.addModelClasses(ThirdPartyIntegration.class);
        configurationBuilder.addModelClasses(Collection.class);


        ActiveAndroid.initialize(configurationBuilder.create());
    }

    private void ServerUrlPredictor(){
        AppInstanceSettings appInstanceSettings =null;


        try {
            try {
                appInstanceSettings=  Utils.ApplicationSettings();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String serverUrl;
        Log.d("oxinbo", "from server processor " + appInstanceSettings.getServerMode());

        serverUrl=  appInstanceSettings.getServerMode()==null ?"Production": appInstanceSettings.getServerMode();
        String modeSelected;
        if(serverUrl==null){
            serverUrl="Production";

        }else{
            //GetAppMode
            if(serverUrl.equals("Demo")){
                ServerMode="Demo";
                //ProductionServerUrl1
                serverURL = getString(R.string.DemoServerUrl1);
                serverURL2 = getString(R.string.DemoServerUrl2);
                serverURL3 = getString(R.string.AirtimeServerUrl);
                // deviceType = Utils.GetDeviceType(Application.this);
                // appVersion = getString(R.string.app_version);
                //  nfspURL =  getString(R.string.nfspUrl);
            }else if(serverUrl.equals("Staging")){

                ServerMode="Staging";

                serverURL = getString(R.string.StagingServerUrl1);
                serverURL2 =  getString(R.string.StagingServerUrl2);
                serverURL3 =  getString(R.string.AirtimeServerUrl);
                //  deviceType = Utils.GetDeviceType(Application.this);
                //appVersion = getString(R.string.app_version);
                // nfspURL  = getString(R.string.nfspUrl);

            } else if (serverUrl.equals("Production")){

                ServerMode="Production";

                serverURL = getString(R.string.ProductionServerUrl1);
                serverURL2 =  getString(R.string.ProductionServerUrl2);
                serverURL3 = getString(R.string.AirtimeServerUrl);
                //deviceType = Utils.GetDeviceType(Application.this);
                // appVersion = getString(R.string.app_version);
                // nfspURL  =  getString(R.string.nfspUrl);


            }else{

                // modeSelected="ProductionDefault";
                serverURL = getString(R.string.ProductionServerUrl1);
                serverURL2 =  getString(R.string.ProductionServerUrl2);
                serverURL3 = getString(R.string.AirtimeServerUrl);
                //deviceType = Utils.GetDeviceType(Application.this);
                // appVersion = getString(R.string.app_version);
                // nfspURL  =  getString(R.string.nfspUrl);

            }

        }



        //Log.d("oxinbo","Server Selected ==> "+serverSerializd.getAppMode());

    }

    public static String getActiveAgentMsisdn() {
        return activeAgentMsisdn;
    }

    public static void setActiveAgentMsisdn(String activeAgentMsisdn) {
        Application.activeAgentMsisdn = activeAgentMsisdn;
    }

    public batteryComponent getbatteryComponent() {
        return mbatteryComponent;
    }
    public static void setCardData(String accessCode) {
        if (accessCode == null || accessCode.trim().length() == 0) {
            pan = "";
           cardExpiryDate = "";
            track2 = "";
        } else {
            if (accessCode.contains("=")) {
                // implementation of CCML
                track2 = accessCode;
                String parts[] = accessCode.split("=");
                 pan = parts[0];
                cardExpiryDate = parts[1].substring(0, 4);
            } else {
                pan = accessCode;
            }
        }
    }
  /*
   * public BluetoothChatService getChatService() { return
   * mBluetoothChatService; }
   */

    public static List<Map<String, Bitmap>> getPicturesImageMap() {
        return PicturesImageMap;
    }


    public static Map<String, String> getFingerPrintImages() {
        return fingerPrintImages;
    }

    public static void setFingerPrintImages(Map<String, String> fingerPrintImages) {
        Application.fingerPrintImages = fingerPrintImages;
    }


    public static Map<String, String> getCollectionItems() {
        return CollectionItems;
    }

    public static void setCollectionItems(Map<String, String> collectionItems) {
        CollectionItems = collectionItems;
    }

    public static void setPicturesImageMap(List<Map<String, Bitmap>> picturesImageMap) {
        PicturesImageMap = picturesImageMap;
    }
    //private final Logger logger = LoggerFactory.getLogger();


    public static List<Map<String, Bitmap>> getFingerPrintList() {
        return fingerPrintList;
    }

    public static void setFingerPrintList(List<Map<String, Bitmap>> fingerPrintList) {
        Application.fingerPrintList = fingerPrintList;
    }

    public static List<Map<String, Bitmap>> getSignatureMap() {
        return SignatureMap;
    }

    public static void setSignatureMap(List<Map<String, Bitmap>> signatureMap) {
        SignatureMap = signatureMap;
    }

    public static List<Map<String, Bitmap>> getGalleryImageList() {
        return galleryImageList;
    }

    public static void setGalleryImageList(List<Map<String, Bitmap>> galleryImage) {
        galleryImageList = galleryImage;
    }




    public static   Map<String, Bitmap> getGalleryImages() {
        return galleryImages;
    }

    public static  void setGalleryImages(Map<String, Bitmap> galleryImag) {
        galleryImages = galleryImag;
    }

    private void InitializeSettings() {
        GeneralSettings.initialize(openFile(GeneralSettings.class.getName()));
        UnsyncedData.initialize(openFile(UnsyncedData.class.getName()));
    }

    private InputStream openFile(String fileName) {
        try {
            return openFileInput(fileName);
        } catch (FileNotFoundException e) {
            Log.i(getClass().getName(), fileName + " file not found.");
        }
        return null;
    }

    public static String getCurrentActivityState() {
        return currentActivityState;
    }

    public static void setCurrentActivityState(String currentActivityState) {
        Application.currentActivityState = currentActivityState;
    }

    public static List<BluetoothDevice> getBluetoothDevices() {
        if (bluetoothDevices == null) {
            bluetoothDevices = new LinkedList<BluetoothDevice>();
        }
        return bluetoothDevices;
    }

    public static void setBluetoothDevices(List<BluetoothDevice> bluetoothDev) {
        bluetoothDevices = bluetoothDev;
    }

    public static OutputStream getOutStream() {
        if (outStream == null) {
            outStream = new OutputStream() {

                @Override
                public void write(int oneByte) throws IOException {
                    // TODO Auto-generated method stub

                }
            };
        }
        return outStream;
    }
    public void setPrinterListener() {
        printer = new AsynPrinter(this.getHandlerThread().getLooper(), this.getcommunicateManager());
        printer.setOnPrinterListener(new AsynPrinter.OnPrinterListener() {

            @Override
            public void onPrinterSuccess() {
                ToastUtil.showToast(Application.this, "��ӡ���");
            }

            @Override
            public void onPrinterFail(int code) {
                if (code == Printer.PRINT_NO_PAPER) {
                    ToastUtil.showToast(Application.this, "��ӡ��ȱֽ");
                } else if (code == Printer.PRINT_TIMEOUT) {
                    ToastUtil.showToast(Application.this, "��ӡ��ʱ��ʧ�ܣ�");
                }

            }
        });

    }
    public void Printf(String buffer) {
        try {
            if (isOpen() == NO_CONNECTED) {
                startActivity(new Intent(getApplicationContext(), ConnectActivity.class));
                return;
            }
            printer.Printf(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setSocket(BluetoothSocket soc) {
       socket = soc;
    }
    public static void setOutStream(OutputStream out) {
       outStream = out;
    }

    public static BluetoothSocket getSocket() {
        return socket;
    }


    public static String getPan() {
        return pan;
    }

    public CommunicateManager getcommunicateManager() {
        return communicateManager;
    }

  /*
   * public void setmChatService(BluetoothChatService mChatService) {
   * this.mChatService = mChatService; }
   */


    public void setVoltageListener(Activity currentActivity) {
        if ((mbatteryComponent != null) && ((this.mBluetoothChatService != null) || (this.serialPortService != null))) {
            mbatteryComponent.setVoltageListener(this);
            // mbatteryComponent.setActivity(currentActivity);
        }
    }



    public void CreatebatteryComponent() {
        if (mbatteryComponent == null)
            mbatteryComponent = new batteryComponent(this);
    }


    public int isOpen() {
        return isOpen;
    }

    public void setBluetoothServiceOpen(
            int isOpenStates,
            BluetoothChatService mBluetoothChatService) {
        this.isOpen = isOpenStates;
        if (this.isOpen == BLUETOOTH_CONNECTED)
            this.mBluetoothChatService = mBluetoothChatService;
        else {
            this.mBluetoothChatService = null;
        }
        communicateManager.setCommunicateService(this.mBluetoothChatService, null);
    }

    public void setSerialPortServiceOpen(int isOpenStates, SerialPortService serialPortService) {
        this.isOpen = isOpenStates;
        if (this.isOpen == SERIAL_CONNECTED)
            this.serialPortService = serialPortService;
        else {
            this.serialPortService = null;
        }
        communicateManager.setCommunicateService(null, this.serialPortService);
    }

    private static Activity currentActivity = null;

    public static Activity getCurrentActivity() {
        return Application.currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        Application.currentActivity = currentActivity;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    private void runGrabbaProcesses() {
        try {

            Grabba.open(this, getResources().getString(R.string.app_name));

        } catch (GrabbaDriverNotInstalledException e) {
            // TODO display message that the Grabba driver is not installed
            e.printStackTrace();
           // logger.debug("Driver not installed");
            ToastUtil.showToast(getApplicationContext(), "Install device driver and try again");

        }

    }
    public void batterystart() {
        if (mbatteryComponent != null)
            mbatteryComponent.start();
    }

    public HandlerThread getHandlerThread() {
        return handlerThread;
    }

    public static User getActiveAgent() {
        return ActiveAgent;
    }

    public static void setActiveAgent(User activeAgent) {
        ActiveAgent = activeAgent;
    }

    public void setActivity(Activity currentActivity) {
        if ((mbatteryComponent != null) && ((this.mBluetoothChatService != null) || (this.serialPortService != null))) {
            // mbatteryComponent.setVoltageListener(this);
            mbatteryComponent.setActivity(currentActivity);
        }
    }




}
