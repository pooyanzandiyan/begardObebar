package com.f22labs.instalikefragmenttransaction.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.f22labs.instalikefragmenttransaction.G;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.fragments.BaseFragment;
import com.f22labs.instalikefragmenttransaction.fragments.HomeFragment;
import com.f22labs.instalikefragmenttransaction.fragments.RankFragment;
import com.f22labs.instalikefragmenttransaction.fragments.ScanFragment;
import com.f22labs.instalikefragmenttransaction.fragments.ProfileFragment;
import com.f22labs.instalikefragmenttransaction.utils.FragmentHistory;
import com.f22labs.instalikefragmenttransaction.utils.NetworkUtils;
import com.f22labs.instalikefragmenttransaction.utils.PermissionUtil;
import com.f22labs.instalikefragmenttransaction.utils.PolyUtil;
import com.f22labs.instalikefragmenttransaction.utils.Utils;
import com.f22labs.instalikefragmenttransaction.views.FragNavController;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.f22labs.instalikefragmenttransaction.G.InMarkar;

public class MainActivity extends BaseActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener {
	// location last updated time
	private String mLastUpdateTime;
	
	// location updates interval - 10sec
	private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	
	// fastest updates interval - 5 sec
	// location updates will be received if another app is requesting the locations
	// than your app can handle
	private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
	
	private static final int REQUEST_CHECK_SETTINGS = 100;
	
	
	// bunch of location related apis
	private FusedLocationProviderClient mFusedLocationClient;
	private SettingsClient mSettingsClient;
	private LocationRequest mLocationRequest;
	private LocationSettingsRequest mLocationSettingsRequest;
	private LocationCallback mLocationCallback;
	private Location mCurrentLocation;
	
	@BindView(R.id.content_frame)
	FrameLayout contentFrame;
	
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	
	private int[] mTabIconsSelected = {
			R.drawable.tab_home,
			R.drawable.scan,
			R.drawable.rank,
			R.drawable.tab_profile};
	
	
	@BindArray(R.array.tab_name)
	String[] TABS;
	
	@BindView(R.id.bottom_tab_layout)
	TabLayout bottomTabLayout;
	
	private FragNavController mNavController;
	
	private FragmentHistory fragmentHistory;
	
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
		ButterKnife.bind(this);
		
