package com.jarvissoft.begardobebar.comunication.sms;

import com.jarvissoft.begardobebar.comunication.models.ConfirmVerificationModel;
import com.jarvissoft.begardobebar.comunication.models.SendVerificationModel;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SmsApiService {
	
	@POST("SendVerification.php")
	Call<SendVerificationModel> sendVerification(@Query("mobile") String mobile);
	@POST("ConfirmVerification.php")
	Call<ConfirmVerificationModel> sendConfirmVerification(@Query("mobile") String mobile, @Query("code") String code);
}
