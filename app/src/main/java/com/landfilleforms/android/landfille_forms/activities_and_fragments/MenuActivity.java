package com.landfilleforms.android.landfille_forms.activities_and_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.landfilleforms.android.landfille_forms.R;
import com.landfilleforms.android.landfille_forms.model.User;
import com.landfilleforms.android.landfille_forms.util.SessionManager;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FragmentActivity myContext;
    SessionManager session;
    User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Home");


        //session used
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        mUser = session.getCurrentUser();
        Log.d("UserName:", mUser.getUsername());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.context_frame,new WelcomeFragment()).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

   /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();
        switch(id) {
            //case home, navigate back to welcome fragment
            case R.id.nav_home:
                fm.beginTransaction().replace(R.id.context_frame,new WelcomeFragment()).commit();
                break;
            //case locations, navigate to locations activity
            case R.id.nav_locations:
                Intent i = new Intent(this,LocationActivity.class);
                startActivity(i);
                break;
            //case import, navigate to export fragment
            case R.id.nav_import:
                fm.beginTransaction().replace(R.id.context_frame,new ImportFragment()).commit();
                break;
            //case export , navigate to export fragment
            case R.id.nav_export:
                fm.beginTransaction().replace(R.id.context_frame,new ExportFragment()).commit();
                break;
            //coming soon. Unsure if we are going to take this out or not.
            case R.id.nav_sync:
                Toast.makeText(this, R.string.coming_soon_toast, Toast.LENGTH_SHORT).show();
                break;
            //Unsure if we are going to take this out or not.
            case R.id.nav_settings:
                Toast.makeText(this, R.string.to_be_determined_toast, Toast.LENGTH_SHORT).show();
                break;
            //logs you out of session and navigates back to login activity.
            case R.id.nav_logout:
                Toast.makeText(this, R.string.logout_toast, Toast.LENGTH_SHORT).show();
                session.logoutUser();
                break;

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
