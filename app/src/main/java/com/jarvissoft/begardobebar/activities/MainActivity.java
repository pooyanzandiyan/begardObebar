package com.jarvissoft.begardobebar.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.jarvissoft.begardobebar.BegardObebarApplication;
import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.comunication.app.AppService;
import com.jarvissoft.begardobebar.fragments.BaseFragment;
import com.jarvissoft.begardobebar.fragments.HomeFragment;
import com.jarvissoft.begardobebar.fragments.RankFragment;
import com.jarvissoft.begardobebar.fragments.ScanFragment;
import com.jarvissoft.begardobebar.fragments.ProfileFragment;
import com.jarvissoft.begardobebar.utils.FragmentHistory;
import com.jarvissoft.begardobebar.utils.NetworkUtils;
import com.jarvissoft.begardobebar.utils.PermissionUtil;
import com.jarvissoft.begardobebar.utils.PolyUtil;
import com.jarvissoft.begardobebar.utils.Utils;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;
import com.jarvissoft.begardobebar.views.FragNavController;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jarvissoft.begardobebar.BegardObebarApplication.InMarkar;

public class MainActivity extends BaseActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener {
	// location last updated time
	private String mLastUpdateTime;
	Handler handler;
	Runnable r;
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
	
	@Override
	public void finish() {
		handler.removeCallbacks(r);
		handler.removeCallbacksAndMessages(r);
		super.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(r);
		handler.removeCallbacksAndMessages(r);
	}
	
	private int[] mTabIconsSelected = {
			R.drawable.newspaper,
			R.drawable.scan,
			R.drawable.rank,
			R.drawable.tab_profile};
	
	
	@BindArray(R.array.tab_name)
	String[] TABS;
	
	@BindView(R.id.bottom_tab_layout)
	TabLayout bottomTabLayout;
	
	private FragNavController mNavController;
	boolean doubleBackToExitPressedOnce = false;
	private FragmentHistory fragmentHistory;
	
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
		ButterKnife.bind(this);
		
		//startActivity(new Intent(this,AdsActivity.class));
		initToolbar();
		
