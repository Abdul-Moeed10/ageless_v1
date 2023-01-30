package com.example.ageless_v1;

import com.google.firebase.auth.UserProfileChangeRequest;

public class UserInfo {
     String user_full_name;
     String user_username;
     String user_email;
     String user_phone_no;
     String user_password;
     String confirm_password;
     String account_type;
     String dob;
     String gender;
     String weight;
     String feet;
     String inches;
     String blood_group;
     String emergency_contact;
     String medical_info;

    public UserInfo(){

    }
    public String getUser_full_name(){
        return user_full_name;}

    public String getUser_username(){
        return user_username;}

    public String getUser_email(){
        return user_email;}

    public String getUser_phone_no(){
        return user_phone_no;}

    public String getUser_password(){
        return user_password;}

    public String getConfirm_password(){
        return confirm_password;}

    public String getAccount_type(){
        return account_type;}

    public String getDob(){
        return dob;}

    public String getGender(){
        return gender;}

    public String getWeight(){
        return weight;}

    public String getFeet(){
        return feet;}

    public String getInches(){
        return inches;}

    public String getBlood_group(){
        return blood_group;}

    public String getEmergency_contact(){
        return emergency_contact;}

    public String getMedical_info(){
        return medical_info;}


    public void setUser_full_name(String user_full_name){
        this.user_full_name=user_full_name;
    }
    public void setUser_username(String user_username){
        this.user_username=user_username;
    }
    public void setUser_email(String user_email){this.user_email=user_email;}
    public void setUser_phone_no(String user_phone_no){
        this.user_phone_no=user_phone_no;
    }
    public void setUser_password(String user_password){
        this.user_password=user_password;
    }
    public void setConfirm_password(String confirm_password){ this.confirm_password=confirm_password; }
    public void setAccount_type(String account_type){
        this.account_type=account_type;
    }
    public void setDob(String dob){ this.dob=dob;}
    public void setGender(String gender){ this.gender=gender;}
    public void setWeight(String weight){ this.weight=weight;}
    public void setFeet(String feet){ this.feet=feet;}
    public void setInches(String inches){ this.inches=inches;}
    public void setBlood_group(String blood_group){ this.blood_group=blood_group;}
    public void setEmergency_contact(String emergency_contact){ this.emergency_contact=emergency_contact;}
    public void setMedical_info(String medical_info){ this.medical_info=medical_info;}
}
