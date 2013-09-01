package appMetro.activityMetro;

import android.app.Activity;

public class ActivityFacebook extends Activity {
	
}
//public class ActivityFacebook extends Activity implements DialogListener { 
//	private Facebook facebookClient; 
//	private LinearLayout facebookButton; 
//	private final String APP_API_ID = "XXXXXXXX"; 
//	
//	@Override 
//	protected void onCreate(Bundle savedInstanceState) { 
//		super.onCreate(savedInstanceState); 
//		facebookClient = new Facebook(); 
//		// replace APP_API_ID with your own 
//		facebookClient.authorize(this, APP_API_ID, 
//			new String[] {"publish_stream", "read_stream", "offline_access"}, this); 
//	} 
//	
//	public void onComplete(Bundle values) { 
//		if (values.isEmpty()){ 
////		"skip" clicked ? 
//		} 
//		// if facebookClient.authorize(...) was successful, this runs 
//		// this also runs after successful post 
//		// after posting, "post_id" is added to the values bundle 
//		// I use that to differentiate between a call from 
//		// faceBook.authorize(...) and a call from a successful post 
//		// is there a better way of doing this? 
//		
//		if (!values.containsKey("post_id")) { 
//			try { 
//				Bundle parameters = new Bundle(); 
//				parameters.putString("message", "YOUR TEXT TO SHARE GOES HERE");
//				// the message to post to the wall 
//				facebookClient.dialog(this, "stream.publish", parameters, this);
//				// "stream.publish" is an API call 
//			} catch (Exception e) { 
//				// TODO: handle exception 
//				System.out.println(e.getMessage()); 
//			} 
//		} 
//	} 
//	
//	@Override 
//	public void onError(DialogError e) { 
//		return; 
//	} 
//	
//	@Override 
//	public void onFacebookError(FacebookError e) { 
//		return; 
//	} 
//	
//	public void onCancel() { 
//		return; 
//	} 
//}