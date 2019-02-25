package com.example.utils.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class GetPhoneMessage {

    //获取android ID
    public static String getAndroidID(Activity activity) {
        return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //获取手机运营商
    public static String getNetworkOpertator(Activity activity){
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperator();
    }

    //获取手机mac
    public static String getMac(Activity activity) {
        WifiManager wifi = (WifiManager) activity.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    //获取屏幕密度
    public static float getDensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        return dm.density;
    }
    //获取屏幕宽度
    public static String getWidth(Activity activity) {
        return String.valueOf(activity.getResources().getDisplayMetrics().widthPixels);
    }
    //获取屏幕高度
    public static String getHeight(Activity activity) {
        return String.valueOf(activity.getResources().getDisplayMetrics().heightPixels);
    }

    //获取手机IMEI
    public static String getIMEI(Activity activity) {
        //需要“android.permission.READ_PHONE_STATE”权限
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //如果不同意，就去请求权限   参数1：上下文，2：权限，3：请求码
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    //获取api版本
    public static String getAPI(){
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    //获取android系统版本
    public static String getPhoneVersion(){
        return String.valueOf(Build.VERSION.RELEASE);
    }

    //获取手机设备型号
    public static String getPhoneModel(){
        return String.valueOf(Build.MODEL);
    }

    //获取手机品牌
    public static String getPhoneBrand(){
        return String.valueOf(Build.BRAND);
    }


    //获取联网类型
    public static String GetNetworkType(Activity activity) {
        String strNetworkType = "";
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD://api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP://api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE://api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
                Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }
        Log.e("cocos2d-x", "Network Type : " + strNetworkType);
        return strNetworkType;
    }
}
