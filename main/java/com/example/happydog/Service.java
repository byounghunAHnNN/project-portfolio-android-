package com.example.happydog;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface Service {
    @FormUrlEncoded
    @POST("auth/android/login3/")
    Call<ResponseCode> getMember(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/android/join/")
    Call<ResponseCode2> getJoin(@Field("username") String username, @Field("password") String password, @Field("usernickname") String usernickname, @Field("address") String address, @Field("email") String email);

    @FormUrlEncoded
    @POST("auth/android/usernameCheck/")
    Call<ResponseCode2> getUsername(@Field("username") String username);

    @FormUrlEncoded
    @POST("auth/android/usernicknameCheck/")
    Call<ResponseCode2> getUsernickname(@Field("usernickname") String usernickname);

    @FormUrlEncoded
    @POST("auth/android/boards/")
    Call<ContentVo> getBoard(@Field("username") String username);

    @FormUrlEncoded
    @POST("auth/android/boardDetail/")
    Call<Board> getBoardDetail(@Field("id") Integer id);

    @FormUrlEncoded
    @POST("auth/android/boardWrite/")
    Call<ContentVo> AndroidWrite(@Field("title") String title, @Field("content_text") String content_text, @Field("content_image") String content_image, @Field("usernickname") String usernickname);

    @FormUrlEncoded
    @PUT("auth/android/boardModify/")
    Call<ContentVo> AndroidModify(@Field("id") Integer id, @Field("title") String title, @Field("content_text") String content_text, @Field("content_image") String content_image);

    @FormUrlEncoded
    @POST("auth/android/boardDelete/")
    Call<ContentVo> AndroidDelete(@Field("id") Integer id, @Field("usernickname") String usernickname);

    @FormUrlEncoded
    @POST("auth/android/boardDetail/replyDelete/")
    Call<Board> ReplyDelete(@Field("id") Integer id, @Field("usernickname") String usernickname, @Field("boardId") Integer boardId);

    @FormUrlEncoded
    @POST("auth/android/boardDetail/replyWrite/")
    Call<BoardVo> ReplyWrite(@Field("id") Integer id, @Field("usernickname") String usernickname, @Field("content") String content, @Field("reply") String reply);

    @FormUrlEncoded
    @POST("auth/android/userModify/")
    Call<ResponseCode2> Modifyinfo(@Field("usernickname") String usernickname, @Field("password") String password, @Field("email") String email);

//    @FormUrlEncoded
//    @POST("auth/android/employee")
//    Call<GpsVo> sendLatLng (@Field("latitude") String latitude, @Field("longitude") String longitude);
//
//    @FormUrlEncoded
//    @POST("/auth/android/employee")
//    Call<GpsVo> sendDistimer(@Field("distance")  String distance, @Field("timer") String timer, @Field("fee") String fee);

    @FormUrlEncoded
    @POST("auth/android/userDogModify/")
    Call<ResponseCode2> DogModify(@Field("id") Integer id, @Field("dog_name") String dog_name, @Field("breed") String breed, @Field("size") String size
            , @Field("weight") String weight, @Field("etc") String etc, @Field("dog_image") String dog_image, @Field("usernickname") String usernickname);

    @GET("auth/android/userDogDetail/")
    Call<PetVo> DogDetail(@Query("usernickname") String usernickname);

    @FormUrlEncoded
    @POST("auth/android/allMemos/")
    Call<MemoVo> allMemo(@Field("usernickname") String usernickname);

    @FormUrlEncoded
    @POST("auth/android/memoWrite/")
    Call<Memo> sendMemo(@Field("memo_content") String memocontent, @Field("memo_date") String memodate, @Field("usernickname") String usernickname);

    @FormUrlEncoded
    @POST("auth/android/dayMemo/")
    Call<MemoVo> getDayMemo(@Field("usernickname") String usernickname, @Field("memo_date") String memodate);

    @FormUrlEncoded
    @POST("auth/android/employer/")
    Call<getGps> getlocation(@Field("ownusernickname") String ownusernickname, @Field("usernickname") String usernickname);

    @FormUrlEncoded
    @POST("auth/android/employee/")
    Call<ResponseCode2> sendGps(@Field("distance") double distance, @Field("timer") double timer, @Field("fee") double fee, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("ownusernickname") String friendNickname ,@Field("usernickname") String usernickname);

    @FormUrlEncoded
    @POST("auth/android/gpsUser/")
    Call<gpsUserList> getGpsList(@Field("usernickname") String usernickname);

}
