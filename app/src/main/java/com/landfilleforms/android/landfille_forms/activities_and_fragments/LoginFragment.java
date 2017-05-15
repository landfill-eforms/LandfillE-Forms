package com.landfilleforms.android.landfille_forms.activities_and_fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema;
import com.landfilleforms.android.landfille_forms.database.dao.InstrumentDao;
import com.landfilleforms.android.landfille_forms.database.dao.InstrumentTypeDao;
import com.landfilleforms.android.landfille_forms.database.dao.UserDao;
import com.landfilleforms.android.landfille_forms.database.util.TestUtil;
import com.landfilleforms.android.landfille_forms.model.ImportedData;
import com.landfilleforms.android.landfille_forms.model.Instrument;
import com.landfilleforms.android.landfille_forms.model.InstrumentType;
import com.landfilleforms.android.landfille_forms.model.User;
import com.landfilleforms.android.landfille_forms.util.BCrypt;
import com.landfilleforms.android.landfille_forms.util.SessionManager;

import java.io.File;
import java.io.FileReader;
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
    private Button mImportButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = new User();
        Log.d(TAG, "onCreate(Bundle) called");
        String path = "LandfillData" + File.separator +"Import";
        File myFile = new File(Environment.getExternalStorageDirectory()+File.separator+path);
        myFile.mkdirs();

        session = new SessionManager(getActivity().getApplicationContext());
        if(session.isLoggedIn()) {
            Intent i = new Intent(getActivity(),MenuActivity.class);
            startActivity(i);
        }
        mDatabase = new LandFillBaseHelper(getActivity()).getWritableDatabase();
        mExistingUsers = UserDao.get(getActivity()).getUsers();
        if(mExistingUsers.size() == 0) {
            TestUtil.insertDummyUserData(mDatabase);
            mExistingUsers = UserDao.get(getActivity()).getUsers();
        }

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

        mLoginButton = (Button)v.findViewById(R.id.login_submit);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mUser.getUsername().trim().length() > 0 && mUser.getPassword().length() > 0) {
                    boolean loginInfoValid = false;
                    for(int i = 0; i < mExistingUsers.size(); i++) {
                        Log.d("Username",mExistingUsers.get(i).getUsername());
                        if (mUser.getUsername().equals(mExistingUsers.get(i).getUsername()) && BCrypt.checkpw(mUser.getPassword(), mExistingUsers.get(i).getPassword())) {
                            if(!mExistingUsers.get(i).isEnabled()){
                                break;
                            }
                            session.createLoginSession(mExistingUsers.get(i));
                            loginInfoValid = true;
                            break;
                        }
                    }
                    if(loginInfoValid) {
                        Intent intent = new Intent(getActivity(),MenuActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getActivity(), "Invalid Username/Password Combination.",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Username/password fields can't be blank.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //TODO: DR this function from importfragment
        mImportButton = (Button) v.findViewById(R.id.import_button);
        mImportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Context context = getActivity();
                String fileName = "LandFillDataImport.json";
                String path = "LandfillData" + File.separator +"Import";
                try{
                    File myFile = new File(Environment.getExternalStorageDirectory()+File.separator+path);
                    myFile.mkdirs();

                    GsonBuilder builder = new GsonBuilder();
                    builder.serializeNulls();
                    builder.setDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                    Gson gson = builder.create();

                    ImportedData importedData = gson.fromJson(new FileReader(Environment.getExternalStorageDirectory() + File.separator + path + File.separator + fileName),ImportedData.class);
                    //Log.d("ImportFrag:",importedData.getUsers().get(0).getUsername());

                    mDatabase = new LandFillBaseHelper(getActivity()).getWritableDatabase();
                    mDatabase.execSQL("delete from "+ LandFillDbSchema.UsersTable.NAME);
                    mDatabase.execSQL("delete from "+ LandFillDbSchema.InstrumentsTable.NAME);
                    mDatabase.execSQL("delete from "+ LandFillDbSchema.InstrumentTypesTable.NAME);
                    TestUtil.insertDummyUserData(mDatabase);
                    UserDao.get(getActivity()).addUsers(importedData.getUsers());
                    List<InstrumentType> instrumentTypes = new ArrayList<>();
                    List<Integer> existingInstrumentTypeIds = new ArrayList<>();
                    for(Instrument i:importedData.getInstruments()){
                        if(!existingInstrumentTypeIds.contains(i.getInstrumentType().getId())){
                            instrumentTypes.add(i.getInstrumentType());
                            existingInstrumentTypeIds.add(i.getInstrumentType().getId());
                            Log.d("InstrumentTypeId",Integer.toString(i.getInstrumentType().getId()));
                        }
                    }

                    InstrumentTypeDao.get(getActivity()).addInstrumentTypes(instrumentTypes);
                    InstrumentDao.get(getActivity()).addInstruments(importedData.getInstruments());

                    Instrument testInstrument = InstrumentDao.get(getActivity()).getInstrument("1");
                    Log.d("ImportFrag", testInstrument.getInstrumentType().getManufacturer());
                    Toast.makeText(context,R.string.import_successful_toast, Toast.LENGTH_SHORT).show();
                    mExistingUsers = importedData.getUsers();

                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,R.string.import_unsuccessful_toast, Toast.LENGTH_SHORT).show();
                }


            }
        });




        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(session.isLoggedIn()) {
            Log.d(TAG, "onStart() called, isLoggedIn is true");
            Intent i = new Intent(getActivity(),LoginActivity.class);
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
            Intent i = new Intent(getActivity(),MenuActivity.class);
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
