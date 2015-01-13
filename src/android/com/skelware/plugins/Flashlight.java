package com.skelware.plugins;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

public final class Flashlight extends CordovaPlugin {

	private Camera camera;

	@Override
	public boolean execute(final String action, final JSONArray args, final CallbackContext callback) {
		try {
			if (action.equals("isSupported")) {
				return isSupported();
			} else if (action.equals("enable")) {
				setEnabled(true);
			} else if (action.equals("disable")) {
				setEnabled(false);
			} else if (action.equals("request")) {
				request();
			} else if (action.equals("release")) {
				release(callback);
			} else {
				return false;
			}
		} catch (final Exception e) {
			callback.error(e.getMessage());
			return false;
		}
		return true;
	}

	private final boolean isSupported() throws Exception {
		request();

		final PackageManager packageManager = this.cordova.getActivity().getPackageManager();
		for (final FeatureInfo feature : packageManager.getSystemAvailableFeatures()) {
			if (PackageManager.FEATURE_CAMERA_FLASH.equalsIgnoreCase(feature.name)) {
				return true;
			}
		}
		camera = null;
		return false;
	}

	private final void setEnabled(final boolean enabled) throws Exception {
		final Camera.Parameters parameters = camera.getParameters();
		parameters.setFlashMode(enabled ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
		camera.setParameters(parameters);
		camera.startPreview();
	}

	private final void request() throws Exception {
		camera = Camera.open();
		if (Build.VERSION.SDK_INT >= 11) {
			camera.setPreviewTexture(new SurfaceTexture(0));
		}
	}

	private final void release(final CallbackContext callback) throws Exception {
		cordova.getThreadPool().execute(new Runnable() {
			public void run() {
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				callback.success();
			}
		});
	}
}
