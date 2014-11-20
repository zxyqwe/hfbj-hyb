//package com.hfbj.hyb.test;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.IBinder;
//import android.os.RemoteException;
//
//public class updateCheckAsync extends Service{
//    checkRequest.Stub mDownloadRequestImpl = new checkRequest.Stub() {
//            @Override
//            public void check(checkCallback callback)
//                throws RemoteException {
//            	callback.sendCheck(App_net.version());
//            }
//
//	};
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mDownloadRequestImpl;
//    }
//    public static Intent makeIntent(Context context) {
//        return new Intent(context, updateCheckAsync.class);
//    }
//}
