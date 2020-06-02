package com.example.mysts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mysts.adapter.CustomerAdapter;
import com.example.mysts.sql.CustomerModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Customer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth auth;
    Context context;
    CustomerAdapter cust_adapter;
    ListView lvlistview;
    String TAG = "TAG";
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView data = findViewById(R.id.tvDATA);
        auth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab_addCustomer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Customer.this, CustomerRegistrationActivity.class);
                startActivity(i);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        context = this;


        Log.d(TAG, "onCreate:ShowCustomerList_Activity ");

        lvlistview = (ListView) findViewById(R.id.lvlistviewCustomer);
        Cursor cursor = new CustomerModel(this).showCustomerDetails();
        cust_adapter = new CustomerAdapter(context, cursor);
        lvlistview.setAdapter(cust_adapter);
        if (cust_adapter.isEmpty())
            data.setVisibility(View.VISIBLE);
        else {
            data.setVisibility(View.GONE);
        }
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        UpdateNavHeader();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Intent i = new Intent(Customer.this, Customer.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_salesman:
                finish();
                Intent i2 = new Intent(Customer.this, Salesman.class);
                startActivity(i2);
                break;
            case R.id.nav_customer:
                finish();
                Intent i3 = new Intent(Customer.this, Customer.class);
                startActivity(i3);
                break;
            case R.id.nav_order:
                finish();
                Intent i4 = new Intent(Customer.this, Order.class);
                startActivity(i4);
                break;

            case R.id.nav_product:
                finish();
                Intent i5 = new Intent(Customer.this, Product.class);
                startActivity(i5);
                break;

            case R.id.nav_logOut:
                auth.signOut();
                finish();
                Intent i6 = new Intent(Customer.this, UserSelectionActivity.class);
                startActivity(i6);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void UpdateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerview = navigationView.getHeaderView(0);
        TextView navUserName = headerview.findViewById(R.id.nav_username);
        TextView navEmail = headerview.findViewById(R.id.nav_email);
        ImageView navProfileDP = headerview.findViewById(R.id.nav_iv_dp);

        navEmail.setText(currentUser.getEmail());
        navUserName.setText(currentUser.getDisplayName());

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navProfileDP);
    }

}
