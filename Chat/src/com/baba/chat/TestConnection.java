package com.baba.chat;


import java.io.BufferedReader; 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class TestConnection extends Activity
{
	private CharSequence status = null;
	Button b;
	EditText e1,e2;
	public static String ip;
	public static int port=5565;
	public static String sid,username,roll,macid;
	ImageView iv;
	String path;
	private Socket client;
	private PrintWriter printwriter;
	InputStream in,in1,in2,in3;
	BufferedReader reader;
	String line,line1,line2,line3;
	Socket socket = null;
	DataOutputStream dataOutputStream = null;
	DataInputStream dataInputStream = null;
	
	StringBuffer br=new StringBuffer();  //For Appending purpose
	
	TextView textIn;
	
	
		@Override
	protected void onCreate(Bundle kaushik) 
	{
			// TODO Auto-generated method stub
		super.onCreate(kaushik);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.testnew);
		b=(Button)findViewById(R.id.testbutton);
		e1=(EditText)findViewById(R.id.iptest);
		e2=(EditText)findViewById(R.id.sidtest);
		//textIn=(TextView)findViewById(R.id.read);
		
		
		
		// Setting ip to previous saved
		path=Environment.getExternalStorageDirectory().toString()+"/AakashApp/";
		File folder = new File(path);
		if(!(folder.exists()&&folder.isDirectory()) )
		{folder.mkdirs();
		} 
		  try {
			  
			  new File(folder, "config.txt");	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		
		try{
		in = new FileInputStream(path+"config.txt");
	    reader = new BufferedReader(new InputStreamReader(in));
	    line = reader.readLine();in.close();
		}
		catch(Exception e)
		{
			//Toast.makeText(getApplicationContext(), "Exception Occured..", Toast.LENGTH_SHORT).show();
		}
		
		e1.setText(line);
		
		
		
		
		
		
		
		
		//Getting username and roll from previous activity
		
		username = getIntent().getExtras().getString("username1");
		roll = getIntent().getExtras().getString("roll1");
		
		// getting macid 
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		macid = wInfo.getMacAddress();
	
		// Setting dp on testconnection
		
		iv=(ImageView)findViewById(R.id.dpconnect);
		String mypath=Environment.getExternalStorageDirectory().toString()+"/AakashApp/";
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bmp = BitmapFactory.decodeFile(mypath+username+"/dp.jpg",options);

		iv.setImageBitmap(bmp);
		TextView banner =(TextView)findViewById(R.id.banner);
		banner.setText("  Hi "+username+" !!  ");
		
		//Connect button
		b.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					InputMethodManager inputManager = (InputMethodManager)
		            getSystemService(Context.INPUT_METHOD_SERVICE); 
		
					inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);	
		
					
					ip = e1.getText().toString();
					sid = e2.getText().toString();
					
					
					Thread t=new Thread(new Runnable()
					{
						

						public void run()
						{
							try
							{
								//ip="10.105.15.204";
							    Log.d("mohit", "b4");
								Socket server= new Socket(ip,5565); // connect to the server
							     Log.d("mohit", "after");
								DataOutputStream dos=new DataOutputStream(server.getOutputStream());
								DataInputStream dis=new DataInputStream(server.getInputStream());
							   
								 Log.d("mohit", "aagain after");
								
								 dos.writeUTF("USER");
								
								
								
								 
								String sss="1";
								String rec=dis.readUTF();
								if(sss.equals(rec))
								{  Log.d("mohit", "success");
									//dos.writeUTF(ip);
									dos.writeUTF(sid);
									status=dis.readUTF();
									
									////////////////
									
									
									if (status.equals("1"))
									{
										Log.d("mohit","GHUS GAYA");
								  /*  	if(username!=null)
										dos.writeUTF(username);
										if(roll!=null)
										dos.writeUTF(roll);
										if(macid!=null)
										dos.writeUTF(macid);
									*/	
									}
									server.close();
								}
								else
								{
									Log.d("mohit", "rec  =  "+rec);
								}
							}
							catch(Exception exp)
							{
								//Toast.makeText(getApplicationContext(), "you got some exception", Toast.LENGTH_LONG).show();
								Log.d("Lavish","your Exception : "+exp);
								//textIn.setText(exp.toString());
							}
							//
							
						}
					});
				
					t.start();
					
					try {
						t.join(0);
					} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				
					////////////////////////////////////////////////////
					
					// Possible status values
					
					if (status==null)
					{
						Toast.makeText(getApplicationContext(), "Please check your Connection and IP settings", Toast.LENGTH_LONG).show();
					}
					
					
					else if (status.equals("0"))
					{
						Toast.makeText(getApplicationContext(), "Session ID Mismatch. \nConnection Aborted.", Toast.LENGTH_LONG).show();
					}
					
					else if (status.equals("1"))
					{
						Toast.makeText(getApplicationContext(), "CONNECTION SUCCESSFUL...", Toast.LENGTH_LONG).show();
						
						//// Saving ip in file////
						
						

						String newadd = e1.getText().toString();
						
						
						PrintWriter writer;
						try {
							writer = new PrintWriter(path+"config.txt");
							writer.print(newadd);
							
							writer.close();
							
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
						
						/////////////////
						
						
						
						
						Intent rc=new Intent(TestConnection.this,AudioMainActivity.class);
						startActivity(rc);
						finish();
						
						
					}
					
					else 
					{
						Toast.makeText(getApplicationContext(), "Something Went Wrong...", Toast.LENGTH_LONG).show();
					}
					
					status=null;
					
					
					//////////////////////////////////////////////
					
				}
			});

	}
		
		
		  @Override
		    public void onBackPressed() {
		       
		        startActivity(new Intent(TestConnection.this,AudioMainActivity.class));
		        finish();
		    }
		
}