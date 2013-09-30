package com.ghostwg.ftpsync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    
    public boolean FTP_Connect(String server) {
        ftp = new FTPClient();
		try {
			Toast.makeText(this,"Подключаемся к: "+ server, Toast.LENGTH_SHORT).show();
			ftp.connect(server);
			} catch (SocketException e) {
			e.printStackTrace();
			Toast.makeText(this, "Имя сервера отсутствует", Toast.LENGTH_SHORT).show();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "Ошибка ввода-вывода", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true; 
    }

    public boolean FTP_Disconnect()
	{
	    try {
	        ftp.logout();
	        ftp.disconnect();
	        Toast.makeText(this,"Подключение завершено.", Toast.LENGTH_SHORT).show();
	        return true;
	    } catch (Exception e) {
	    	Toast.makeText(this,"Ошибка завершения подключения.", Toast.LENGTH_SHORT).show();
	    }
	    return false;
	} 

    
    public boolean FTP_Login(String User, String Pass){	
    	try {
                ftp.login(User, Pass);
                Toast.makeText(this,"Доступ разрешен!", Toast.LENGTH_SHORT).show();
                ftp.setFileType(ftp.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
            } catch(Exception e) {
        	Toast.makeText(this, "Ошибка: Некорректные учетные данные", Toast.LENGTH_SHORT).show();
        }
        return true;
     }
    
    public void FTP_CD(String Dir){
        try {
            ftp.changeWorkingDirectory(Dir);
            Toast.makeText(this, "Установка удаленного каталога " + Dir, Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
          Toast.makeText(this, "Ошибка: Отсутствует удаленный каталог " + Dir, Toast.LENGTH_SHORT).show();
        }
    }
    
    public boolean FTP_Download(String srcFilePath, String desFilePath)
    {
       
	try {
			output = new FileOutputStream(desFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(this,"Файл не найден", Toast.LENGTH_SHORT).show();
		}
		// get the file from the remote system
		try {
			ftp.retrieveFile(srcFilePath, output);
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this,"Ошибка загрузки файла (проверьте наличие)", Toast.LENGTH_SHORT).show();
			try {
				output.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		File file = new File(desFilePath);
		long length = file.length() / 1024;
		Toast.makeText(this,"Файл получен: " + desFilePath + " (" + length
				+ "Kb)", Toast.LENGTH_SHORT).show();
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
   }

   public boolean FTP_Upload(String dstFilePath, String srcFilePath)  {
 		
		try{
 			input = new FileInputStream(srcFilePath);
 		} catch (Exception e) {
 			Toast.makeText(this,"Файл отсутствует", Toast.LENGTH_SHORT).show();
 			return false;
 		}
 		try {
 			ftp.storeFile(dstFilePath, input);
 		} catch (IOException e) {
 			Toast.makeText(this,"Ошибка выгрузки файла (проверьте наличие)", Toast.LENGTH_SHORT).show();
 			try {
 				input.close();
 			} catch (IOException e1) {
 			
 				e1.printStackTrace();
 			}
 			return false;
 		}
 	
 		File file = new File(srcFilePath);
 		long length = file.length() / 1024;
 		Toast.makeText(this,"Файл выгружен: " + UFile + " (" + length
 				+ "Kb)", Toast.LENGTH_SHORT).show();
 		
 		try {
 			input.close();
 		}
 		catch(IOException e1) {}
 		return true;
 	}
    
	// обработчик нажатия кнопки получить данные	
    public void btnGdClick (View v){
       	FTP_Connect(FTP_Server);
    	FTP_Login(FTP_User, FTP_Pass);
    	FTP_CD(FTP_Dir);
    	//FTP_Download(FTP_Dir+"/"+DFile, LOCAL_Dir+"/"+DFile);
    	//FTP_Disconnect();
    }
     
    // обработчик нажатия кнопки отправить данные
    public void btnSdClick (View v){
    	FTP_Connect(FTP_Server);
    	FTP_Login(FTP_User, FTP_Pass);
    	FTP_CD(FTP_Dir);
    	//FTP_Upload(FTP_Dir+"/"+UFile,LOCAL_Dir+"/"+UFile);
    	//FTP_Disconnect();
     }
        
    // обработчик нажатия кнопки настройки
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