		initToolbar();
		if (!PermissionUtil.checkPermission(MainActivity.this,
				Manifest.permission.CAMERA,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_FINE_LOCATION)) {
			new AlertDialog.Builder(this)
					.setMessage("بازی بگرد و ببر نیاز به دسترسی هایی مثل جی پی اس و دسترسی به دوربین دارد لطفا در صفحه ای که بعدا بارگذاری می شود روی allow یا اجازه دادن لمس کنید")
					.setPositiveButton("حله! بزن بریم", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							PermissionUtil.requestPermission(MainActivity.this, 0x103, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION);
						}}).show();
			
		} else {
			init();
			startLocationUpdates();
		}
		

		
		
		initTab();
		
		fragmentHistory = new FragmentHistory();
		
		
		mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_frame)
				.transactionListener(this)
				.rootFragmentListener(this, TABS.length)
				.build();
		
		
		switchTab(0);
		
		bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				
				fragmentHistory.push(tab.getPosition());
				switchTab(tab.getPosition());
				
				
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			
			}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				
				mNavController.clearStack();
				
				switchTab(tab.getPosition());
				
				
			}
		});
		final Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() {
				if (!NetworkUtils.isConnected(MainActivity.this)) {
					if (G.isFirst) {
						startActivity(new Intent(MainActivity.this, noInternet.class));
						G.isFirst = false;
					}
				}
				handler.postDelayed(this, 2000);
				
			}
		};
		handler.postDelayed(r, 1000);
	}
	
	private void startLocationUpdates() {
		
		mSettingsClient
				.checkLocationSettings(mLocationSettingsRequest)
				.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
					@SuppressLint("MissingPermission")
					@Override
					public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
						Log.i("gpsss", "All location settings are satisfied.");
						
						showToastMessage("درحال یافتن موقعیت مکانی شما...", Toast.LENGTH_SHORT);
						//noinspection MissingPermission
						mFusedLocationClient.requestLocationUpdates(mLocationRequest,
								mLocationCallback, Looper.myLooper());
						
						updateLocationUI();
					}
				})
				.addOnFailureListener(this, new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						int statusCode = ((ApiException) e).getStatusCode();
						switch (statusCode) {
							case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
								Log.i("gpsss", "Location settings are not satisfied. Attempting to upgrade " +
										"location settings ");
								try {
									// Show the dialog by calling startResolutionForResult(), and check the
									// result in onActivityResult().
									ResolvableApiException rae = (ResolvableApiException) e;
									rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
								} catch (IntentSender.SendIntentException sie) {
									Log.i("gpsss", "PendingIntent unable to execute request.");
								}
								break;
							case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
								String errorMessage = "Location settings are inadequate, and cannot be " +
										"fixed here. Fix in Settings.";
								Log.e("gpsss", errorMessage);
								
								Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
						}
						
						updateLocationUI();
					}
				});
	}
	
	private void init() {
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		mSettingsClient = LocationServices.getSettingsClient(this);
		
		mLocationCallback = new LocationCallback() {
			@Override
			public void onLocationResult(LocationResult locationResult) {
				super.onLocationResult(locationResult);
				// location is received
				mCurrentLocation = locationResult.getLastLocation();
				mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
				updateLocationUI();
			}
		};
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
		builder.addLocationRequest(mLocationRequest);
		mLocationSettingsRequest = builder.build();
	}
	
	private void updateLocationUI() {
		if (mCurrentLocation != null) {
			
			Log.v("gpss",
					"Lat: " + mCurrentLocation.getLatitude() + ", " +
							"Lng: " + mCurrentLocation.getLongitude()
			);
			ArrayList<LatLng> polygon = new ArrayList<LatLng>();
			polygon.add(new LatLng(35.736041, 51.533793));
			polygon.add(new LatLng(35.735878, 51.535038));
			polygon.add(new LatLng(35.734664, 51.533577));
			polygon.add(new LatLng(35.734511, 51.534782));
			LatLng myLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
			InMarkar = PolyUtil.containsLocation(myLocation, polygon, false);
			Log.v("gpss",
					"ok: " + InMarkar);
			Log.v("gpss", "Last updated on: " + mLastUpdateTime);
		}
		
	}
	
	private void initToolbar() {
		setSupportActionBar(toolbar);
		
		
	}
	
	private void initTab() {
		if (bottomTabLayout != null) {
			for (int i = 0; i < TABS.length; i++) {
				bottomTabLayout.addTab(bottomTabLayout.newTab());
				TabLayout.Tab tab = bottomTabLayout.getTabAt(i);
				if (tab != null)
					tab.setCustomView(getTabView(i));
			}
		}
	}
	
	
	private View getTabView(int position) {
		View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item_bottom, null);
		ImageView icon = view.findViewById(R.id.tab_icon);
		icon.setImageDrawable(Utils.setDrawableSelector(MainActivity.this, mTabIconsSelected[position], mTabIconsSelected[position]));
		return view;
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case 0x100: {
				if (PermissionUtil.isPermissionGranted(permissions, grantResults,
						Manifest.permission.CAMERA,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.ACCESS_FINE_LOCATION)) {
					
					init();
					startLocationUpdates();
				}else{
					longToastMessage("متاسفانه بدون اجازه دسترسی دادن، امکان بازی وجود ندارد");
				}
			}
		}
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onStop() {
		
		super.onStop();
	}
	
	
	private void switchTab(int position) {
		mNavController.switchTab(position);
//        updateToolbarTitle(position);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			
			
			case android.R.id.home:
				
				
				onBackPressed();
				return true;
		}
		
		
		return super.onOptionsItemSelected(item);
		
	}
	
	@Override
	public void onBackPressed() {
		
		if (!mNavController.isRootFragment()) {
			mNavController.popFragment();
		} else {
			
			if (fragmentHistory.isEmpty()) {
				super.onBackPressed();
			} else {
				
				
				if (fragmentHistory.getStackSize() > 1) {
					
					int position = fragmentHistory.popPrevious();
					
					switchTab(position);
					
					updateTabSelection(position);
					
				} else {
					
					switchTab(0);
					
					updateTabSelection(0);
					
					fragmentHistory.emptyStack();
				}
			}
			
		}
	}
	
	
	private void updateTabSelection(int currentTab) {
		
		for (int i = 0; i < TABS.length; i++) {
			TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);
			if (currentTab != i) {
				selectedTab.getCustomView().setSelected(false);
			} else {
				selectedTab.getCustomView().setSelected(true);
			}
			
		}
	}
	

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mNavController != null) {
			mNavController.onSaveInstanceState(outState);
		}
	}
	
	@Override
	public void pushFragment(Fragment fragment) {
		if (mNavController != null) {
			mNavController.pushFragment(fragment);
		}
	}
	
	
	@Override
	public void onTabTransaction(Fragment fragment, int index) {
		// If we have a backstack, show the back button
		if (getSupportActionBar() != null && mNavController != null) {
			
			
			updateToolbar();
			
		}
	}
	
	private void updateToolbar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
		getSupportActionBar().setDisplayShowHomeEnabled(!mNavController.isRootFragment());
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
	}
	
	
	@Override
	public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
		//do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
		// If we have a backstack, show the back button
		if (getSupportActionBar() != null && mNavController != null) {
			
			updateToolbar();
			
		}
	}
	
	@Override
	public Fragment getRootFragment(int index) {
		switch (index) {
			
			case FragNavController.TAB1:
				return new HomeFragment();
			case FragNavController.TAB2:
				return new ScanFragment();
			case FragNavController.TAB3:
				return new RankFragment();
			case FragNavController.TAB4:
				return new ProfileFragment();
			
		}
		throw new IllegalStateException("Need to send an index that we know");
	}


//    private void updateToolbarTitle(int position){
//
//
//        getSupportActionBar().setName(TABS[position]);
//
//    }
	
	
	public void updateToolbarTitle(String title) {
		
		
		getSupportActionBar().setTitle(title);
		
	}
}
