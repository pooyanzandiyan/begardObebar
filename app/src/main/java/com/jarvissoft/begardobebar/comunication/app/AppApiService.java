package com.jarvissoft.begardobebar.comunication.app;

import com.jarvissoft.begardobebar.comunication.models.AdsModel;
import com.jarvissoft.begardobebar.comunication.models.NewsModel;
import com.jarvissoft.begardobebar.comunication.models.ProfileInfo;
import com.jarvissoft.begardobebar.comunication.models.ProfileModel;
import com.jarvissoft.begardobebar.comunication.models.QuestionModel;
import com.jarvissoft.begardobebar.comunication.models.ScoreModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppApiService {
	
	@POST("getNews.php")
	Call<List<NewsModel>> getNews();
	@POST("getProfile.php")
	Call<ProfileModel> getProfile(@Query("mobile") String mobile);
	@POST("getProfileInfo.php")
	Call<List<ProfileInfo>> getProfileInfo(@Query("mobile") String mobile);
	@POST("getScore.php")
	Call<List<ScoreModel>> getScore();
	@POST("setProfile.php")
	Call<String> changeProfile(@Query("mobile") String mobile,@Query("fullName") String fullName);
	@POST("setAvatar.php")
	Call<String> setProfileImg(@Query("mobile") String mobile,@Query("imgId") String imgId);
	@POST("checkToken.php")
	Call<String> checkToken(@Query("mobile") String mobile,@Query("token") String token);
	@POST("getQuestion.php")
	Call<QuestionModel> getQuestion(@Query("id") String id,@Query("mobile") String mobile);
	@POST("setAnswer.php")
	Call<String> setAnswer(@Query("id") String id,@Query("mobile") String mobile,@Query("answer") String answer);
	@POST("getAds.php")
	Call<AdsModel> getAds();
	
}
