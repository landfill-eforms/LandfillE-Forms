package com.landfilleforms.android.landfille_forms;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema;
import com.landfilleforms.android.landfille_forms.database.dao.UserDao;
import com.landfilleforms.android.landfille_forms.database.util.TestUtil;
import com.landfilleforms.android.landfille_forms.model.ImportedData;
import com.landfilleforms.android.landfille_forms.model.User;
import com.landfilleforms.android.landfille_forms.util.BCrypt;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;


/**
 * Created by gkang on 4/25/2017
 */

public class ImportFragment extends Fragment {
    Button mImportButton;
    private SessionManager session;
    private User mUser;
    private SQLiteDatabase mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        mUser = session.getCurrentUser();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_import, container, false);
        getActivity().setTitle("Import");
        mImportButton = (Button) view.findViewById(R.id.menu_import_button);
        mImportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Context context = getActivity();
                String fileName = "LandFillDataImport.json";
                String path = "LandfillData" + File.separator +"Import";
                try{
                    checkJsonFileExistence(fileName,path);

                    GsonBuilder builder = new GsonBuilder();
                    builder.serializeNulls();
                    builder.setDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                    Gson gson = builder.create();

                    ImportedData importedData = gson.fromJson(new FileReader(Environment.getExternalStorageDirectory() + File.separator + path + File.separator + fileName),ImportedData.class);
                    //Log.d("ImportFrag:",importedData.getUsers().get(0).getUsername());

                    mDatabase = new LandFillBaseHelper(getActivity()).getWritableDatabase();
                    mDatabase.execSQL("delete from "+ LandFillDbSchema.UsersTable.NAME);
                    TestUtil.insertDummyUserData(mDatabase);
                    UserDao.get(getActivity()).addUsers(importedData.getUsers());

                    Toast.makeText(context,R.string.import_successful_toast, Toast.LENGTH_SHORT).show();

                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,R.string.import_unsuccessful_toast, Toast.LENGTH_SHORT).show();
                }


            }
        });
        return view;
    }

    public void checkJsonFileExistence(String fileName, String path){
        File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + path + File.separator + fileName);
        if(myFile.exists()){
            Log.d("ImportFrag:","Import file exists.");
        }
        else {
            Log.d("ImportFrag:","Import file doesn't exist.");
        }
    }
}
