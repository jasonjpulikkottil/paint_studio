package com.jdots.paint.app;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nikartm.button.FitButton;
import com.jdots.paint.MainActivity;
import com.jdots.filter.EditImageActivity;

import com.jdots.tuner.TuneActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class StartActivity extends AppCompatActivity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.paint_studioTheme);
  		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start);


		FitButton buttonCanvas = (FitButton) findViewById(R.id.buttonCanvas);
		FitButton buttonTuning = (FitButton) findViewById(R.id.buttonTune);
		FitButton buttonEdit = (FitButton) findViewById(R.id.buttonEdit);

		buttonCanvas.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				getPermission();
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				//  finish();
			}
		});

		buttonTuning.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			Intent intent = new Intent(getApplicationContext(), TuneActivity.class);
			startActivity(intent);
				//finish();

			}
		});


		buttonEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), EditImageActivity.class);
				startActivity(intent);
				//finish();

			}
		});


}
public void getPermission()
{
	try {
		Dexter.withContext(this)
				.withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.withListener(new PermissionListener() {
					@Override
					public void onPermissionGranted(PermissionGrantedResponse response) {

					}

					@Override
					public void onPermissionDenied(PermissionDeniedResponse response) {

					}

					@Override
					public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
				}).check();

	}catch(Exception ignored){}
}

}