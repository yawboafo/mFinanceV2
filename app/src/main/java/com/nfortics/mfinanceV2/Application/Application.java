package com.nfortics.mfinanceV2.Application;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.cengalabs.flatui.FlatUI;
import com.grabba.Grabba;
import com.grabba.GrabbaDriverNotInstalledException;
import com.nfortics.mfinanceV2.Activities.ConnectActivity;
import com.nfortics.mfinanceV2.AsyncTask.AsynPrinter;
import com.nfortics.mfinanceV2.Models.Account;
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
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.Utilities.Utils;
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
    public static int screenWidth, screenheight;
       public static String activeAgentMsisdn;

    private Map<String,Bitmap> galleryImages=new HashMap<>();
    private static List<Map<String,Bitmap>> galleryImageList=new ArrayList<>();
    //private final Logger logger = LoggerFactory.getLogger();


    public static List<Map<String, Bitmap>> getGalleryImageList() {
        return galleryImageList;
    }

    public static void setGalleryImageList(List<Map<String, Bitmap>> galleryImage) {
       galleryImageList = galleryImage;
    }

    private static transient List<BluetoothDevice>bluetoothDevices = new ArrayList<BluetoothDevice>();


    public Map<String, Bitmap> getGalleryImages() {
        return galleryImages;
    }

    public void setGalleryImages(Map<String, Bitmap> galleryImages) {
        this.galleryImages = galleryImages;
    }

    private void InitializeSettings() {
        GeneralSettings.initialize(openFile(GeneralSettings.class.getName()));

    }

    private InputStream openFile(String fileName) {
        try {
            return openFileInput(fileName);
        } catch (FileNotFoundException e) {
            Log.i(getClass().getName(), fileName + " file not found.");
        }
        return null;
    }

   public  static  User ActiveAgent;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        deviceType = "phone";
        ServerUrlPredictor("Production");
        initializeDB();
        InitializeSettings();
        initializeDB();
        if (Utils.deviceIsGrabba()) {
            runGrabbaProcesses();
        }
     Application.setCurrentActivityState("Application");


        Log.d("oxinbo", "current Activity " + com.nfortics.mfinanceV2.Application.Application.getCurrentActivityState());

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

    private void ServerUrlPredictor(String val){


        String serverUrl;
        Log.d("oxinbo", "from server processor " + val);

        serverUrl= val==null ?"Production":val;
        String modeSelected;
        if(serverUrl==null){
            val="Production";

        }else{
            //GetAppMode
            if(serverUrl.equals("Demo")){
                modeSelected="Demo";
                //ProductionServerUrl1
                serverURL = getString(R.string.DemoServerUrl1);
                serverURL2 = getString(R.string.DemoServerUrl2);
                serverURL3 = getString(R.string.AirtimeServerUrl);
                // deviceType = Utils.GetDeviceType(Application.this);
                // appVersion = getString(R.string.app_version);
                //  nfspURL =  getString(R.string.nfspUrl);
            }else if(serverUrl.equals("Staging")){

                modeSelected="Staging";

                serverURL = getString(R.string.StagingServerUrl1);
                serverURL2 =  getString(R.string.StagingServerUrl2);
                serverURL3 =  getString(R.string.AirtimeServerUrl);
                //  deviceType = Utils.GetDeviceType(Application.this);
                //appVersion = getString(R.string.app_version);
                // nfspURL  = getString(R.string.nfspUrl);

            } else if (serverUrl.equals("Production")){

                // modeSelected="Production";

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
