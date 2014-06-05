package com.baba.chat;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;

public class Splash extends Activity
{

	
	@Override
	protected void onCreate(Bundle babarocks) {
		// TODO Auto-generated method stub
		super.onCreate(babarocks);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.splash);
		
		
		// Creating folder on launch if not present
		File folder = new File(Environment.getExternalStorageDirectory().toString()+"/AakashApp/");
		if(!(folder.exists()&&folder.isDirectory()))  
		{
		folder.mkdirs();
		} 
		  
		  // Showing splash screen for 3.5 seconds
		Thread timer1 =new Thread()
		{
			public void run()
			{
				try {
					sleep(3500);
					} 
				catch (InterruptedException e)
					{
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				finally
				{// Launching Signin screen 
					Intent openactivity1 =new Intent(Splash.this,FirstMainActivity.class);
					startActivity(openactivity1);
				}
			}
			
		};
		timer1.start();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		finish();
	}
	
	
	
	
}
