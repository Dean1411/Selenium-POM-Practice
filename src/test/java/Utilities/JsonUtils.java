package Utilities;

import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonUtils {
	
	private static JSONObject getJsonData() throws Exception {
		
		FileReader reader = new FileReader(
				System.getProperty("user.dir") + "/src//test/resources/jsonData/loginData.json"
				);
		
		 JSONTokener tokener = new JSONTokener(reader);
		 
		 return new JSONObject(tokener);
		
	}
	
	public static JSONArray getSignUpForLogin() throws JSONException, Exception {
		return getJsonData().getJSONArray("signup_to_login_with_user");
	}
	
    public static JSONArray getLoginUsers() throws Exception {
        return getJsonData().getJSONArray("loginUser");
    }

    public static JSONArray getRegisterUsers() throws Exception {
        return getJsonData().getJSONArray("registerUser");
    }

    public static JSONArray getSignupNames() throws Exception {
        return getJsonData().getJSONArray("signupName");
    }

}
