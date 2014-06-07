package com.baba.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View.OnTouchListener;


public class FirstMainActivity extends Activity {
	ListView list;
	int dgflag=0,currentpos;
	Button newaccbutton,forgetpw;
	InputStream in,in1,in2,in3;
	ScrollView parentScroll;
	BufferedReader reader,reader1,reader2;
	File[] users;
	String line,line1,line2,line3;
	String []web;
	String []rollweb;
	String []passweb;
	String []dobweb;
	 List<String> state = new ArrayList<String>();
	 List<String> rollstate = new ArrayList<String>();
	 List<String> passstate = new ArrayList<String>();
	 List<String> dobstate = new ArrayList<String>();
	 
	 String mypath,currentpass,currentusername,currentroll,currentdob;
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
		    		in2 = new FileInputStream(mypath+users[i].getName()+"/dob.txt");
		    	    reader = new BufferedReader(new InputStreamReader(in));
		    	    reader1 = new BufferedReader(new InputStreamReader(in1));
		    	    reader2 = new BufferedReader(new InputStreamReader(in2));
		    	    line = reader.readLine();in.close();
		    	    line1 = reader1.readLine();in1.close();
		    	    line2 = reader2.readLine();in2.close();
		    	    rollstate.add(line);
		    	    passstate.add(line1);
		    	    dobstate.add(line2);
		    	    
		    	    
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
	    
	     web = state.toArray(new String [state.size()] );
	     rollweb = rollstate.toArray(new String [rollstate.size()] );
	     passweb = passstate.toArray(new String [passstate.size()] );
	     dobweb = dobstate.toArray(new String [dobstate.size()] );
	   
	     
	     newaccbutton = (Button)findViewById(R.id.createnewacc);
	    
	    // Register button
	    newaccbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent it=new Intent(FirstMainActivity.this,Login.class);
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
	           
				/////////////////List long click listener//////////////
				
				
				
				
				
				
				
				/// To be Implemented in future////////////
				
				
				
				
				
				
				
				
				//////////////////////////Long click ends here////////////////
				
				
				
				
				
				
				
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
		            	currentdob=dobweb[position];
		            	dgflag=0;
		            	
		            	
		            	
		            	//////////////////////////New Dialog/////////////////////////////////
		            	
		            	final Dialog dialog = new Dialog(FirstMainActivity.this);
		    			dialog.setContentView(R.layout.dialog);
		    			dialog.setTitle("Enter Password");
		    			
		     
		    			// set the custom dialog components - text, image and button
		    			final TextView textd = (TextView) dialog.findViewById(R.id.dgpasstext);
		    			final EditText passd = (EditText) dialog.findViewById(R.id.dgpassedit);
		    			final TextView dobt = (TextView) dialog.findViewById(R.id.dobtext);
		    			final TextView dobe = (TextView) dialog.findViewById(R.id.dob);
		    			//EditText passd = (EditText) dialog.findViewById(R.id.dget);
		    			final TextView fpass = (TextView) dialog.findViewById(R.id.forgetpassdg);
		    			//fpass.setVisibility(View.GONE);
		    			dobt.setVisibility(View.GONE);
		    			dobe.setVisibility(View.GONE);
		    			
		    			
		    			fpass.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dobt.setVisibility(View.VISIBLE);
				    			dobe.setVisibility(View.VISIBLE);
				    			
