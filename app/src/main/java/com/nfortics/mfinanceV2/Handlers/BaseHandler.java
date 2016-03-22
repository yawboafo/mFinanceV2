package com.nfortics.mfinanceV2.Handlers;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Application.ApplicationInitialisationException;
import com.nfortics.mfinanceV2.MessageAlert;
import com.nfortics.mfinanceV2.Request.HttpStatusException;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * Created by bigfire on 11/16/2015.
 */
public abstract class BaseHandler extends Handler {

    protected final Context context;
    private static ProgressDialog progressDialog;
    private Message message;
    private String requestClassName;

    public BaseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        this.message = msg;
        super.handleMessage(message);
        try {
            Bundle bundle = this.message.getData();
            requestClassName = bundle.getString("request_class_name");
            processErrors(message);

            if (message.arg1 == 0)
                processMessage(message);
            else
                processStatusCode(message.arg1);

        } catch (ConnectException e) {
            Log.e(getClass().getName(),
                    "Failed to handle message - connection failed.", e);
            Toast.makeText(
                    context,
                    "Connection failure. Please check your internet connection.",
                    Toast.LENGTH_LONG).show();
        } catch (UnknownHostException e) {
            Log.e(getClass().getName(),
                    "Failed to handle message - connection failed.", e);
            Toast.makeText(
                    context,
                    "Connection failure. Please check your internet connection.",
                    Toast.LENGTH_LONG).show();
        } catch (HttpStatusException e) {
            Log.e(getClass().getName(),
                    "Failed to handle message - login error?", e);
            if (e.getHttpStatus() == 408) {
                Toast.makeText(
                        context,
                        "Server is taking too Long to respond. Please try again later.",
                        Toast.LENGTH_LONG).show();
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

        } catch (ApplicationInitialisationException e) {
            Log.e(getClass().getName(), "Application initialisation failed.", e);
            Toast.makeText(context, "Application initialisation failed.",
                    Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            Log.i("Im NUll", "NULL NULL NULL");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(getClass().getName(), "Failed to handle message.", e);
            Toast.makeText(context, "Oops! Can you try again?",
                    Toast.LENGTH_LONG).show();
            // MessageAlert.showMessage(e.toString(), context);
        } finally {
            dismissProgressDialog();
        }
    }

    protected abstract ProgressDialog createProgressDialog();

    protected abstract void processMessage(Message message);

    protected void processErrors(Message message) throws Exception {
        Serializable error = message.getData().getSerializable(Application.ERROR);
        if (error != null) {
            throw (Exception) error;
        }
    }

    public void showProgressDialog() {
        if (progressDialog != null) {
            dismissProgressDialog();
        }
        progressDialog = createProgressDialog();
    }

    public void dismissProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "Failed to close progress dialog.", e);
        } finally {
            progressDialog = null;
        }
    }

    private void processStatusCode(int code) {
        Utils.LogInfo(getClass().getName(), "Status exception = " + code);
        switch (code) {

            case 100:
                // Utils.LogInfo(getClass().getName(),
                // "Oops, there was an operational error please try again later.");
                // if
                // (requestClassName.equalsIgnoreCase(LoginRequest.class.getName()))
                // {
                // sending arg2 to LoginActivity to prompt user to set pin.
                message.arg2 = 100;
                processMessage(message);
                // }
                break;

            case 101:
                // Utils.LogInfo(getClass().getName(),
                // "You are not registered to use this service.");
                break;

            case 102:
                Utils.LogInfo(getClass().getName(),
                        "You are not registered to use this service.");
                MessageAlert.showMessage(
                        "You are not registered to use this service.", context);
                break;

            case 103:
                message.arg2 = 103;
                processMessage(message);
                break;

            case 104:
                Utils.LogInfo(getClass().getName(), ">>> INFO Status code = "
                        + code + " Customer does not exist.");
                // if (requestClassName.equalsIgnoreCase(CustomerSearchRequest.class
                // .getName())) {
                // setting arg2 to 104 to indicate the customer does not exist .
                message.arg2 = 104;
                // sending arg2 to .
                // message.arg2 = 1;
                processMessage(message);
                // }
                break;

            case 105:

                break;

            case 211:
                Utils.LogInfo(getClass().getName(), ">>> INFO Status code = "
                        + code + " Locality is absent.");
                break;

            case 201:
                Utils.LogInfo(getClass().getName(), "Invalid Mobile Number.");
                MessageAlert.showMessage("Invalid Mobile Number.", context);
                break;

            case 202:
                message.arg2 = 202;
                processMessage(message);

                break;

            case 203:
                Utils.LogInfo(getClass().getName(),
                        "No search parameter was passed in api call");
                message.arg2 = 203;
                processMessage(message);
                break;

            case 204:
                message.arg2 = 204;
                // sending arg2 to .
                // message.arg2 = 1;
                processMessage(message);
                break;

            case 205:
                message.arg2 = 205;
                // sending arg2 to .
                // message.arg2 = 1;
                processMessage(message);
                break;

            case 206:
                Utils.LogInfo(getClass().getName(), ">>> INFO Status code = "
                        + code + " Number parameter is absent");
                message.arg2 = 206;
                // sending arg2 to .
                // message.arg2 = 1;
                processMessage(message);
                break;

            case 207:
                message.arg2 = 207;
                // sending arg2 to .
                // message.arg2 = 1;
                processMessage(message);
                break;

            case 208:

                break;

            case 209:

                break;

            case 210:

                break;

            case 111:
                message.arg2 = 111;
                // sending arg2 to .
                // message.arg2 = 1;
                processMessage(message);
                break;
            default:
                message.arg2 = message.arg1;
                processMessage(message);
                break;
        }
    }
}

