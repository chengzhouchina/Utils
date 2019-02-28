package com.example.utils.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.utils.json.CallInfo;
import com.example.utils.json.PhoneDto;
import com.example.utils.json.SmsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhoneUtil {
    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    //上下文对象
    private Context context;
    //联系人提供者的uri
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    public PhoneUtil(Context context) {
        this.context = context;
    }

    //获取所有联系人
    public List<PhoneDto> getPhone() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限  第二个参数是一个 数组 说明可以同时申请多个权限
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
        }

        List<PhoneDto> phoneDtos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri, new String[]{NUM, NAME}, null, null, null);
        while (cursor.moveToNext()) {
            PhoneDto phoneDto = new PhoneDto(cursor.getString(cursor.getColumnIndex(NAME)), cursor.getString(cursor.getColumnIndex(NUM)));
            phoneDtos.add(phoneDto);
        }
        Log.e("mytest", "getPhone: --" + phoneDtos.toString());
        return phoneDtos;
    }

    /**
     * 获取短信
     *
     * @return
     */
    public List getSms() {
        List<SmsInfo> smsInfos = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            //如果不同意，就去请求权限   参数1：上下文，2：权限，3：请求码
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_SMS}, 1);
        }
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Uri smsUri = Uri.parse("content://sms/");
        Cursor cursor = context.getContentResolver().query(smsUri, projection, null, null, "date desc");
        String smsType;
        String smsName;
        String smsNumber;
        String smsBody;
        String smsTime;
        String smsDate;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                smsType = cursor.getString(cursor.getColumnIndex("type"));
                int type = Integer.parseInt(smsType);
                if (type == 1) {
                    smsType = "接收";
                } else if (type == 2) {
                    smsType = "发送";
                } else {
                    smsType = "其他";
                }

                smsName = cursor.getString(cursor.getColumnIndex("person"));
                if (smsName == null) {
                    smsName = "未知号码";
                }
                smsNumber = cursor.getString(cursor.getColumnIndex("address"));
                smsName = getContactNameByAddr(context, smsNumber);
                smsBody = cursor.getString(cursor.getColumnIndex("body"));
                smsDate = cursor.getString(cursor.getColumnIndex("date"));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d = new Date(Long.parseLong(smsDate));
                smsTime = dateFormat.format(d);
                SmsInfo smsInfo = new SmsInfo(smsType, smsName, smsNumber, smsBody, smsDate);
                smsInfos.add(smsInfo);
            }
            cursor.close();
        }
        return smsInfos;
    }

    /**
     * 通过号码获取name
     * @param context
     * @param phoneNumber
     * @return
     */
    public static String getContactNameByAddr(Context context,
                                              String phoneNumber) {

        Uri personUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cur = context.getContentResolver().query(personUri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cur.moveToFirst()) {
            int nameIdx = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cur.getString(nameIdx);
            cur.close();
            return name;
        }
        return "未知号码";
    }

    /**
     * 获取通话记录
     * @return
     */
    public List<CallInfo> getCallLog() {
        List<CallInfo> infos = new ArrayList<CallInfo>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{CallLog.Calls.NUMBER, CallLog.Calls.DATE,
                CallLog.Calls.TYPE};
        Cursor cursor = cr.query(uri, projection, null, null, null);
        while (cursor.moveToNext()) {
            String number = cursor.getString(0);
            long date = cursor.getLong(1);
            int type = cursor.getInt(2);
            infos.add(new CallInfo(number, date, type));
        }
        cursor.close();
        return infos;
    }
}
