package com.baba.chat;

import static android.media.MediaRecorder.AudioSource.MIC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.util.Log;


public class AudioSession {
	private boolean isRecording=false;
	public  AudioRecord recorder=null;
	private int port          =50005;
	private int sampleRate    =44100;
	private int channelConfig =AudioFormat.CHANNEL_IN_MONO;
	private int encodingFormat=AudioFormat.ENCODING_PCM_16BIT;
	int minBufSize=AudioRecord.getMinBufferSize(sampleRate,channelConfig,encodingFormat)+4096;
	int bufferSize=0;
	public String ipAddress="10.105.15.138";
	private static final String IPV4_NUM       ="([01]?\\d\\d?|2[0-4]\\d|25[0-5])";		//REGULAR EXPRESSION FOR IP NUM
	private static final String IP_DOT         ="\\.";
	private static final String IPV4_PATTERN   ="^"+IPV4_NUM+IP_DOT+IPV4_NUM+IP_DOT+IPV4_NUM+IP_DOT+IPV4_NUM+"$";	
	public static final  String PERMISSION_TEXT="You may start talking";
	DatagramSocket socket,socket1, socket2, socket3;

	
	public
	void stopStreaming(){	//TO STOP AUDIO RECORDER BY RELEASING IT
		
		if(recorder!=null){
		recorder.stop();
		recorder.release();
		recorder=null;
		}
	}

	public
	void startStreaming(){	//START AUDIO RECORDING
		//ipAddress=getIpAddress();
		//port=getPort();
		//if(!isValidIPAddressAndPort(ipAddress,port)) return;	//CHECK FOR VALID IP AND PORT
		//recordButton.setEnabled(false);
		//recordButton.setText("");
		//Log.e("startStreaming", "inStream");
		new Thread(new Runnable(){
			@Override
			public
			void run(){
				try{
					socket=new DatagramSocket();	//INITIALIZE SOCKET TO READ PACKETS
					byte[] buffer=new byte[minBufSize];
					DatagramPacket packet;
					final InetAddress destination=InetAddress.getByName(ipAddress);
					//Log.e("before recorder", "about to initialize");
					recorder=new AudioRecord(MIC,sampleRate,channelConfig,encodingFormat,minBufSize*10);	//INITIALIZE RECORDER
					//test starts
			
					
					//test ends
					if(recorder.getState()==AudioRecord.STATE_INITIALIZED)	//CHECK IF RECORDER INITIALIZED
						recorder.startRecording();
					else
						Log.e("not initialized","kuch aur kar");
					
					while(isRecording){		//KEEP ON RECORDING IN PARALLEL UNTILL STOP STREAMING IS CALLED
						bufferSize=recorder.read(buffer,0,buffer.length);
						packet=new DatagramPacket(buffer,buffer.length,destination,port);
						socket.send(packet);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					socket.close();
				}
			}
		}).start();
	}
	/*
	private
	String getIpAddress(){
		return ipAddressField.getText().toString();
	}

	private
	int getPort(){
		String portText=portField.getText().toString();
		return Integer.valueOf(portText.isEmpty()?"0":portText);
	}
*/
	
	
	public
	void onRequestPress(){	//RAISE REQUEST FOR AUDIO DOUBT
		final byte[] request=("Raise Hand").getBytes();
		new Thread(new Runnable(){
			@Override
			public
			void run(){
				try{
					//port=getPort();
					final InetAddress destination=InetAddress.getByName(ipAddress);
					socket1=new DatagramSocket();
					socket1.send(new DatagramPacket(request,request.length,destination,port));
					//Log.e("REquest", "Ssnt");
					while(waitingForPermission()) ;	//SEND PERMISSION
				}
				catch(SocketException e){
					e.printStackTrace();
				}
				catch(UnknownHostException e){
					e.printStackTrace();
				}
				catch(IOException e){
					e.printStackTrace();
				}
				finally{
					socket1.close();
				}
			}
		}).start();
	}

	private
	boolean waitingForPermission() throws IOException{	//WAITING TO RECEIVE PERMISSION TO START AUDIO DOUBT
		byte[] receiveData=new byte[8192];
		socket2=new DatagramSocket(port);
		
		DatagramPacket receivePacket=new DatagramPacket(receiveData,receiveData.length);
		//Log.e("waiting", "to receive");
		socket2.receive(receivePacket);
		//Log.e("Received","received");
		final String requestText=new String(receivePacket.getData());	//RECEIVE PACKET FROM SERVER
	//	Log.d("requestText",requestText);
		socket2.close();
		if(requestText.contains(PERMISSION_TEXT)){	//IF PERMISSION RECEIVED, START STREAMING
			isRecording=true;
			Log.e("startStreaming", "STREAMING");
			startStreaming();
			return false;
		}
		return true;
	}
/*
	public
	void onDefaultIP(View view){ipAddressField.setText("10.129.156.20");}

	public
	void onDefaultPort(View view){portField.setText("50005");}
*/
	public
	void onWithdrawPress(){	//IF WITHDRAW BUTTON PRESSED DURING STREAMING
		final byte[] request=("Withdraw").getBytes();
		new Thread(new Runnable(){
			@Override
			public
			void run(){
				try{
					//port=getPort();
					final InetAddress destination=InetAddress.getByName(ipAddress);
					socket3=new DatagramSocket();
					socket3.send(new DatagramPacket(request,request.length,destination,port));	//SEND WITHDRAW REQUEST TO SERVER
					isRecording=false;
					stopStreaming();	//STOP STREAMING
				}
				catch(SocketException e){
					e.printStackTrace();
				}
				catch(UnknownHostException e){
					e.printStackTrace();
				}
				catch(IOException e){
					e.printStackTrace();
				}
				finally{
					socket3.close();
				}
			}
		}).start();
	}
}
