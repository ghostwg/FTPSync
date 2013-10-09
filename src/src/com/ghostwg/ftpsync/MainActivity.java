package com.ghostwg.ftpsync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import android.support.v4.*;

import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	String FTP_Server;
	String FTP_Dir;
	String FTP_User;
	String FTP_Pass;
	String LOCAL_Dir;
	String DFile;
	String UFile;
	Button btnGD;
	Button btnSD;
	Button btnST;
	TextView tV1;
	SharedPreferences sPref;
	FTPClient ftp;
	FileOutputStream output;
	FileInputStream input;
		
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    // init variables from *.xml settings file
    public void init() {
    	sPref = getSharedPreferences("ftp_settings",0);
        FTP_Server = sPref.getString("FTP_SERVER", "");
    	FTP_User = sPref.getString("FTP_USER", "");
	    FTP_Pass = sPref.getString("FTP_PASSWORD", "");
	    FTP_Dir = sPref.getString("REMOTE_CATALOG", "");
	    LOCAL_Dir = sPref.getString("LOCAL_CATALOG", "");
	    DFile = sPref.getString("REMOTE_FILE", "");
	    UFile = sPref.getString("LOCAL_FILE", "");
	}
    // connection to ftp server
    public boolean FTP_Connect(final String server) {
		try {
		    ftp = new FTPClient();
		   msg("Подключаемся к: "+ server);	
			ftp.connect(server,21);
			} catch (SocketException e) {
				e.printStackTrace();
				msg("Имя сервера отсутствует");
			} catch (IOException e) {
				e.printStackTrace();
				msg("Ошибка ввода-вывода");
			}
		return true; 
    }
    // disconnection from ftp server
    public boolean FTP_Disconnect()
	{
	    try {
	        ftp.logout();
	        ftp.disconnect();
	        msg("Подключение завершено.");
	        return true;
	    } catch (Exception e) {
	    	msg("Ошибка завершения подключения.");
	    }
	    return false;
	} 
    // login to ftp server
    public boolean FTP_Login(String User, String Pass){	
    	try {
             ftp.login(User, Pass);
             msg("Доступ разрешен!");
             ftp.setFileType(ftp.BINARY_FILE_TYPE);
             ftp.enterLocalPassiveMode();
            } catch(Exception e) {
        	 msg("Ошибка: Некорректные учетные данные");
        }
        return true;
     }
    // change directory on ftp server
    public void FTP_CD(String Dir){
        try {
            ftp.changeWorkingDirectory(Dir);
            msg("Установка удаленного каталога " + Dir);
            } catch(Exception e) {
            msg("Ошибка: Отсутствует удаленный каталог " + Dir);
        }
    }
    // download file from ftp server
    public boolean FTP_Download(String srcFilePath, String desFilePath) {
    
    	try {
    		output = new FileOutputStream(desFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			msg("Файл не найден");
		}
		// get the file from the remote system
		try {
			ftp.retrieveFile(srcFilePath, output);
		} catch (IOException e) {
			e.printStackTrace();
			msg("Ошибка загрузки файла (проверьте наличие)");
			try {
				output.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		File file = new File(desFilePath);
		long length = file.length() / 1024;
		msg("Файл получен: "+desFilePath+" ("+ length+"Kb)");
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
   }
    // upload file from ftp server
    public boolean FTP_Upload(String dstFilePath, String srcFilePath)  {
 	
		try{
 			input = new FileInputStream(srcFilePath);
 		} catch (Exception e) {
 			msg("Файл отсутствует");
 			return false;
 		}
 		try {
 			ftp.storeFile(dstFilePath, input);
 		} catch (IOException e) {
 			msg("Ошибка выгрузки файла (проверьте наличие)");
 			try {
 				input.close();
 			} catch (IOException e1) {
				e1.printStackTrace();
 			}
 			return false;
 		}
 	
 		File file = new File(srcFilePath);
 		long length = file.length() / 1024;
 		msg("Файл выгружен: "+UFile+" ("+length+")");
 		
 		try {
 			input.close();
 		}
 		catch(IOException e1) {}
 		return true;
 	}
    // send message on activity
   	public void msg(String msg){
   		Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
   	}
	// main get file method	
    public void btnGdClick (View v){
       	new Thread(new Runnable() {
       		public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub
						try{
							FTP_Connect(FTP_Server);
							FTP_Login(FTP_User, FTP_Pass);
							FTP_CD(FTP_Dir);
							FTP_Download(FTP_Dir+"/"+DFile, LOCAL_Dir+"/"+DFile);
							FTP_Disconnect();
							}
						catch (Exception e){
							msg("Download Error");
							}
					}
				 });				
       		}
		}).start();
    }
    // main send file method
    public void btnSdClick (View v){
    	new Thread(new Runnable() {
       		public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub
						try{
							FTP_Connect(FTP_Server);
							FTP_Login(FTP_User, FTP_Pass);
							FTP_CD(FTP_Dir);
							FTP_Upload(FTP_Dir+"/"+UFile,LOCAL_Dir+"/"+UFile);
							FTP_Disconnect();
							}
						catch (Exception e){
							msg("Download Error");
							}
					}
				});
			}
       	}).start();
     }
    // main open settings method
    public void btnStClick (View V){
    	try{
    	Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    	}
    	catch (Exception e)
    	{
    		Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show();	
    	}
    }
    
  	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
