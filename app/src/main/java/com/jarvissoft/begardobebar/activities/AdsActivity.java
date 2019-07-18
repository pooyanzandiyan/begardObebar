package com.jarvissoft.begardobebar.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;

import com.jarvissoft.begardobebar.R;
import com.jarvissoft.begardobebar.comunication.app.AppService;
import com.jarvissoft.qrcodescanner.QrCodeActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.jarvissoft.begardobebar.fragments.ScanFragment.REQUEST_CODE_QR_SCAN;

public class AdsActivity extends BaseActivity {
	CountDownTimer countDownTimer;
	private final String GOT_RESULT = "com.jarvissoft.qrcodescanner.got_qr_scan_relult";
	private final String ERROR_DECODING_IMAGE = "com.jarvissoft.qrcodescanner.error_decoding_image";
	private final String LOGTAG = "QRScannerQRCodeActivity";
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ads);
		Button btn = findViewById(R.id.btnAds);
		ImageView img = findViewById(R.id.imgAds);
		showLoading();
		AppService.getInstance().getAds(result -> {
			if (result != null) {
				Picasso.get().load(result.getImgUrl()).into(img, new Callback() {
					@Override
					public void onSuccess() {
						cancelLoading();
						countDownTimer = new CountDownTimer(2000, 1000) {
							@Override
							public void onTick(long millisUntilFinished) {
								btn.setText(String.format("امکان بستن تبلیغ در %s ثانیه دیگر", millisUntilFinished / 1000));
							}
							
							@Override
							public void onFinish() {
								btn.setText("بستن تبلیغ");
								btn.setOnClickListener(v -> {
									Intent i = new Intent(AdsActivity.this, QrCodeActivity.class);
									startActivityForResult(i, REQUEST_CODE_QR_SCAN);
								});
							}
						}.start();
						img.setOnClickListener(v -> {
							if (result.getSiteUrl() != null)
								if (!result.getSiteUrl().equals(""))
									startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(result.getSiteUrl())));
						});
					}
					
					@Override
					public void onError(Exception e) {
						shortToastMessage("خطا در برقراری ارتباط با سرور");
						cancelLoading();
					}
				});
				
				
			}
			
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		setResult(Activity.RESULT_OK, data);
		finish();
		
	}
}
