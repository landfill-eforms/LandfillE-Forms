package com.landfilleforms.android.landfille_forms;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema;
import com.landfilleforms.android.landfille_forms.database.TestUtil;
import com.landfilleforms.android.landfille_forms.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Work on 10/30/2016.
 */

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    SessionManager session;
    SQLiteDatabase mDatabase;

    private User mUser;
    private List<User> mExistingUsers;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = new User();
        Log.d(TAG, "onCreate(Bundle) called");

        session = new SessionManager(getActivity().getApplicationContext());
        if(session.isLoggedIn()) {
            Intent i = new Intent(getActivity(),UserHubActivity.class);
            startActivity(i);
        }
        mDatabase = new LandFillBaseHelper(getActivity()).getWritableDatabase();
        TestUtil.insertDummyUserData(mDatabase);
        mExistingUsers = UserLab.get(getActivity()).getUsers();

        mUser.setUsername("");
        mUser.setPassword("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mUsernameField = (EditText)v.findViewById(R.id.user_username);
        mUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordField = (EditText)v.findViewById(R.id.user_password);
        mPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLoginButton = (Button)v.findViewById(R.id.login_submit);//Change!
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Link to other activity.
                if(mUser.getUsername().trim().length() > 0 && mUser.getPassword().trim().length() > 0) {
                    //In the future, make this query the user table and find a username/pw match.
<<<<<<< HEAD
                    for(int i = 0; i < mExistingUsers.size(); i++) {
                        Log.d("UserName", mExistingUsers.get(i).getUsername());
                        if(mUser.getUsername().equals(mExistingUsers.get(i).getUsername()) && mUser.getPassword().equals(mExistingUsers.get(i).getPassword())) {
                            session.createLoginSession(mExistingUsers.get(i).getUsername(), mExistingUsers.get(i).getFullName());

                            Intent intent = new Intent(getActivity(),UserHubActivity.class);
                            startActivity(intent);
                        }
=======
                    if(mUser.getUsername().equals("aquach") && mUser.getPassword().equals("asd")){
                        //Create session and move on to next activity
                        mUser.setFullName("Alvin Quach");//Delete this later
                        session.createLoginSession(mUser.getUsername(), mUser.getFullName());

                        //Intent i = new Intent(getActivity(),UserHubActivity.class);
                        Intent i = new Intent(getActivity(),MenuActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), R.string.incorrect_login_toast, Toast.LENGTH_SHORT).show();
>>>>>>> 4d8387fd21859511a75141235a15aaed4af59665
                    }
                    Toast.makeText(getActivity(), R.string.incorrect_login_toast, Toast.LENGTH_SHORT).show();
//                    if(mUser.getUsername().equals("aquach") && mUser.getPassword().equals("asd")){
//                        //Create session and move on to next activity
//                        mUser.setFullName("Alvin Quach");//Delete this later
//                        session.createLoginSession(mUser.getUsername(), mUser.getFullName());
//
//                        Intent i = new Intent(getActivity(),UserHubActivity.class);
//                        startActivity(i);
//                    } else {
//                        Toast.makeText(getActivity(), R.string.incorrect_login_toast, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getActivity(), R.string.blank_login_toast, Toast.LENGTH_SHORT).show();
                }
//                if(mUser.getUsername() != null) Log.d(TAG, "Username:" + mUser.getUsername());
//                if(mUser.getPassword() != null) Log.d(TAG, "Password:" + mUser.getPassword());
//                Intent i = new Intent(getActivity(),InstantaneousFormActivity.class);
//                i.putExtra(EXTRA_USERNAME, mUser.getUsername());
//                i.putExtra(EXTRA_PASSWORD, mUser.getPassword());
//                startActivity(i);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(session.isLoggedIn()) {
            Log.d(TAG, "onStart() called, isLoggedIn is true");
            Intent i = new Intent(getActivity(),UserHubActivity.class);
            startActivity(i);
        }
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(session.isLoggedIn()) {
            Intent i = new Intent(getActivity(),UserHubActivity.class);
            startActivity(i);
        }
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
    private Cursor getAllUsers() {
        // COMPLETED (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
        return mDatabase.query(
                LandFillDbSchema.UsersTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
