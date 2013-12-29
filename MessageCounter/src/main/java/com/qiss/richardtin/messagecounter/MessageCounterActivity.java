package com.qiss.richardtin.messagecounter;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageCounterActivity extends Activity {
    static final public String TAG = "MessageCounter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_counter);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_counter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        final private Uri MMS_CONTENT_URI = Uri.parse("content://mms");
        final private Uri SMS_CONTENT_URI = Uri.parse("content://sms");

        static final String[] PROJECTION = new String[] {
                "read"
        };

        // The indexes of the default columns which must be consistent
        // with above PROJECTION.
        static final int UNREAD = 0;

        private TextView mTextUnreadSMS;
        private TextView mTextTotalSMS;
        private TextView mTextUnreadMMS;
        private TextView mTextTotalMMS;
        private Button mButtonRefresh;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_message_counter, container, false);

            initialize(rootView);
            updateMessageCount();

            return rootView;
        }

        @Override
        public void onClick(View view) {
            if (view == mButtonRefresh) {
                updateMessageCount();
            }
        }

        private void initialize(View view) {
            mTextUnreadSMS = (TextView) view.findViewById(R.id.textUnreadSMS);
            mTextTotalSMS = (TextView) view.findViewById(R.id.textTotalSMS);
            mTextUnreadMMS = (TextView) view.findViewById(R.id.textUnreadMMS);
            mTextTotalMMS = (TextView) view.findViewById(R.id.textTotalMMS);
            mButtonRefresh = (Button) view.findViewById(R.id.buttonRefresh);
            mButtonRefresh.setOnClickListener(this);
        }

        private void updateMessageCount() {
            Activity activity = this.getActivity();
            if (activity == null) {
                Log.d(TAG, "activity == null");
                return;
            } else {
                Log.d(TAG, "Do updateMessageCount()");
            }

            ContentResolver cr = activity.getContentResolver();
            Cursor cursor;

            String selection = "read = " + UNREAD;

            if (mTextUnreadSMS != null) {
                cursor = cr.query(SMS_CONTENT_URI, PROJECTION, selection, null, null);
                if (cursor != null) {
                    mTextUnreadSMS.setText(getString(R.string.unread_sms) + cursor.getCount());
                    cursor.close();
                }
            } else {
                Log.d(TAG, "Unread SMS TextView is null");
            }

            if (mTextTotalSMS != null) {
                cursor = cr.query(SMS_CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    mTextTotalSMS.setText(getString(R.string.total_sms) + cursor.getCount());
                    cursor.close();
                }
            } else {
                Log.d(TAG, "Total SMS TextView is null");
            }

            if (mTextUnreadMMS != null) {
                cursor = cr.query(MMS_CONTENT_URI, PROJECTION, selection, null, null);
                if (cursor != null) {
                    mTextUnreadMMS.setText(getString(R.string.unread_mms) + cursor.getCount());
                    cursor.close();
                }
            } else {
                Log.d(TAG, "Unread MMS TextView is null");
            }

            if (mTextTotalMMS != null) {
                cursor = cr.query(MMS_CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    mTextTotalMMS.setText(getString(R.string.total_mms) + cursor.getCount());
                    cursor.close();
                }
            } else {
                Log.d(TAG, "Total MMS TextView is null");
            }
        }

    }

}
