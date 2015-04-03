package com.notificationlight;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;

import com.flask.colorpicker.ColorPickerDialogBuilder;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;

public class NotificationLight extends ActionBarActivity {

	private static final int LED_NOTIFICATION_ID = 0;
	private static int timeOn = 100;
	private static int timeOff = 100;
	protected static final String TAG = "NotificationLight";
	private int currentNotificationColor = 0xFFff0000;
	private Notification notif;
	private NotificationManager notificationManager;
	private EditText timeOnEditText, timeOffEditText, colorEditText;
	private Button testButton;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_light);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// get an instance of the Notification manager
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// create a notification
		notif = new Notification();
		// get the views
		timeOnEditText = (EditText) findViewById(R.id.editText_time_on);
		timeOffEditText = (EditText) findViewById(R.id.editText_time_btwn_flashes);
		colorEditText = (EditText) findViewById(R.id.editText_color);
		testButton = (Button) findViewById(R.id.button_test);
		// display the Color picker
		colorEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showColorPicker();
				}
			}
		});

		colorEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showColorPicker();
			}
		});
		// show the notication led
		testButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tOn = timeOnEditText.getText().toString().trim();
				String tOff = timeOffEditText.getText().toString().trim();
				try {
					timeOn = Integer.parseInt(tOn);
					timeOff = Integer.parseInt(tOff);
				} catch (NumberFormatException e) {
					Log.e(TAG, "Exception " + e.getLocalizedMessage());
				} finally {
					showNotificationLight();
				}
			}
		});
	}

	private void showNotificationLight() {
		notif.ledARGB = currentNotificationColor;
		notif.flags = Notification.FLAG_SHOW_LIGHTS;
		notif.ledOnMS = timeOn;
		notif.ledOffMS = timeOff;
		notificationManager.notify(LED_NOTIFICATION_ID, notif);
	}

	private void showColorPicker() {
		ColorPickerDialogBuilder
				.with(this)
				.setTitle("Choose color")
				.initialColor(currentNotificationColor)
				.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
				.density(12)
				.setOnColorSelectedListener(new OnColorSelectedListener() {
					@Override
					public void onColorSelected(int selectedColor) {
					}
				})
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int selectedColor) {
						currentNotificationColor = selectedColor;
						colorEditText.setText(String.format("#%06X",
								(0xFFFFFF & currentNotificationColor)));
					}
				})
				.setNegativeButton("cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).build().show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (notificationManager != null) {
			notificationManager.cancel(LED_NOTIFICATION_ID);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (notificationManager != null) {
			notificationManager.cancel(LED_NOTIFICATION_ID);
		}
	}
}
