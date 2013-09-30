package com.ghostwg.ftpsync;

import java.io.FileNotFoundException;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private static final String FTP_SERVER = "FTP_SERVER";
	private static final String FTP_CATALOG = "REMOTE_CATALOG";
	private static final String FTP_USER = "FTP_USER";
	private static final String FTP_PASS = "FTP_PASSWORD";
	private static final String LOCAL_CATALOG = "LOCAL_CATALOG";
	private static final String REMOTE_FILE = "REMOTE_FILE";
	private static final String LOCAL_FILE = "LOCAL_FILE";
	private static final String APP_PREFERENSES = "ftp_settings";
	EditText FTP_Server;
	EditText FTP_User;
	EditText FTP_Pass;
	EditText FTP_Cat;
	EditText LOCAL_Cat;
	EditText FTP_File;
	EditText LOCAL_File;
	Button	 btnSave;
	SharedPreferences sPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		FTP_Server = (EditText)findViewById(R.id.editText1);
		FTP_User = (EditText)findViewById(R.id.editText2);
		FTP_Pass = (EditText)findViewById(R.id.editText3);
		FTP_Cat = (EditText)findViewById(R.id.editText4);
		LOCAL_Cat = (EditText)findViewById(R.id.editText5);
		FTP_File = (EditText)findViewById(R.id.editText6);
		LOCAL_File = (EditText)findViewById(R.id.editText7);
		btnSave =(Button)findViewById(R.id.button1);
		LoadSettings();
	}
	
	public void btnSaveSet(View view){
		SaveSettings();
	}
	
	void SaveSettings(){
		sPref = getSharedPreferences(APP_PREFERENSES,0);
		Editor ed = sPref.edit();
	    ed.putString(FTP_SERVER, FTP_Server.getText().toString());
	    ed.putString(FTP_USER, FTP_User.getText().toString());
	    ed.putString(FTP_PASS, FTP_Pass.getText().toString());
	    ed.putString(FTP_CATALOG, FTP_Cat.getText().toString());
	    ed.putString(LOCAL_CATALOG, LOCAL_Cat.getText().toString());
	    ed.putString(REMOTE_FILE, FTP_File.getText().toString());
	    ed.putString(LOCAL_FILE, LOCAL_File.getText().toString());
	    ed.commit();
	    Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
	}
	
	void LoadSettings(){
		sPref = getSharedPreferences(APP_PREFERENSES,0);
	    String savedText = sPref.getString(FTP_SERVER, ""); 
	    FTP_Server.setText(savedText);
	    String savedText1 = sPref.getString(FTP_USER, "");
	    FTP_User.setText(savedText1);
	    String savedText2 = sPref.getString(FTP_PASS, "");
	    FTP_Pass.setText(savedText2);
	    String savedText3 = sPref.getString(FTP_CATALOG, "");
	    FTP_Cat.setText(savedText3);
	    String savedText4 = sPref.getString(LOCAL_CATALOG, "");
	    LOCAL_Cat.setText(savedText4);
	    String savedText5 = sPref.getString(REMOTE_FILE, "");
	    FTP_File.setText(savedText5);
	    String savedText6 = sPref.getString(LOCAL_FILE, "");
	    LOCAL_File.setText(savedText6);
	    
	    Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}