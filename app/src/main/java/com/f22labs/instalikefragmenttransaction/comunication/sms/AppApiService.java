package com.f22labs.instalikefragmenttransaction.comunication.sms;

import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ConfirmVerificationModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.NewsModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ProfileInfo;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ProfileModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.QuestionModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.ScoreModel;
import com.f22labs.instalikefragmenttransaction.comunication.sms.models.SendVerificationModel;

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
	
}
