package com.iitb.interactiveclassroom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AudioMainActivity extends Activity implements OnClickListener {

	Socket s, socket;
	BufferedReader br;
	PrintWriter pw;
	final Context context = this;
	String serverResponse, option, textMsg, textSubject, macAddress,path;
	Intent confirm;
	ImageButton raiseHandAudio;
	ImageView dpaudio; 
	public static ArrayList<String> doubt=new ArrayList<String>();
	public static ArrayList<String> textMessage=new ArrayList<String>();
	EditText doubtText, doubtSubject;
	TextView hiname;
	public static TextView counter;
	static String macadd;
	Button sendDoubtText,viewHistory;;
	DataInputStream dis;
	DataOutputStream dos;
	public static int count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main_audiotext);
		init(); // INITIALIZING VARIABLES
		
		viewHistory.setOnClickListener(this); //LISTENER FOR VIEW HISTORY BUTTON
		
		path=Environment.getExternalStorageDirectory().toString()+"/AakashApp/"+TestConnection.username;
		
		raiseHandAudio.setOnClickListener(this); // LISTENER FOR AUDIO DOUBT
													// BUTTON
		sendDoubtText.setOnClickListener(this); // LISTENER FOR TEXT DOUBT
												// BUTTON
	
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bmp = BitmapFactory.decodeFile(path+"/dp.jpg",options);

		dpaudio.setImageBitmap(bmp);
		
		hiname.setText("  Hi "+TestConnection.username+" !!  ");
	
	
	
	}

	public void init() {
		raiseHandAudio = (ImageButton) findViewById(R.id.audio_doubt_button);
		doubtText = (EditText) findViewById(R.id.text_doubt_msg);
		sendDoubtText = (Button) findViewById(R.id.send_text_doubt);
		doubtSubject = (EditText) findViewById(R.id.doubt_subject);
		dpaudio=(ImageView)findViewById(R.id.dpaudio);
		hiname=(TextView)findViewById(R.id.hi_name);
		viewHistory = (Button) findViewById(R.id.view_history);
		counter=(TextView)findViewById(R.id.counter);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
// Setting onclicks for options menu
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	
	if(item.getItemId()==R.id.action_about)
	{
		Toast.makeText(getApplicationContext(),"App Developed By:\n BABA Communications Pvt. Ltd. \n The Future Of Programming World",Toast.LENGTH_LONG).show();
	}
	
	else if (item.getItemId()==R.id.action_settings){
		
		Intent iw=new Intent(AudioMainActivity.this,Settings.class);
		startActivity(iw);
		
		
		
	}
	
	else if (item.getItemId()==R.id.action_help){
		
		Intent iw=new Intent(AudioMainActivity.this,Help.class);
		startActivity(iw);
		
		
		
	}
	
else if (item.getItemId()==R.id.action_logout){
		
		/*Intent iw=new Intent(AudioMainActivity.this,Users.class);
		startActivity(iw);
		
		finish();
		*/
	Intent startMain = new Intent(AudioMainActivity.this,Users.class);
    startMain.addCategory(Intent.CATEGORY_HOME);
    //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(startMain);
    Toast.makeText(getApplicationContext(), "Logged out Successfully", Toast.LENGTH_SHORT).show();
    finish();
	
	
		
	}
	
	
	
	return super.onOptionsItemSelected(item);
}

	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.send_text_doubt:
			textMsg = doubtText.getText().toString(); // READ MESSAGE
			textSubject = doubtSubject.getText().toString(); // READ SUBJECT
			if (textMsg.isEmpty() || textSubject.isEmpty()) { // CHECK IF ANY OF
																// THE TWO FIELD
																// IS EMPTY
				Toast.makeText(AudioMainActivity.this, "All fields are mandatory",
						Toast.LENGTH_LONG).show();
			} else {
				createDialog(); // CREATE A CONFIRMATION DIALOG
			}
			break;
		case R.id.view_history:
			try{
					Intent vh=new Intent(AudioMainActivity.this,ViewHistory.class);
				  
				  vh.putStringArrayListExtra("doubtt",doubt);
				  vh.putStringArrayListExtra("textMessage", textMessage);
					 
				for(int i=0;i<doubt.size();i++)
					{Log.d("mohit","testing"+doubt.get(i));
					Log.d("mohit","testing  "+textMessage.get(i));
					
					}
				  startActivity(vh);
				 }
				 catch(Exception e)
				 {
					 Log.d("mohit", e.toString()+"  Starting of the activity");
				 }
				  break;
			
		case R.id.audio_doubt_button:
			confirm = new Intent(AudioMainActivity.this, AudioDoubt.class); // START
																		// AUDIO
																		// DOUBT
																		// SESSION
			startActivity(confirm);
			break;
		}
	}

	private void createDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("Submit this message?");
		builder.setMessage("Subject:- " + textSubject + "\nDoubt:- \n"
				+ textMsg);

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss(); // DISMISS DIALOG IF NO IS CLICKED
			}
		});
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.e("yes", "yes");
				count++;
				if(count<=5)
					{	
				doubt.add(doubtSubject.getText().toString());
				textMessage.add(doubtText.getText().toString());
				
				counter.setText("Doubts Remaining : "+(5-count));
						
						
						sendTextRequest();
					}
				else
					counter.setText("Doubts Remaining : 0");
				
					// SEND DOUBT IF YES IS CLICKED
				}});
		AlertDialog alert = builder.create(); // CREATE ALERT DIALOG
		alert.show();
	}

	private void sendTextRequest() {
		// TODO Auto-generated method stub

		Toast.makeText(getBaseContext(), "Sending...", Toast.LENGTH_LONG)
				.show();
		// public class ClientThread implements Runnable {
		new Thread() {

			public void run() {
				// TODO Auto-generated method stub

				try {

						socket = new Socket(TestConnection.ip, TestConnection.port);

					// Log.e("ClientActivity", "C: Sending command.");
						dos=new DataOutputStream(socket.getOutputStream());
						dis=new DataInputStream(socket.getInputStream());
				 
						dos.writeUTF("DOUBT");
					
					
				    
				//  	if(TestConnection.username!=null)
						dos.writeUTF(TestConnection.username);
						Log.d("mohit", "uname="+TestConnection.username);
						//	if(TestConnection.roll!=null)
						dos.writeUTF(TestConnection.roll);
						Log.d("mohit", "roll="+TestConnection.roll);
						
						//	if(TestConnection.macid!=null)
						dos.writeUTF(getMacAddress());
						Log.d("mohit", "macid="+getMacAddress());
							
				    
						dos.writeUTF(doubtSubject.getText().toString()); // SEND
						Log.d("mohit", "doubtsub="+doubtSubject.getText().toString());
																		// SUBJECT
						dos.writeUTF(doubtText.getText().toString()); // SEND
						Log.d("mohit", "doubtText="+doubtText.getText().toString());
																// MESSAGE
						//	pw.println(getMacAddress()); // SEND MAC ID
						
						
						//Image sending
						File filedp = new File(path+"/dp.jpg");
						sendFile(filedp);

						String msgServer = dis.readUTF(); // RECEIVE
						Log.d("mohit", "msgServer="+msgServer);
														// CONFIRMATION
														// IF MESSAGE
														// RECEIVED BY
														// SERVER
				    	
						if (msgServer.contains("received")) {
							Toast.makeText(AudioMainActivity.this, "Doubt Sent",
								Toast.LENGTH_SHORT).show();
						} 
						else {
						Toast.makeText(AudioMainActivity.this,
								"Server Error! Doubt not sent!",
								Toast.LENGTH_SHORT).show();
						}
					}
					catch (Exception e) {
					e.printStackTrace();

					}
					finally {
					if (socket != null) {
						try {
							// close socket connection after using it
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}// End run

		}.start();		//MESSAGE SENDING THREAD STARTED
	}
	public void sendFile(File file) throws IOException {
        FileInputStream fileIn = new FileInputStream(file);
        byte[] buf = new byte[Short.MAX_VALUE];
        int bytesRead;        
        while( (bytesRead = fileIn.read(buf)) != -1 ) {
            dos.writeShort(bytesRead);
            dos.write(buf,0,bytesRead);
        }
        dos.writeShort(-1);
        fileIn.close();
    }
	private String getMacAddress() {	//GET MAC ADDRESS ( WIFI CONNECTION NEEDED )
		// TODO Auto-generated method stub
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		return wInfo.getMacAddress();
	}
	
	
	
	 @Override
	    public void onBackPressed() {
	       
	      //  Toast.makeText(getApplicationContext(), "LogOut From Options To Go Back", Toast.LENGTH_SHORT).show();
		 DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			            //Yes button clicked
			        	/*
			        	Intent iw=new Intent(AudioMainActivity.this,Users.class);
			    		startActivity(iw);
			    		
			    		finish();
			    		*/
			        	
			        	Intent startMain = new Intent(AudioMainActivity.this,Users.class);
			            startMain.addCategory(Intent.CATEGORY_HOME);
			           // startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			            startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            startActivity(startMain);
			            Toast.makeText(getApplicationContext(), "Logged out Successfully", Toast.LENGTH_SHORT).show();
			            finish();
			        	
			        	
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			        	
			        	
			        	
			        	
			            break;
			        }
			    }
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure to LOGOUT and go back?").setPositiveButton("Yes", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener).show();
		 
		 
		 
		 
		 
	    }
	
	

}

