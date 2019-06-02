package com.f22labs.instalikefragmenttransaction.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.f22labs.instalikefragmenttransaction.G;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.f22labs.instalikefragmenttransaction.activities.Question;
import com.f22labs.instalikefragmenttransaction.comunication.sms.AppService;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.QuestionModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ServiceCallback;
import com.f22labs.instalikefragmenttransaction.utils.PermissionUtil;
import com.f22labs.instalikefragmenttransaction.utils.pref.SystemPrefs;

import butterknife.ButterKnife;

import static com.f22labs.instalikefragmenttransaction.G.InMarkar;


public class ScanFragment extends BaseFragment {
	
	View view;
	private Button button;
	private static final int REQUEST_CODE_QR_SCAN = 101;
	private final String LOGTAG = "QRCScanner-MainActivity";
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
			if (data == null)
				return;
			//Getting the passed result
			String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
			if (result != null) {
				AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
				alertDialog.setTitle("خطا");
				alertDialog.setMessage("خطا در اسکن کردن qr کد !");
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				alertDialog.show();
			}
			return;
			
		}
		if (requestCode == REQUEST_CODE_QR_SCAN) {
			if (data == null)
				return;
			showLoading();
			
			String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
			if(isValidQrCode(result))
				AppService.getInstance().getQuestion(result, SystemPrefs.getInstance().getMobileNumber(), new ServiceCallback<QuestionModel>() {
					@Override
					public void callback(QuestionModel result) {
						cancelLoading();
						if(result!=null){
							if(result.isStatus()){
							G.question=result;
							startActivity(new Intent(getActivity(), Question.class));}
							else{
								ToastMessage("شما قبلا این qr کد را اسکن کرده اید");
							}
						}
					}
				});

			
		}
	}
	
	private boolean isValidQrCode(String code) {
		String regEx;
		if (code.length() > 1) {
			regEx = "^[0-9]{2}$";
		} else {
			regEx = "^[0-9]{1}$";
		}
		
		return code.matches(regEx);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_scan, container, false);
		checkToken();

		button = view.findViewById(R.id.button_start_scan);
		button.setEnabled(false);
		final Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() {
				checkInMarkar();
				handler.postDelayed(this, 1000);
			}
		};
		handler.postDelayed(r, 1000);
		
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), QrCodeActivity.class);
				startActivityForResult(i, REQUEST_CODE_QR_SCAN);
			}
		});
		ButterKnife.bind(this, view);
		((MainActivity) getActivity()).updateToolbarTitle("اسکن کن");
		
		
		return view;
	}
	
	
	void checkInMarkar() {
		if (InMarkar) {
			view.findViewById(R.id.txtInMarkar).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.txtInMarkar).setVisibility(View.VISIBLE);
			
		}
		button.setEnabled(InMarkar);
		
	}
}
