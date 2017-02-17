package com.landfilleforms.android.landfille_forms;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.landfilleforms.android.landfille_forms.model.User;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by owchr on 2/15/2017.
 */

public class WelcomeFragment extends Fragment{
    private SessionManager session;
    private User mUser;
    private TextView mWelcomeText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        HashMap<String,String> currentUser = session.getUserDetails();
        mUser = new User();
        mUser.setUsername(currentUser.get(SessionManager.KEY_USERNAME));
        mUser.setFullName(currentUser.get(SessionManager.KEY_USERFULLNAME));
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_screen,container,false);
        mWelcomeText = (TextView) view.findViewById(R.id.welcome_text);
        mWelcomeText.setText("Welcome " +  mUser.getFullName() + "! \n What would you like to do?");
        return view;
    }
}
