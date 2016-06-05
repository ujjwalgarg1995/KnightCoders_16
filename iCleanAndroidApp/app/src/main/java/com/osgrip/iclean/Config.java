package com.osgrip.iclean;

/**
 * Created by Pranjal on 07-Jan-16.
 */
public class Config {
    // File upload url (replace the ip with your server address)
    private static final String BASE_URL="http://iclean.osgrip.com/";
    public static final String FILE_UPLOAD_URL = BASE_URL+"RegComplain.php";
    public static final String REFRESH_MY_COMPLAIN_URL = BASE_URL+"RefreshComplain.php";
    public static final String VOLUNTEER_REQUEST_URL = BASE_URL+"VolunteerReq.php";
    public static final String LOGIN_CITIZEN_URL = BASE_URL+"Register.php";
    public static final String ACCEPT_COMPLAIN_URL = BASE_URL+"complainalloted.php";
    public static final String FETCH_NEW_URL = BASE_URL+"getcomplains.php";
    public static final String LOGIN_VOLUNTEER_URL = BASE_URL+"vollogin.php";


    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Swachh AppDemo";
    public static final String wardList[]={ "----Select Ward----",
            "Ward 1", "Ward 2", "Ward 3", "Ward 4", "Ward 5",
            "Ward 6", "Ward 7", "Ward 8", "Ward 9", "Ward 10",
            "Ward 11", "Ward 12", "Ward 13", "Ward 14", "Ward 15",
            "Ward 16", "Ward 17", "Ward 18","Ward 19","Ward 20",
            "Ward 21","Ward 22","Ward 23","Ward 24","Ward 25"};
    public static final String[] vJobList = {"----Select Profession----","Service","Business","Student","Social Worker","House Wife","Other"};
}
