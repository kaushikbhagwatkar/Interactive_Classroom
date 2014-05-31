package com.baba.chat;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AudioDoubt extends Activity implements OnClickListener {

	AudioSession as = null;
	int queue = 1;
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	final Context context = this;
	TextView queuePos, status;
	ImageView cancel,startsp;
	Intent back;

	@Override
	protected void onCreate(Bundle qsavedInstanceState) {
		super.onCreate(qsavedInstanceState);
		//Log.e("Starting", "audiodoubt");
		setContentView(R.layout.activity_audio_doubt);
		//Log.e("Started", "audiodoubt");
		init(); // INITIALIZE VARIABLES
		receiver(); // RECEIVE STATUS FROM SERVER
		cancel.setOnClickListener(this);
	}

	private void receiver() {	//THIS FUNCTION HAS TO BE REFINED MUCH AND WILL BE DONE WHILE DECIDING WHAT HAS TO BE DONE IN SERVER
		// TODO Auto-generated method stub
		while (queue >= 0) // crap condition... to be changed
		{
			if (queue > 0) {
				status.setText("You are currently in the waiting queue.");
				queuePos.setText("Current Queue Position: " + queue);
			} else {
				status.setText("You are in the active list.");
				queuePos.setText("Wait for your turn.");
			}
			try{
				queue--;
				Thread.sleep(500);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		status.setText("Its your turn !!!");
		queuePos.setText("Start Speaking !!!");
		queuePos.setOnClickListener(this);	//IF THIS PRESSED, USER CAN START SPEAKING

	}

	public void init() {
		queuePos = (TextView) findViewById(R.id.queue_pos);
		status = (TextView) findViewById(R.id.state);
		cancel = (ImageView) findViewById(R.id.cancel_request);
		startsp = (ImageView) findViewById(R.id.start_speaking);
		
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
			as = new AudioSession();	//THIS OBJECT HANDLES ALL ASPECTS OF COMMUNICATION
			as.onRequestPress();	//START AUDIO MESSAGE
			break;
		case R.id.cancel_request:
			if (as != null) {
				as.onWithdrawPress();	//CANCEL AUDIO REQUEST OR STOP AUDIO MESSAGE
			}
			back=new Intent(AudioDoubt.this, AudioMainActivity.class);
			startActivity(back);
			finish();	//FINISH THIS ACTIVITY
			break;
		}
	}

	
	
	//onBackPressed
	
}
