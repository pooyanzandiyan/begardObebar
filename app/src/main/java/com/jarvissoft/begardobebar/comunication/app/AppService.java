package com.jarvissoft.begardobebar.comunication.app;

import com.jarvissoft.begardobebar.BegardObebarApplication;
import com.jarvissoft.begardobebar.comunication.models.AdsModel;
import com.jarvissoft.begardobebar.comunication.models.NewsModel;
import com.jarvissoft.begardobebar.comunication.models.ProfileInfo;
import com.jarvissoft.begardobebar.comunication.models.ProfileModel;
import com.jarvissoft.begardobebar.comunication.models.QuestionModel;
import com.jarvissoft.begardobebar.comunication.models.ScoreModel;
import com.jarvissoft.begardobebar.comunication.models.ServiceCallback;
import com.jarvissoft.begardobebar.utils.NetworkUtils;
import com.jarvissoft.begardobebar.utils.pref.SystemPrefs;


import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppService {
	AppApiService service;
	private static final AppService instance = new AppService();
	
	public static AppService getInstance() {
		return instance;
	}
	
	private static String TAG = BegardObebarApplication.getInstance().getClass().getSimpleName();
	
	public AppService() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(60, TimeUnit.SECONDS);
		builder.writeTimeout(60, TimeUnit.SECONDS);
		builder.readTimeout(60, TimeUnit.SECONDS);
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://begardobebar.pzandian.ir")
				.client(NetworkUtils.httpClientBuilder())
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		
		service = retrofit.create(AppApiService.class);
		
	}
	
	public void getNews(final ServiceCallback<List<NewsModel>> callback) {
		service.getNews().enqueue(new Callback<List<NewsModel>>() {
			@Override
			public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
				callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<List<NewsModel>> call, Throwable t) {
			callback.callback(null);
			}
		});
		
		
	}
	public void getProfile(String mobile,final ServiceCallback<ProfileModel> callback){
		service.getProfile(mobile).enqueue(new Callback<ProfileModel>() {
			@Override
			public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
				callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<ProfileModel> call, Throwable t) {
				callback.callback(null);
			}
		});
	}
	public void ChangeProfile(String mobile,String fullName,final ServiceCallback<String> callback){
		service.changeProfile(mobile,fullName).enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<String> call, Throwable t) {
				callback.callback(null);
			}
		});
	}
	public void setAvatar(String mobile,String id,final ServiceCallback<String> callback){
		service.setProfileImg(mobile,id).enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<String> call, Throwable t) {
				callback.callback(null);
			}
		});
	}
	public void getQuestion(String id,String mobile, final ServiceCallback<QuestionModel> callback){
		service.getQuestion(id,mobile).enqueue(new Callback<QuestionModel>() {
			@Override
			public void onResponse(Call<QuestionModel> call, Response<QuestionModel> response) {
				callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<QuestionModel> call, Throwable t) {
				callback.callback(null);
			}
		});
	}
	public void checkToken(final ServiceCallback<String> callback){
		service.checkToken(SystemPrefs.getInstance().getMobileNumber(), SystemPrefs.getInstance().getToken()).enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<String> call, Throwable t) {
				
				callback.callback(null);
			}
		});
	}
	public void setAnswer(String id,String answer,final ServiceCallback<String> callback){
		service.setAnswer(id,SystemPrefs.getInstance().getMobileNumber(), answer).enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<String> call, Throwable t) {
				
				callback.callback(null);
			}
		});
	}
	public void getProfileInfo(String mobile, final ServiceCallback<List<ProfileInfo>> callback){
		service.getProfileInfo(mobile).enqueue(new Callback<List<ProfileInfo>>() {
			@Override
			public void onResponse(Call<List<ProfileInfo>> call, Response<List<ProfileInfo>> response) {
				callback.callback(response.body());
				
			}
			
			@Override
			public void onFailure(Call<List<ProfileInfo>> call, Throwable t) {
				callback.callback(null);
			}
		});
	}
	public void getScore( final ServiceCallback<List<ScoreModel>> callback){
		service.getScore().enqueue(new Callback<List<ScoreModel>>() {
			@Override
			public void onResponse(Call<List<ScoreModel>> call, Response<List<ScoreModel>> response) {
				callback.callback(response.body());
				
			}
			
			@Override
			public void onFailure(Call<List<ScoreModel>> call, Throwable t) {
				callback.callback(null);
			}
		});
	}
	public  void getAds(final ServiceCallback<AdsModel> callback){
		service.getAds().enqueue(new Callback<AdsModel>() {
			@Override
			public void onResponse(Call<AdsModel> call, Response<AdsModel> response) {
				callback.callback(response.body());
			}
			
			@Override
			public void onFailure(Call<AdsModel> call, Throwable t) {
				callback.callback(null);
			}
		});
		
	}
}

