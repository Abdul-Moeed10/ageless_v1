package com.example.ageless_v1;

public class UserInfo {
    private String user_full_name;
    private String user_email;
    private String user_phone_no;
    private String user_password;

    public UserInfo(){

    }
    public String getUser_full_name(){
        return user_full_name;}

    public String getUser_email(){
        return user_email;}

    public String getUser_phone_no(){
        return user_phone_no;}

    public String getUser_password(){
        return user_password;}

    public void setUser_full_name(String user_full_name){
        this.user_full_name=user_full_name;
    }
    public void setUser_email(String user_email){this.user_email=user_email;}
    public void setUser_phone_no(String user_phone_no){
        this.user_phone_no=user_phone_no;
    }
    public void setUser_password(String user_password){
        this.user_password=user_password;
    }
}