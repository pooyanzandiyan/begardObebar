package com.jarvissoft.begardobebar.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jarvissoft.begardobebar.BegardObebarApplication;
import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.activities.AdsActivity;
import com.jarvissoft.begardobebar.activities.MainActivity;
import com.jarvissoft.begardobebar.activities.QuestionActivity;
import com.jarvissoft.begardobebar.comunication.app.AppService;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;
import com.jarvissoft.qrcodescanner.QrCodeActivity;

import butterknife.ButterKnife;

import static com.jarvissoft.begardobebar.BegardObebarApplication.InMarkar;


public class ScanFragment extends BaseFragment {
	
	View view;
	private Button button;
	public static final int REQUEST_CODE_QR_SCAN = 101;
	private final String LOGTAG = "QRCScanner-MainActivity";
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
			if (data == null)
				return;
			//Getting the passed result
			String result = data.getStringExtra("com.jarvissoft.qrcodescanner.error_decoding_image");
			if (result != null) {
				AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
				alertDialog.setTitle("خطا");
				alertDialog.setMessage("خطا در اسکن کردن qr کد !");
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
						(dialog, which) -> dialog.dismiss());
				alertDialog.show();
			}
			return;
			
		}
		if (requestCode == REQUEST_CODE_QR_SCAN) {
			if (data == null)
				return;
			showLoading();
			
			String result = data.getStringExtra("com.jarvissoft.qrcodescanner.got_qr_scan_relult");
			if (isValidQrCode(result))
				AppService.getInstance().getQuestion(result, SystemPrefs.getInstance().getMobileNumber(), result1 -> {
					cancelLoading();
					if (result1 != null) {
						if (result1.isStatus()) {
							if (result1.getQuestion() != null) {
								if (!result1.getQuestion().equals("")) {
									BegardObebarApplication.question = result1;
									startActivity(new Intent(getActivity(), QuestionActivity.class));
								} else {
									ToastMessage("سوالی برای این روز در سرور وجود ندارد.لطفا ساعاتی بعد مراجعه کنید");
								}
								
							} else {
								ToastMessage("سوالی برای این روز در سرور وجود ندارد.لطفا ساعاتی بعد مراجعه کنید");
							}
						} else {
							ToastMessage("شما قبلا این qr کد را اسکن کرده اید");
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
		button.setEnabled(true);
		final Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() {
				checkInMarkar();
				handler.postDelayed(this, 1000);
			}
		};
		handler.postDelayed(r, 1000);

		button.setOnClickListener(v -> {
			
			
			Intent i = new Intent(getActivity(), AdsActivity.class);
			startActivityForResult(i, REQUEST_CODE_QR_SCAN);
			
			
		});
		
		ButterKnife.bind(this, view);
		((MainActivity) getActivity()).updateToolbarTitle("اسکن کن");
		
		
		return view;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (button != null)
			if (!SystemPrefs.getInstance().isShownOnce(120))
				showFirstRuntimeHelp(button, "اسکن کن و جایزه ببر",
						"هروز همه جا های مارکار رو بگرد و qr کد ها رو اسکن کن و به سوالا جواب بده.هرکس امتیازش بیشتر شد برندس",
						120);
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
