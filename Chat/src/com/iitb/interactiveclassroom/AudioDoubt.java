package com.iitb.interactiveclassroom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Currency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AudioDoubt extends Activity implements OnClickListener {

	AudioSession as = null;
	String queue = "1";
	Socket s,client_speak;;
	BufferedReader br;
	PrintWriter pw;
	final Context context = this;
	TextView queuePos, status;
	ImageView cancel,startsp;
	Intent back;
	Boolean flagg=true;
	ServerSocket recSocket;
	Handler uiHandler;
	Boolean runningFlag=true;
	DataInputStream rdis;

	
	
	@Override
	protected void onCreate(Bundle qsavedInstanceState) {
		super.onCreate(qsavedInstanceState);
		//Log.e("Starting", "audiodoubt");
		setContentView(R.layout.activity_audio_doubt);
		uiHandler=new Handler();
		//Log.e("Started", "audiodoubt");
		// INITIALIZE VARIABLES
		queuePos = (TextView) findViewById(R.id.queue_pos);
		status = (TextView) findViewById(R.id.state);
		cancel = (ImageView) findViewById(R.id.cancel_request);
		startsp = (ImageView) findViewById(R.id.start_speaking);
		startsp.setVisibility(View.GONE);
		//cancel.setOnClickListener(this);
		
		
		// RECEIVE STATUS FROM SERVER
		
		 Thread rt=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub

					try{
						Log.d("Lavish","Receive Thread running");
						//Toast.makeText(gh.getApplicationContext(), "Ghus gaya", Toast.LENGTH_LONG).show();
						//if(recSocket==null)
						
						recSocket= new ServerSocket(5570);
						
					while(runningFlag){
							
							client_speak=recSocket.accept();
						
						
						
					 // connect to the server
					//final DataOutputStream rdos=new DataOutputStream(recSocket.getOutputStream());
						
							 rdis=new DataInputStream(client_speak.getInputStream());
							
						
							queue=rdis.readUTF();
							
					
					Log.e("I Got","Data:"+queue);
					//Toast.makeText(gh.getApplicationContext(), "Read kar liya", Toast.LENGTH_LONG).show();

					runOnUiThread(new Runnable() {
						public void run() {
							
							
							if((queue.equals("start_speaking")))
							{
										
										Log.e("Inside got ","Data:"+queue);
										queuePos.setVisibility(View.GONE);
										
										startsp.setVisibility(View.VISIBLE);
										
								
							
							}
							
							else if(queue.equals("kick_you"))
							{
								Log.e("inside if", "inside if block");
								runningFlag=false;
								try {
									recSocket.close();
									Log.e("closing", "socket closed");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
									
									Log.e("Inside got ","Data:"+queue);
									
									if(as!=null)
			 						{as.onWithdrawPress();
									
									}
									back = new Intent(AudioDoubt.this, AudioMainActivity.class);
									  back.addCategory(Intent.CATEGORY_HOME);
							           // startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							            back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);															// SESSION
									startActivity(back);
									finish();
									
									
									
									
							
							
							}
							
							else
							{
								
								Log.e("inside else", "inside else block");
								
									// TODO Auto-generated method stub
									Log.e("Inside got ","Data:"+queue);
									queuePos.setText("Your position is "+queue);
									startsp.setVisibility(View.GONE);
										Log.e("PPPPPPPPPPPPPPPPPPPPPPPPP ","Data:"+queue);
							
							
								
							}
							
							
							
							
							
							
						}
					});
					
					
										
					
					}
					
					
					
					}
					
					catch(Exception e)
					{
						System.out.println(e.toString());
					}
					
					finally
					{
						Log.e("INSIDE FINALLY","HURRAYYYY");
					}
					
				}
			});
			rt.start();
				
				
			
			
			// Sending THREAD
				
				Thread t=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						try{
							Socket sendingSocket= new Socket(TestConnection.ip,5566); // connect to the server
							final DataOutputStream dos=new DataOutputStream(sendingSocket.getOutputStream());
							DataInputStream dis=new DataInputStream(sendingSocket.getInputStream());
							
							 dos.writeUTF(TestConnection.username);
							 dos.writeUTF(TestConnection.roll);
							 dos.writeUTF(TestConnection.macid);
							 dos.writeUTF(AudioMainActivity.topicname);
							 
							
							
						
							cancel.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stu
									runningFlag=false;
								try {
									dos.writeUTF("kick_me_out");
									dos.writeUTF(TestConnection.macid);
									try {
										recSocket.close();
										Log.e("closing", "socket closed");
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
									if(as!=null)
			 						{as.onWithdrawPress();
									
									}
									
									back = new Intent(AudioDoubt.this, AudioMainActivity.class);
									  back.addCategory(Intent.CATEGORY_HOME);
							           // startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							            back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);															// SESSION
									startActivity(back);
									finish();
								
									//Thread.currentThread().interrupt();
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
									
									
								}
							});
							
							
							
							}
							
							catch(Exception e)
							{
								
							}
								
							/*
							
								if (Integer.parseInt(queue) > 0) {
									status.setText("You are currently in the waiting queue.");
									queuePos.setText("Current Queue Position: " + queue);
								} else {
									status.setText("You are in the active list.");
									queuePos.setText("Wait for your turn.");
								}
						*/
						
						
					}
				});
					
					t.start();
					
				//}
					
					
				//status.setText("Its your turn !!!");
				//queuePos.setText("Start Speaking !!!");
				startsp.setOnClickListener(this);	//IF THIS PRESSED, USER CAN START SPEAKING
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.audio_doubt, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.start_speaking:
			Log.e("Lavish before", "AudioSession");
			as = new AudioSession();	//THIS OBJECT HANDLES ALL ASPECTS OF COMMUNICATION
			as.onRequestPress();

			Log.e(" Lavish safter", "AudioSession");//START AUDIO MESSAGE
			break;
		/*
		
		case R.id.cancel_request:
			if (as != null) {
				as.onWithdrawPress();	//CANCEL AUDIO REQUEST OR STOP AUDIO MESSAGE
			}
			
			else
			{
				
			}
			
			*/
			
		}
	}

	
	
	//onBackPressed
	
}


