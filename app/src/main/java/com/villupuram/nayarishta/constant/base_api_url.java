package com.villupuram.nayarishta.constant;

import android.content.Context;

/**
 * Created by vinit on 6/24/2017.
 */

public class base_api_url {



    public static final String BASE_URL = "http://nayarishta.com/nayarishta-app/api/";
    public static String PhotoUrl="",ReligioNname="",CastName="",SubcasteName="",MotherTangue="",Gotra="",HeightID="",HeightText="",EducationText="",Interests="",music_values="",FavoriteReads_valuew="",PreferredMovies_values="",SportsFitnessActivities_values="",FavoriteCuisine_values="",PreferredDressStyle_value="",BranchText="",ProfessionName="",AnnualIncomeName=""
            ,Email="", global_draftmessage="";

    public static final String USER_DETAIL = BASE_URL + "getUserInfo.php?userid=";
    public static final String USER_GALLERY = BASE_URL + "getGalleryInfo.php?userid=";
    public static final String EDUCATION_LIST = BASE_URL + "educations.php";
    public static final String BRANCHES_LIST = BASE_URL + "branches.php";
    public static final String PROFESSION_LIST = BASE_URL + "professions.php";
    public static final String ANNUAL_INCOM_LIST = BASE_URL + "income.php";
    public static final String MOTHER_LIST = BASE_URL + "motheris.php";
    public static final String FATHER_LIST = BASE_URL + "fatheris.php";
    public static final String RELIGIOUS = BASE_URL + "religious.php";
    public static final String CASTE = BASE_URL + "caste.php";
    public static final String SUBCASTE = BASE_URL + "subcaste.php";
    public static final String LANGUAGE = BASE_URL + "languages.php";
    public static final String COUNTRY = BASE_URL + "countries.php";
    public static final String COUNTRYLIST = BASE_URL + "getCountryName.php?countryid=";
    public static final String HEIGHTS = BASE_URL + "heightlist.php";
    public static final String SPECIAL_CASES = BASE_URL + "specialcases.php";
    public static final String COMPLEXION = BASE_URL + "complexion.php";
    public static final String BODYTYPE = BASE_URL + "bodytype.php";
    public static int globallogincheck=0;
    public static int bydefaultstatus=1;
    public static int FacebookloginOrnot=0;
    public static String varify_profile="", Check_FB_Rregistration="", basicdetails="";
    public static final String MyCOnstantPreference = "MyPrefs";








}
