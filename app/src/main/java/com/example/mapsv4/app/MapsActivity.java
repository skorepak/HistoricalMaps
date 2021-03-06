package com.example.mapsv4.app;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapsv4.app.helper.DatabaseHelper;
import com.example.mapsv4.app.model.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private static final String TAG = "Maps::MainActivity";
    public Locations mLocations;
    // Database Helper
    DatabaseHelper db;
    CustomDrawerAdapter adapter;
    List<DrawerItem> dataList;
    private GoogleMap mMap;
    private String[] mMapTitles;
    /**private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
     private ActionBarDrawerToggle mDrawerToggle;**/
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBar mBar;
    private boolean showInfoTiles = false;
    private boolean showHistoricTiles = false;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        setupDrawer();
        //setupOldDrawer();

        mMapTitles = getResources().getStringArray(R.array.Maps);

        mBar = getActionBar();
        mBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lightblue)));
        mBar.setDisplayHomeAsUpEnabled(true);
        mBar.setDisplayShowHomeEnabled(true);
        mBar.setCustomView(R.layout.actionbar_layout);

        SupportMapFragment mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = mf.getMap();

        // enable GPS
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //provide some information for InfoPanel
                setPosition(marker.getPosition());
                return false;
            }
        });

        mTitle = getTitle();
        mDrawerTitle = getString(R.string.title_drawer_maps);

        mLocations = new Locations(this);

        db = new DatabaseHelper(getApplicationContext());

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setupOldDrawer() {
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mMapTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                mBar.setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                mBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void setupDrawer() {
        // Initializing
        dataList = new ArrayList<DrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);


        // Add Drawer Item to dataList
        dataList.add(new DrawerItem("Maps")); // adding a header to the list
        for (String menu : getResources().getStringArray(R.array.Maps)) {
            dataList.add(new DrawerItem(menu, R.drawable.abc_textfield_search_right_default_holo_dark));
        }

        dataList.add(new DrawerItem("Application About"));// adding a header to the list
        for (String menu : getResources().getStringArray(R.array.Application)) {
            dataList.add(new DrawerItem(menu, R.drawable.abc_textfield_search_right_default_holo_dark));
        }

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        findViewById(R.id.infoPane).setVisibility(View.INVISIBLE);
        findViewById(R.id.searchView).setVisibility((!drawerOpen) ? View.VISIBLE : View.INVISIBLE);
        menu.findItem(R.id.action_gps).setVisible(!drawerOpen);
        menu.findItem(R.id.action_pin).setVisible(!drawerOpen);
        menu.findItem(R.id.action_info).setVisible(!drawerOpen);
        menu.findItem(R.id.action_fav).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_gps:
                setUpMap();
                Log.i(TAG, "Moving view to Šumava");
                return true;
            case R.id.action_pin:
                // should be as + into favorities (kind of LIKE/DISLIKE on marker)
                Log.i(TAG, "Click on Pin");
                return true;
            case R.id.action_info:
                setInfoStatus("Informations");
                Log.i(TAG, "Informations");
                return true;
            case R.id.action_fav:
                setInfoStatus("Favorities");
                Log.i(TAG, "Favorities");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Called whenever we handle Info panel */
    private void setInfoStatus(String s) {
        // Do a null check to confirm that we have not already instantiated the map.
        ScrollView infoPane = (ScrollView) findViewById(R.id.infoPane);
        TextView text = (TextView) findViewById(R.id.header);
        if (infoPane.isShown() && text.getText() == s) {
            infoPane.setVisibility(View.INVISIBLE);
        } else {
            text.setText(s);
            infoPane.setVisibility(View.VISIBLE);
        }
    }

    /* Called to handle position of marker */
    private void setPosition(LatLng latLng) {
        TextView text = (TextView) findViewById(R.id.position);
        text.setText("Position:"
                + "\nLatitude is  " + latLng.latitude
                + "\nLongitude is " + latLng.longitude);
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        Log.i(TAG, "Setting Map up!");
        LatLng sumava = new LatLng(49.018412, 13.518837);
        mMap.addMarker(new MarkerOptions().position(sumava).title("Šumava"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sumava)      // Sets the center of the map
                .zoom(9)                    // Sets the zoom
                .bearing(0)                 // Sets the orientation of the camera (90 = east)
                .tilt(15)                   // Sets the tilt of the camera to x degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //provide some information for InfoPanel
        setPosition(sumava);
        mMap.setOnMapLongClickListener(new MyLongClickListener());
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void historicTiles() {
        if (showHistoricTiles) {
            showHistoricTiles = false;
            mMap.clear();
            if (showInfoTiles)
                mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new InfoTileProvider()));
        } else {
            showHistoricTiles = true;
            mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new HistoricTileProvider()));
        }
    }

    private void infoTiles() {
        if (showInfoTiles) {
            showInfoTiles = false;
            mMap.clear();
            if (showHistoricTiles)
                mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new HistoricTileProvider()));
        } else {
            showInfoTiles = true;
            mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new InfoTileProvider()));
        }
    }

    private void showDialog(LatLng latLng){
        InsertLocationDialog f = new InsertLocationDialog();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        f.setLatLng(latLng);
        f.show(ft, "Foo");

        tryDB(latLng);

        Log.i(TAG, "New marker position" + latLng.toString());
        mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker - " + latLng.toString()));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)             // Sets the center of the map
                .zoom(9)                    // Sets the zoom
                .bearing(0)                 // Sets the orientation of the camera (90 = east)
                .tilt(15)                   // Sets the tilt of the camera to x degrees
                .build();
    }

    private void tryDB(LatLng other) {
        // Creating places
        LatLng sumava = new LatLng(49.018412, 13.518837);
        Places place1 = new Places((float) sumava.latitude, (float) sumava.longitude,
                "Šumava", "Sumava", "Sumava");
        Places place2 = new Places((float) other.latitude, (float) other.longitude,
                "NeŠumava", "NeSumava", "NeSumava");

        // Inserting places in db
        long place1_id = db.createPlace(place1);
        long place2_id = db.createPlace(place2);
        long place3_id = db.createPlace(place1);

        Log.d("Places Count", "Places Count: " + db.getAllPlaces().size());

        place2.setPlaces("Mokrava", "Mokrava", "Mokrava");
        place2.setId(1);
        db.updatePlace(place2);

        // Getting all tag names
        Log.d("Places", "Getting All Places");

        List<Places> allPlaces = db.getAllPlaces();
        for (Places place : allPlaces) {
            Log.d("Place", "Name:" + place.getPlaceCZ() + "(DE-"
                    + place.getPlaceDE() + ",EN-" + place.getPlaceEN()
                    + ") Lat: " + place.getLatitude()
                    + " Lng:" + place.getLongitude());
        }

        // Don't forget to close database connection
        db.closeDB();

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switch (position) {
                case (1):
                    mMap.clear();
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case (2):
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case (3):
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case (4):
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case (5):
                    historicTiles();
                    break;
                case (6):
                    infoTiles();
                    break;
                case (8):
                    Toast.makeText(getApplicationContext(), "Version: " + "\nnumber - " + BuildConfig.VERSION_CODE +
                                    "; name - " + BuildConfig.VERSION_NAME,
                            Toast.LENGTH_LONG
                    ).show();
                    break;
                case (9):
                    Toast.makeText(getApplicationContext(), "Authors: \nPetr Tománek \nJakub Skořepa\n Vasily Gusev",
                            Toast.LENGTH_LONG).show();
                    break;
                case (10):
                    Toast.makeText(getApplicationContext(), "Feedback",
                            Toast.LENGTH_LONG).show();
                    break;
            }
            mDrawerLayout.closeDrawers();
        }
    }

    private class MyLongClickListener implements GoogleMap.OnMapLongClickListener {

        @Override
        public void onMapLongClick(LatLng latLng) {
            showDialog(latLng);
        }
    }
}
