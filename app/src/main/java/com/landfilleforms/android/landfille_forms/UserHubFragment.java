package com.landfilleforms.android.landfille_forms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.landfilleforms.android.landfille_forms.model.User;

import java.util.HashMap;

/**
 * Created by Work on 2/3/2017.
 */

public class UserHubFragment extends Fragment {
    private static final String TAG = "UserHubFragment";
    private SessionManager session;
    private User mUser;

    private Button mLocationsButton;
    private Button mNotificationsButton;
    private Button mLogoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        HashMap<String,String> currentUser = session.getUserDetails();
        mUser = new User();
        mUser.setUsername(currentUser.get(SessionManager.KEY_USERNAME));
        mUser.setFullName(currentUser.get(SessionManager.KEY_USERFULLNAME));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_hub, container, false);

        mLocationsButton = (Button)v.findViewById(R.id.location_button);
        mLocationsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),LocationActivity.class);
                startActivity(i);
            }
        });

        mNotificationsButton = (Button)v.findViewById(R.id.notification_button);
        mNotificationsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();
            }
        });

        mLogoutButton = (Button)v.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                session.logoutUser();
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        session.checkLogin();
        Log.d(TAG, "onStart() called");
    }
//test
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        session.checkLogin();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}
