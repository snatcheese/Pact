package com.example.application;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BlueHandler {

	private static final String TAG = "Jon";
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private static String address = "00:06:66:63:1F:69";
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	/*
	 * private InputStream inStream = null; Handler handler = new Handler();
	 * byte delimiter = 10; boolean stopWorker = false; int readBufferPosition =
	 * 0; byte[] readBuffer = new byte[1024];
	 */

	public CharSequence CheckBt() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!mBluetoothAdapter.isEnabled()) {
			return "Bluetooth désactivé !";
		}
		if (mBluetoothAdapter == null) {
			return "Bluetooth inexistant ou occupé !";
		}
		return null;
	}

	public void Connect() {
		Log.d(TAG, address);
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		Log.d(TAG, "Connexion en cours à " + device);
		mBluetoothAdapter.startDiscovery();
		try {
			btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			btSocket.connect();
			Log.d(TAG, "Connexion réussie !!");
		} catch (IOException e) {
			try {
				btSocket.close();
			} catch (IOException e2) {
				Log.d(TAG, "Impossible de fermer la connexion.");
			}
			Log.d(TAG, "Création de socket échouée.");
		}
	}

	public void writeData(int data) {
		try {
			outStream = btSocket.getOutputStream();
		} catch (IOException e) {
			Log.d(TAG, "Bug AVANT l'envoie.", e);
		}
		//String message = data;
		//byte[] msgBuffer = message.getBytes();

		try {
			outStream.write(data);
		} catch (IOException e) {
			Log.d(TAG, "Bug DURANT l'envoie.", e);
		}
	}

}
