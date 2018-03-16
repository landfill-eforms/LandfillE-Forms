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

                    String fakeJsonForEmulator = "{\n" +
                            "  \"instruments\" : [ {\n" +
                            "    \"id\" : 11,\n" +
                            "    \"serialNumber\" : \"121126615\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"An adapted version of the TVA1000\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"SEM500\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 8\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 12,\n" +
                            "    \"serialNumber\" : \"GM05311\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : true,\n" +
                            "      \"description\" : \"For gas wells and probes\",\n" +
                            "      \"instantaneous\" : false,\n" +
                            "      \"carbonDioxidePercent\" : true,\n" +
                            "      \"pressure\" : true,\n" +
                            "      \"type\" : \"GEM2000\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : true,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : true,\n" +
                            "      \"nitrogenPercent\" : true,\n" +
                            "      \"id\" : 6\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 13,\n" +
                            "    \"serialNumber\" : \"GM05322\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : true,\n" +
                            "      \"description\" : \"For gas wells and probes\",\n" +
                            "      \"instantaneous\" : false,\n" +
                            "      \"carbonDioxidePercent\" : true,\n" +
                            "      \"pressure\" : true,\n" +
                            "      \"type\" : \"GEM2000\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : true,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : true,\n" +
                            "      \"nitrogenPercent\" : true,\n" +
                            "      \"id\" : 6\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 14,\n" +
                            "    \"serialNumber\" : \"GM05326\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : true,\n" +
                            "      \"description\" : \"For gas wells and probes\",\n" +
                            "      \"instantaneous\" : false,\n" +
                            "      \"carbonDioxidePercent\" : true,\n" +
                            "      \"pressure\" : true,\n" +
                            "      \"type\" : \"GEM2000\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : true,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : true,\n" +
                            "      \"nitrogenPercent\" : true,\n" +
                            "      \"id\" : 6\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 15,\n" +
                            "    \"serialNumber\" : \"GM05328\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : true,\n" +
                            "      \"description\" : \"For gas wells and probes\",\n" +
                            "      \"instantaneous\" : false,\n" +
                            "      \"carbonDioxidePercent\" : true,\n" +
                            "      \"pressure\" : true,\n" +
                            "      \"type\" : \"GEM2000\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : true,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : true,\n" +
                            "      \"nitrogenPercent\" : true,\n" +
                            "      \"id\" : 6\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 16,\n" +
                            "    \"serialNumber\" : \"GM05881\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : true,\n" +
                            "      \"description\" : \"For gas wells and probes\",\n" +
                            "      \"instantaneous\" : false,\n" +
                            "      \"carbonDioxidePercent\" : true,\n" +
                            "      \"pressure\" : true,\n" +
                            "      \"type\" : \"GEM2000\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : true,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : true,\n" +
                            "      \"nitrogenPercent\" : true,\n" +
                            "      \"id\" : 6\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 17,\n" +
                            "    \"serialNumber\" : \"GM12524\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : true,\n" +
                            "      \"description\" : \"For gas wells and probes\",\n" +
                            "      \"instantaneous\" : false,\n" +
                            "      \"carbonDioxidePercent\" : true,\n" +
                            "      \"pressure\" : true,\n" +
                            "      \"type\" : \"GEM2000\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : true,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : true,\n" +
                            "      \"nitrogenPercent\" : true,\n" +
                            "      \"id\" : 6\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 18,\n" +
                            "    \"serialNumber\" : \"G501938\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : true,\n" +
                            "      \"description\" : \"with color screen\",\n" +
                            "      \"instantaneous\" : false,\n" +
                            "      \"carbonDioxidePercent\" : true,\n" +
                            "      \"pressure\" : true,\n" +
                            "      \"type\" : \"GEM5000\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : true,\n" +
                            "      \"hydrogenSulfidePpm\" : true,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : true,\n" +
                            "      \"nitrogenPercent\" : true,\n" +
                            "      \"id\" : 7\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 19,\n" +
                            "    \"serialNumber\" : \"342\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : true,\n" +
                            "      \"description\" : \"Obsolete model, no longer serviced.\",\n" +
                            "      \"instantaneous\" : false,\n" +
                            "      \"carbonDioxidePercent\" : true,\n" +
                            "      \"pressure\" : true,\n" +
                            "      \"type\" : \"GEM-500\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : true,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : true,\n" +
                            "      \"nitrogenPercent\" : true,\n" +
                            "      \"id\" : 9\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"OBSOLETE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 20,\n" +
                            "    \"serialNumber\" : \"0914235943\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"FID unit\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"TVA-1000\",\n" +
                            "      \"manufacturer\" : \"Thremo\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 5\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 6,\n" +
                            "    \"serialNumber\" : \"17192496\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"FID unit\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"TVA-1000\",\n" +
                            "      \"manufacturer\" : \"Thremo\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 5\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 7,\n" +
                            "    \"serialNumber\" : \"33435442\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"FID unit\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"TVA-1000\",\n" +
                            "      \"manufacturer\" : \"Thremo\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 5\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 8,\n" +
                            "    \"serialNumber\" : \"425908759\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"FID unit\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"TVA-1000\",\n" +
                            "      \"manufacturer\" : \"Thremo\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 5\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 9,\n" +
                            "    \"serialNumber\" : \"12111812\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"An adapted version of the TVA1000\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"SEM500\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 8\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 10,\n" +
                            "    \"serialNumber\" : \"67176223\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"An adapted version of the TVA1000\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"SEM500\",\n" +
                            "      \"manufacturer\" : \"Landtec\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 8\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : null\n" +
                            "  }, {\n" +
                            "    \"id\" : 1,\n" +
                            "    \"serialNumber\" : \"18751\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"Groundwater controller\",\n" +
                            "      \"instantaneous\" : false,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"400UH\",\n" +
                            "      \"manufacturer\" : \"QED\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 1\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"INACTIVE\",\n" +
                            "    \"description\" : \"QED controller\"\n" +
                            "  }, {\n" +
                            "    \"id\" : 2,\n" +
                            "    \"serialNumber\" : \"1123456\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"059-A110-000\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"VOC\",\n" +
                            "      \"manufacturer\" : \"RAE Systems\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 2\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"OBSOLETE\",\n" +
                            "    \"description\" : \" VOC monitor \"\n" +
                            "  }, {\n" +
                            "    \"id\" : 3,\n" +
                            "    \"serialNumber\" : \"12345\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"059-A110-000\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"VOC\",\n" +
                            "      \"manufacturer\" : \"RAE Systems\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 2\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"OBSOLETE\",\n" +
                            "    \"description\" : \"voc12345\"\n" +
                            "  }, {\n" +
                            "    \"id\" : 4,\n" +
                            "    \"serialNumber\" : \"22561\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"VC-12001\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"VOC1\",\n" +
                            "      \"manufacturer\" : \"EMR\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : false,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 4\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : \"CH4 level monitoring\"\n" +
                            "  }, {\n" +
                            "    \"id\" : 5,\n" +
                            "    \"serialNumber\" : \"7157035\",\n" +
                            "    \"instrumentType\" : {\n" +
                            "      \"methanePercent\" : false,\n" +
                            "      \"description\" : \"FID unit\",\n" +
                            "      \"instantaneous\" : true,\n" +
                            "      \"carbonDioxidePercent\" : false,\n" +
                            "      \"pressure\" : false,\n" +
                            "      \"type\" : \"TVA-1000\",\n" +
                            "      \"manufacturer\" : \"Thremo\",\n" +
                            "      \"probe\" : false,\n" +
                            "      \"hydrogenSulfidePpm\" : false,\n" +
                            "      \"methanePpm\" : true,\n" +
                            "      \"oxygenPercent\" : false,\n" +
                            "      \"nitrogenPercent\" : false,\n" +
                            "      \"id\" : 5\n" +
                            "    },\n" +
                            "    \"instrumentStatus\" : \"ACTIVE\",\n" +
                            "    \"description\" : \"\"\n" +
                            "  } ],\n" +
                            "  \"users\" : [ {\n" +
                            "    \"id\" : 5,\n" +
                            "    \"username\" : \"gnigussie\",\n" +
                            "    \"password\" : \"$2a$04$0Qg9xRzvqECUTm6bOMCywOrS263UJvSQPD/5WIJyVIqI./mc8cM1C\",\n" +
                            "    \"firstname\" : \"Getahun\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Nigussie\",\n" +
                            "    \"emailAddress\" : \"Getahun.Nigussie@lacity.org\",\n" +
                            "    \"employeeId\" : \"GN\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 6,\n" +
                            "    \"username\" : \"abahariance\",\n" +
                            "    \"password\" : \"$2a$04$UXMK3r6tlWhC4pG3DBBRA.3ZV3fbgTYzssuyuEcTGG3gGQOSBwmEG\",\n" +
                            "    \"firstname\" : \"Arpa\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Bahariance\",\n" +
                            "    \"emailAddress\" : \"Arpa.Bahariance@lacity.org\",\n" +
                            "    \"employeeId\" : \"AB\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 7,\n" +
                            "    \"username\" : \"cmenzies\",\n" +
                            "    \"password\" : \"$2a$04$EhhRn9gEZkBJOzQvIJ0jV.Vt6yc0YcK6skC9029QRxhyspez1Z3JS\",\n" +
                            "    \"firstname\" : \"Charles\",\n" +
                            "    \"middlename\" : \"O\",\n" +
                            "    \"lastname\" : \"Menzies\",\n" +
                            "    \"emailAddress\" : \"Charles.Menzies@lacity.org\",\n" +
                            "    \"employeeId\" : \"COM\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 8,\n" +
                            "    \"username\" : \"amiddleton\",\n" +
                            "    \"password\" : \"$2a$04$BXo0D.bc1dvT5aqeE.C6vegVDc0.R2LCH/u/xiOqvO0rLqZj8NKb2\",\n" +
                            "    \"firstname\" : \"Alicia\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Middleton\",\n" +
                            "    \"emailAddress\" : \"Alicia.Middleton@lacity.org\",\n" +
                            "    \"employeeId\" : \"AM\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 9,\n" +
                            "    \"username\" : \"jbrosius\",\n" +
                            "    \"password\" : \"$2a$04$hj/BCNDFOyx.ihoTOrcqEu/.c00wl8MJV21fR5beJgLyHJY4w6Tdq\",\n" +
                            "    \"firstname\" : \"John\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Brosius\",\n" +
                            "    \"emailAddress\" : \"John.Brosius@lacity.org\",\n" +
                            "    \"employeeId\" : \"\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 1,\n" +
                            "    \"username\" : \"aquach\",\n" +
                            "    \"password\" : \"$2a$04$9yUSI5Uowlv8nlaZJ3HaV.ZrwokNbuLYGc5A9KxyGXV8StlFdCefO\",\n" +
                            "    \"firstname\" : \"Alvin\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Quach\",\n" +
                            "    \"emailAddress\" : \"alvinquach91@gmail.com\",\n" +
                            "    \"employeeId\" : \"\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 2,\n" +
                            "    \"username\" : \"santest\",\n" +
                            "    \"password\" : \"$2a$04$n9Ee7dzVsiwBIwJbBe9H5ugAkfCuHRyyeVepS4d6NpjC2WRy9RSmu\",\n" +
                            "    \"firstname\" : \"San\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Test\",\n" +
                            "    \"emailAddress\" : \"santest@santest.com\",\n" +
                            "    \"employeeId\" : \"999999\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 3,\n" +
                            "    \"username\" : \"wandrews\",\n" +
                            "    \"password\" : \"$2a$04$XGRQZWzpr4PuC7Np86lDdOv1JTQagFYsPw2lCb/nYqDYq2oAEwnte\",\n" +
                            "    \"firstname\" : \"William\",\n" +
                            "    \"middlename\" : \"C\",\n" +
                            "    \"lastname\" : \"Andrews\",\n" +
                            "    \"emailAddress\" : \"William.Andrews@lacity.org\",\n" +
                            "    \"employeeId\" : \"39205\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 4,\n" +
                            "    \"username\" : \"jsalas\",\n" +
                            "    \"password\" : \"$2a$04$oYFTKHu0NrAGMwjuNpq7sePwrk.UMnbscrZms/Nyqi2jd1G4SDmoS\",\n" +
                            "    \"firstname\" : \"John\",\n" +
                            "    \"middlename\" : \"C\",\n" +
                            "    \"lastname\" : \"Salas\",\n" +
                            "    \"emailAddress\" : \"John.Salas@lacity.org\",\n" +
                            "    \"employeeId\" : \"JCS\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 16,\n" +
                            "    \"username\" : \"jgonz219\",\n" +
                            "    \"password\" : \"$2a$04$txHDJEiG22eD6RViOV24p.GSCkflwQ8931WIWOt2TLPWgYRS83.nm\",\n" +
                            "    \"firstname\" : \"Jose\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Gonzalez\",\n" +
                            "    \"emailAddress\" : \"jgonz219@gmail.com\",\n" +
                            "    \"employeeId\" : \"\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 17,\n" +
                            "    \"username\" : \"jmancia\",\n" +
                            "    \"password\" : \"$2a$04$ziDEr8MvAqujmcNU5s7JieyEL428MN1kuP/zq0vaAZWYLq/5zKq3.\",\n" +
                            "    \"firstname\" : \"Jonathan\",\n" +
                            "    \"middlename\" : \"M\",\n" +
                            "    \"lastname\" : \"Mancia\",\n" +
                            "    \"emailAddress\" : \"jonathan.mancia@lacity.org\",\n" +
                            "    \"employeeId\" : \"jmm\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 18,\n" +
                            "    \"username\" : \"talvarado\",\n" +
                            "    \"password\" : \"$2a$04$XQs1TbxFQ5ihpXlx893ohe9pU.AY9RIcmdR6f6oPdgOjFjKUfAsdS\",\n" +
                            "    \"firstname\" : \"Teky\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Alvarado\",\n" +
                            "    \"emailAddress\" : \"alvarado.teky@outlook.com\",\n" +
                            "    \"employeeId\" : \"\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 14,\n" +
                            "    \"username\" : \"smahbub\",\n" +
                            "    \"password\" : \"$2a$04$TpRLauv85iAzgWNyX1U0RuUjpRh.H4pfu3rcAvJxsNeV8EZdTQ2be\",\n" +
                            "    \"firstname\" : \"Safa\",\n" +
                            "    \"middlename\" : \"Al\",\n" +
                            "    \"lastname\" : \"Mahbub\",\n" +
                            "    \"emailAddress\" : \"safamahbub357@gmail.com\",\n" +
                            "    \"employeeId\" : \"\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 15,\n" +
                            "    \"username\" : \"asdf\",\n" +
                            "    \"password\" : \"$2a$04$9yUSI5Uowlv8nlaZJ3HaV.ZrwokNbuLYGc5A9KxyGXV8StlFdCefO\",\n" +
                            "    \"firstname\" : \"Ayy Ess\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Dee Eff\",\n" +
                            "    \"emailAddress\" : \"asdf@asdf.asdf\",\n" +
                            "    \"employeeId\" : \"ASDF\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 10,\n" +
                            "    \"username\" : \"bmisra\",\n" +
                            "    \"password\" : \"$2a$04$bWdsdv/D.ukB3DKQftesuOU4P0AQ00PhFB.SkmpOyyHvQ91cyIQA6\",\n" +
                            "    \"firstname\" : \"Brijesh\",\n" +
                            "    \"middlename\" : \"C\",\n" +
                            "    \"lastname\" : \"Misra\",\n" +
                            "    \"emailAddress\" : \"Brijesh.Misra@lacity.org\",\n" +
                            "    \"employeeId\" : \"BCM\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 11,\n" +
                            "    \"username\" : \"jhamilton\",\n" +
                            "    \"password\" : \"$2a$04$tjUDkR0Vzz.IHapbsthPoehHRdws.nOwTanEci0fsE6WNii0BiWsa\",\n" +
                            "    \"firstname\" : \"John\",\n" +
                            "    \"middlename\" : \"Cobb\",\n" +
                            "    \"lastname\" : \"Hamilton\",\n" +
                            "    \"emailAddress\" : \"John.Cobb.Hamilton@lacity.org\",\n" +
                            "    \"employeeId\" : \"JCH\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 12,\n" +
                            "    \"username\" : \"jkarroum\",\n" +
                            "    \"password\" : \"$2a$04$0LPfu.LEs9q4IMDMAg5ipux5iZB/woX.d/zRgcvUNMJC9rUjKhA8.\",\n" +
                            "    \"firstname\" : \"John\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Karroum\",\n" +
                            "    \"emailAddress\" : \"John.Karroum@lacity.org\",\n" +
                            "    \"employeeId\" : \"JK\",\n" +
                            "    \"enabled\" : true\n" +
                            "  }, {\n" +
                            "    \"id\" : 13,\n" +
                            "    \"username\" : \"aadame\",\n" +
                            "    \"password\" : \"$2a$04$J8hAIQKuZxS.1uS/NxpLz..bYbP2ksmJNQmMZX8DwtiL34b304f9y\",\n" +
                            "    \"firstname\" : \"Andres\",\n" +
                            "    \"middlename\" : \"\",\n" +
                            "    \"lastname\" : \"Adame\",\n" +
                            "    \"emailAddress\" : \"aadame95@gmail.com\",\n" +
                            "    \"employeeId\" : \"AA\",\n" +
                            "    \"enabled\" : true\n" +
                            "  } ]\n" +
                            "}";

                    ImportedData importedData = gson.fromJson(fakeJsonForEmulator, ImportedData.class);

//                    ImportedData importedData = gson.fromJson(new FileReader(Environment.getExternalStorageDirectory() + File.separator + path + File.separator + fileName),ImportedData.class);
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
