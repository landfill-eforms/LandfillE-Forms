package com.landfilleforms.android.landfille_forms.activities_and_fragments;

import android.support.v4.app.Fragment;
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
import com.landfilleforms.android.landfille_forms.util.SessionManager;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


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

        //Not having the import fragment check for login yet since I'm letting users access it w/ logging in
/*        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        mUser = session.getCurrentUser();*/
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_import, container, false);
        getActivity().setTitle(R.string.import_header);
        mImportButton = (Button) view.findViewById(R.id.menu_import_button);
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
