package com.solmi.biobrainexample.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    public final static String Myaction = "com.solmi.biobrainexample.ACTION_MY_BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
 /*     StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Log.d(TAG, log);
        Toast.makeText(context, log, Toast.LENGTH_LONG).show();*/

        Toast.makeText(context, "onReceive>>>", Toast.LENGTH_LONG).show();

        if(Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())){
            Toast.makeText(context,"충전기가 연결되었습니다.",Toast.LENGTH_SHORT).show();
        }

        if(Myaction.equals(intent.getAction())){
            Toast.makeText(context,"버튼이 눌렸습니다.",Toast.LENGTH_SHORT).show();


        }

         /*  final PendingResult pendingResult = goAsync();
        Task asyncTask = new Task(pendingResult, intent);
        asyncTask.execute();*/
    }

    private static class Task extends AsyncTask<String, Integer, String> {

        private final PendingResult pendingResult;
        private final Intent intent;

        private Task(PendingResult pendingResult, Intent intent) {
            this.pendingResult = pendingResult;
            this.intent = intent;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            sb.append("Action: " + intent.getAction() + "\n");
            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
            String log = sb.toString();
            Log.d(TAG, log);
            return log;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish();
        }
    }
}
