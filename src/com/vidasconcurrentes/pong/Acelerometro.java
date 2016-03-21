package com.vidasconcurrentes.pong;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Acelerometro implements SensorEventListener {

	private SensorManager sm = null;
	private int x;

	public Acelerometro(Context context) {
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}

	public void register() {
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
	}

	public void unregister() {
		sm.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) { }

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			x = Math.round(event.values[0] * 100);
		}
	}

	public int getXInclination() {
		return x;
	}
}