		if (!PermissionUtil.checkPermission(MainActivity.this,
				Manifest.permission.CAMERA,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_FINE_LOCATION)) {
			new AlertDialog.Builder(this)
					.setMessage("بازی بگرد و ببر نیاز به دسترسی هایی مثل جی پی اس و دسترسی به دوربین دارد لطفا در صفحه ای که بعدا بارگذاری می شود روی allow یا اجازه دادن لمس کنید")
					.setPositiveButton("حله! بزن بریم", (dialog, which) -> PermissionUtil.requestPermission(MainActivity.this, 0x100, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)).show();
			
		} else {
			if (!isMockSettingsON(MainActivity.this)) {
				init();
				startLocationUpdates();
			} else {
				new AlertDialog.Builder(MainActivity.this).setCancelable(false)
						.setPositiveButton("باشه", (dialog, which) -> {
							finish();
							android.os.Process.killProcess(android.os.Process.myPid());
							System.exit(1);
						})
						.setMessage("شما حالت moke location را برای موقعیت مکانی تقلبی فعال کرده اید و این خلاف قانون بازی می باشد. لطفا ابتدا ان را غیر فعال کنید و دوباره وارد برنامه شوید").create().show();
			}
			
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
		handler = new Handler();
		r = new Runnable() {
			public void run() {
				if (!NetworkUtils.isConnected(MainActivity.this)) {
					if (BegardObebarApplication.isFirst) {
//						startActivity(new Intent(MainActivity.this, NoInternetActivity.class));
//						BegardObebarApplication.isFirst = false;
						shortToastMessage("شما به اینترنت متصل نیستید");
					}
				}
				handler.postDelayed(this, 2000);
				
			}
		};
		handler.postDelayed(r, 1000);
		if (!SystemPrefs.getInstance().isShownOnce(4210)) {
			new AlertDialog.Builder(this).setMessage("به بازی بگرد و ببر خوش اومدید \n به طور خلاصه این بازی به گونه ای طراحی شده است که شما باید هرروز در محیط مجموعه مارکار حضور داشته باشید و qr کد هایی را که در محیط هستند را اسکن کنید، سپس به سوالات مربوط به هر qr کد جواب دهید و امتیاز کسب کنید. \n در روز آخر به نفر اول تا سوم این مسابقه جایزه نقدی اهدا می گردد. \n\n این برنامه توسط کمیته فرهنگی و ورزشی سی و نهمین دوره مسابقات جام جانباختگان طراحی شده است")
					.setPositiveButton("چه خوب! بزن بریم", (dialog, which) -> {
						SystemPrefs.getInstance().setNotShownAgain(true,4210);
					}).setIcon(R.drawable.icon).create().show();
		}
		AppService.getInstance().getProfile(SystemPrefs.getInstance().getMobileNumber(), result1 -> {
			if (result1 != null) {
				
				if((result1.getFullName().trim()).length()<1){
					startActivity(new Intent(this, ChangeProfile.class));
				}
				//finish();
			}
		});
	}
	
	
	private void startLocationUpdates() {
		
		mSettingsClient
				.checkLocationSettings(mLocationSettingsRequest)
				.addOnSuccessListener(this, locationSettingsResponse -> {
					showToastMessage("درحال یافتن موقعیت مکانی شما...", Toast.LENGTH_SHORT);
					if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
						mFusedLocationClient.requestLocationUpdates(mLocationRequest,
								mLocationCallback, Looper.myLooper());
					}
					updateLocationUI();
				})
				.addOnFailureListener(this, e -> {
					int statusCode = ((ApiException) e).getStatusCode();
					switch (statusCode) {
						case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
							
							try {
								
								ResolvableApiException rae = (ResolvableApiException) e;
								rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
							} catch (IntentSender.SendIntentException sie) {
								Log.i("gpsss", "PendingIntent unable to execute request.");
							}
							break;
						case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
							
							
							Toast.makeText(MainActivity.this, "خطا در ارتباط با جی پی اس", Toast.LENGTH_LONG).show();
					}
					
					updateLocationUI();
				});
	}
	
	public boolean isMockSettingsON(Context context) {
		// returns true if mock location enabled, false if not enabled.
		if (Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
			return false;
		else
			return true;
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
			
			ArrayList<LatLng> polygon = new ArrayList<LatLng>();
			/*polygon.add(new LatLng(35.736041, 51.533793));
			polygon.add(new LatLng(35.735878, 51.535038));
			polygon.add(new LatLng(35.734664, 51.533577));
			polygon.add(new LatLng(35.734511, 51.534782));*/
			polygon.add(new LatLng(35.736231, 51.532868));
			polygon.add(new LatLng(35.736039, 51.535615));
			polygon.add(new LatLng(35.734385, 51.535389));
			polygon.add(new LatLng(35.734724, 51.532696));
			
			
			LatLng myLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
			//TODO fix it
			
			//InMarkar=isPointInPolygon(myLocation,polygon);
			InMarkar = PolyUtil.containsLocation(myLocation, polygon, true);
		}
		//InMarkar = true;
		
	}
	private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
		int intersectCount = 0;
		for (int j = 0; j < vertices.size() - 1; j++) {
			if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
				intersectCount++;
			}
		}
		
		return ((intersectCount % 2) == 1); // odd = inside, even = outside;
	}
	
	private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {
		
		double aY = vertA.latitude;
		double bY = vertB.latitude;
		double aX = vertA.longitude;
		double bX = vertB.longitude;
		double pY = tap.latitude;
		double pX = tap.longitude;
		
		if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
				|| (aX < pX && bX < pX)) {
			return false; // a and b can't both be above or below pt.y, and a or
			// b must be east of pt.x
		}
		
		double m = (aY - bY) / (aX - bX); // Rise over run
		double bee = (-aX) * m + aY; // y = mx + b
		double x = (pY - bee) / m; // algebra is neat!
		
		return x > pX;
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
					
					if (!isMockSettingsON(MainActivity.this)) {
						init();
						startLocationUpdates();
					} else {
						new AlertDialog.Builder(MainActivity.this)
								.setCancelable(false)
								.setPositiveButton("باشه", (dialog, which) -> {
									finish();
									android.os.Process.killProcess(android.os.Process.myPid());
									System.exit(1);
								})
								.setMessage("شما حالت moke location را برای موقعیت مکانی تقلبی فعال کرده اید و این خلاف قانون بازی می باشد. لطفا ابتدا ان را غیر فعال کنید و دوباره وارد برنامه شوید").create().show();
					}
				} else {
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
		if (handler != null)
			handler.removeCallbacksAndMessages(r);
		super.onStop();
	}
	
	
	private void switchTab(int position) {
		mNavController.switchTab(position);
	}
	
	
	@Override
	protected void onResume() {
		if (handler != null)
			handler.postDelayed(r, 1000);
		super.onResume();
	}
	
	
	@Override
	protected void onPause() {
		if (handler != null)
			handler.removeCallbacksAndMessages(r);
		
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
				this.doubleBackToExitPressedOnce = true;
				showToastMessage("برای خروج لطفا دوبار روی دکمه بازگشت لمس کنید", Toast.LENGTH_SHORT);
				handler.postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
				if (doubleBackToExitPressedOnce) {
					handler.removeCallbacksAndMessages(r);
					handler.removeCallbacks(r);
					super.onBackPressed();
					finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(1);
					
					handler = null;
					r = null;
				} else {
					return;
				}
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
	
	
	public void updateToolbarTitle(String title) {
		getSupportActionBar().setTitle(title);
		
	}
}