				    			dialog.setTitle("Forgot Password");
				    			dobt.setText("Date Of Birth");
				    			textd.setText("Roll No.");
				    			passd.setText("");
				    			dgflag=1;
				    			fpass.setVisibility(View.GONE);
				    			
								
								
							}
						});
		    			
		    			textd.setText("Password");
		    			//ImageView image = (ImageView) dialog.findViewById(R.id.image);
		    			//image.setImageResource(R.drawable.ic_launcher);
		     
		    			Button dialogButton = (Button) dialog.findViewById(R.id.dgbut);
		    			Button cancelButton = (Button) dialog.findViewById(R.id.canceldg);
		    			
		    			//////////// Date Picker////////////////////
		    			
		    			final Calendar myCalendar = Calendar.getInstance();

		    			final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

		    			    @Override
		    			    public void onDateSet(DatePicker view, int year, int monthOfYear,
		    			            int dayOfMonth) {
		    			        // TODO Auto-generated method stub
		    			        myCalendar.set(Calendar.YEAR, year);
		    			        myCalendar.set(Calendar.MONTH, monthOfYear);
		    			        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		    			        String myFormat = "MM/dd/yy"; //In which you need put here
		    			        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		    			        dobe.setText(sdf.format(myCalendar.getTime()));
		    			    }

		    			};
		    			
		    			
		    			
		    			dobe.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								new DatePickerDialog(FirstMainActivity.this, date, myCalendar
					                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
					                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
								
								
							}
						});
		    			
		    			
		    			/////////////////////Date picker ended//////////////////////
		    			
		    			// if button is clicked, close the custom dialog
		    			dialogButton.setOnClickListener(new OnClickListener() {
		    				@Override
		    				public void onClick(View v) {
		    					
		    					if (dgflag==0)// In passchecking mode
		    					{
		    						
		    					
		    					 String value1 = passd.getText().toString();
				            	
				            	  //  Checking password
				            	  if (value1.equals(currentpass))
				            	  {
				            		  Toast.makeText(getApplicationContext(), "SUCCESSFUL LOGIN",Toast.LENGTH_SHORT).show();
				            		  Intent tc=new Intent(FirstMainActivity.this,TestConnection.class);
				            		  tc.putExtra("username1", currentusername);
				            		  tc.putExtra("roll1", currentroll);
				            		  dialog.dismiss();
				            		  startActivity(tc);
				            		  finish();
				    			
		    					
				            	  }
				            	  
				            	  else
				            	  {
				            		  
				            		  Toast.makeText(getApplicationContext(), "Authentication Failure", Toast.LENGTH_SHORT).show();
				            		  
				            		  
				            	  }
				            	  
		    					}
		    					
		    					
		    					
		    					else if (dgflag==1)// In roll and dob checking mode
		    					{
		    						String value1 = passd.getText().toString();
		    						String value2 = dobe.getText().toString();
		    						
		    						
		    						if (value1.equals(currentroll)&&value2.equals(currentdob))
					            	  {
		    							
		    							dgflag=2;
		    							dialog.setTitle("Reset Password");
						    			dobt.setVisibility(View.GONE);
						    			dobe.setVisibility(View.GONE);
						    			textd.setText("Enter New Password");
						    			passd.setText("");
					            	  }
					            	  
					            	  else
					            	  {
					            		  Log.d("my",currentroll+"\n"+value1+"\n"+currentdob+"\n"+value2);
					            		  Toast.makeText(getApplicationContext(), "Wrong Credentials...", Toast.LENGTH_SHORT).show();
					            		  
					            		  
					            	  }
		    								    						
		    					}
		    					
		    					
		    					else if (dgflag==2)// Password resetting mode
		    					{
		    						String value1=passd.getText().toString();
		    						
		    						if(!(value1.equals("")))
		    						{
		    							dialog.dismiss();
		    							Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
					            		///////////// File management///////////////////  
		    							
		    							File filedelete = new File(mypath+currentusername+"/pass.txt");
		    							filedelete.delete();
		    							
		    							File folder11 = new File(mypath+currentusername+"/");
		    							  folder11.mkdirs();
		    							  
		    							  try {
		    								  
		    								 PrintWriter writer11;
		    								  new File(folder11,"pass.txt");
		    								 
		    								  
		    									writer11 = new PrintWriter(mypath+currentusername+"/"+"pass.txt");
		    									writer11.print(passd.getText().toString());
		    									
		    									writer11.close();
		    									passweb[currentpos]=passd.getText().toString();
		    									
		    								  
		    									} catch (Exception e) {
		    										// TODO Auto-generated catch block
		    										e.printStackTrace();
		    									}
		    							
		    							
		    							
		    							
		    							
		    							///////////////////////////////////////////////////
		    							
		    						}
		    						
		    						else
		    							
		    						{
		    							Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
					            		  
		    							
		    						}
		    						
		    						
		    						
		    					}
		    					
				            	  
		    				}
		    			});
		     
		    			cancelButton.setOnClickListener(new OnClickListener() {
		    				@Override
		    				public void onClick(View v) {
		    				dialog.dismiss();
		    					dgflag=0;
		    					
		    				}
		    			});
		    			
		    			
		    			dialog.show();
		            	
		            	
		            	
		            	

		                /////////////////////////New Dialog end///////////////////////////////////
		                
		            }
		        });

				
				
				
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainfm, menu);
		return true;
	}
	
// Setting onclicks for options menu
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	
	if(item.getItemId()==R.id.action_aboutfm)
	{
		Toast.makeText(getApplicationContext(),"App Developed By:\n BABA Communications Pvt. Ltd. \n The Future Of Programming World",Toast.LENGTH_LONG).show();
	}
	
	else if (item.getItemId()==R.id.action_remove){
		
		Intent iw=new Intent(FirstMainActivity.this,DeleteAccount.class);
		iw.putExtra("users", web);
		startActivity(iw);
		finish();
		
		
	}
	
	else if (item.getItemId()==R.id.action_helpfm){
		
		Intent iw=new Intent(FirstMainActivity.this,Help.class);
		startActivity(iw);
	}
	
	else if (item.getItemId()==R.id.action_exit){
		Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startMain);
        finish();
	
		
	}
	
	
	
	return super.onOptionsItemSelected(item);
}

	
	
	
	// Double backpress exit
	private static long back_pressed;

	@Override
	public void onBackPressed()
	{
	        if (back_pressed + 2000 > System.currentTimeMillis()) {
	        	
	        	Intent startMain = new Intent(Intent.ACTION_MAIN);
	            startMain.addCategory(Intent.CATEGORY_HOME);
	            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(startMain);
	            finish();
	        	
	        	
	        }
	        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
	        back_pressed = System.currentTimeMillis();
	}
	
	
	
	
	
	
	
	
	
}
