package com.baba.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View.OnTouchListener;


public class FirstMainActivity extends Activity {
	ListView list;
	Button newaccbutton;
	InputStream in,in1,in2,in3;
	ScrollView parentScroll;
	BufferedReader reader,reader1;
	File[] users;
	String line,line1,line2,line3;
	 List<String> state = new ArrayList<String>();
	 List<String> rollstate = new ArrayList<String>();
	 List<String> passstate = new ArrayList<String>();
	 
	 String mypath,currentpass,currentusername,currentroll;
	 int statecount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.first_activity_main);
		TextView noacc = (TextView)findViewById(R.id.heading);
		
		//
		try{
		mypath=Environment.getExternalStorageDirectory().toString()+"/AakashApp/";
		
	       int sd=0;statecount=0;
	       users = new File(mypath).listFiles();
	       
	       // Storing all usernames in state and retriving password and roll nos in same order
	       for (int i=0;i < users.length;i++) {
	           // System.out.println(users[i]);
	            if (users[i].isDirectory())
		    	{
		    		state.add(users[i].getName());
		    		in = new FileInputStream(mypath+users[i].getName()+"/roll.txt");
		    		in1 = new FileInputStream(mypath+users[i].getName()+"/pass.txt");
		    	    reader = new BufferedReader(new InputStreamReader(in));
		    	    reader1 = new BufferedReader(new InputStreamReader(in1));
		    	    line = reader.readLine();in.close();
		    	    line1 = reader1.readLine();in1.close();
		    	    rollstate.add(line);
		    	    passstate.add(line1);
		    	    
		    	    
		    		statecount++;
		    	}
		    	
	            
	            
	        }
	
		}
		catch(Exception e)
		{
			//Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "No Accounts Created Yet", Toast.LENGTH_LONG).show();
			noacc.setText("NO ACCOUNTS");
			
		}
		
		
		if (users==null|| users.length==0)
		{
			Toast.makeText(getApplicationContext(), "No Accounts Created Yet", Toast.LENGTH_LONG).show();
			noacc.setText("NO ACCOUNTS");
			
		}
		
		// Converting arraylist to array
	    
	    final String [] web = state.toArray(new String [state.size()] );
	    final String [] rollweb = rollstate.toArray(new String [state.size()] );
	    final String [] passweb = passstate.toArray(new String [state.size()] );
	    
	    newaccbutton = (Button)findViewById(R.id.createnewacc);
	    
	    // Register button
	    newaccbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent it=new Intent("com.baba.chat.LOGIN");
				startActivity(it);
				finish();
				
				
				
			}
		});
	    
				// Setting listview
				CustomList adapter = new CustomList(FirstMainActivity.this, web,rollweb);
				list=(ListView)findViewById(R.id.list);
				list.setAdapter(adapter);
				list.setScrollingCacheEnabled(false);
				
				
				// Scrolling listview
				
				parentScroll=(ScrollView)findViewById(R.id.scrollout);
				
				parentScroll.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						findViewById(R.id.list).getParent()
                        .requestDisallowInterceptTouchEvent(false);
						return false;
					}
	            });
	           
				
				list.setOnTouchListener(new View.OnTouchListener() {

	                public boolean onTouch(View v, MotionEvent event) {
	                   // Log.v("CHILD", "CHILD TOUCH");
	                    // Disallow the touch request for parent scroll on touch of
	                    // child view
	                    v.getParent().requestDisallowInterceptTouchEvent(true);
	                    return false;
	                }
	            });
				
				
				
				
				
				
				
				////////////////////////
				
				
				
				
				// Giving pop up and validation
				
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		            @Override
		            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		            	
		            	currentpass=passweb[position];
		            	currentroll=rollweb[position];
		            	currentusername=web[position];
		            	
		            	
		            	AlertDialog.Builder alert = new AlertDialog.Builder(FirstMainActivity.this);

		            	alert.setTitle("Enter Password");
		            	alert.setMessage("Password");

		            	// Set an EditText view to get user input 
		            	final EditText input = new EditText(FirstMainActivity.this);
		            	input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		            	alert.setView(input);

		            	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		            	public void onClick(DialogInterface dialog, int whichButton) {
		            	  String value = input.getText().toString();
		            	  
		            	  //  Checking password
		            	  if (value.equals(currentpass))
		            	  {
		            		  Toast.makeText(getApplicationContext(), "SUCCESSFUL LOGIN",Toast.LENGTH_SHORT).show();
		            		  Intent tc=new Intent("com.baba.chat.TESTCONNECTION");
		            		  tc.putExtra("username1", currentusername);
		            		  tc.putExtra("roll1", currentroll);
		            		  
		            		  startActivity(tc);
		            		 // finish();
		            		  
		            		  
		            	  }
		            	  
		            	  else
		            	  {
		            		  Toast.makeText(getApplicationContext(), "FAILURE",Toast.LENGTH_SHORT).show();
		            	  }
		            	  
		            	  }
		            	});

		            	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            	  public void onClick(DialogInterface dialog, int whichButton) {
		            	    // Canceled.
		            	  }
		            	});

		            	alert.show();
		            	
		            	
		               

		                
		                
		            }
		        });

				
				
				
	}
		
	// Double backpress exit
	private static long back_pressed;

	@Override
	public void onBackPressed()
	{
	        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
	        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
	        back_pressed = System.currentTimeMillis();
	}
	
}
