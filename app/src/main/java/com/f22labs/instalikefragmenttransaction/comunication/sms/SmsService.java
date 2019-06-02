package com.f22labs.instalikefragmenttransaction.comunication.sms;

import com.f22labs.instalikefragmenttransaction.G;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ConfirmVerificationModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.SendVerificationModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ServiceCallback;
import com.f22labs.instalikefragmenttransaction.utils.NetworkUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SmsService {
	SmsApiService service;
	private static final SmsService instance = new SmsService();
	
	public static SmsService getInstance() {
		return instance;
	}
	
	private static String TAG = G.getInstance().getClass().getSimpleName();
	
	public SmsService() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(60, TimeUnit.SECONDS);
		builder.writeTimeout(60, TimeUnit.SECONDS);
		builder.readTimeout(60, TimeUnit.SECONDS);
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://begardobebar.pzandian.ir")
				.client(NetworkUtils.httpClientBuilder())
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		
		service = retrofit.create(SmsApiService.class);
		
	}
	
	public void SendVerification(String mobile, final ServiceCallback<SendVerificationModel> callback) {
		service.sendVerification(mobile).enqueue(new Callback<SendVerificationModel>() {
			@Override
			public void onResponse(Call<SendVerificationModel> call, Response<SendVerificationModel> response) {
				if (response != null)
					callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<SendVerificationModel> call, Throwable t) {
				callback.callback(null);
			}
		});
		
		
	}
	public void ConfirmVerification(String mobile,String code, final ServiceCallback<ConfirmVerificationModel> callback) {
		service.sendConfirmVerification(mobile,code).enqueue(new Callback<ConfirmVerificationModel>() {
			@Override
			public void onResponse(Call<ConfirmVerificationModel> call, Response<ConfirmVerificationModel> response) {
				if (response != null)
					callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<ConfirmVerificationModel> call, Throwable t) {
				callback.callback(null);
			}
		});
		
		
	}
	
}

