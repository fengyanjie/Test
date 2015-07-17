package com.voole.epg.download.base;

import android.widget.Toast;

public class TVToast {
	public static void show(final Toast toast, final long durationInMilliseconds) {
		toast.setDuration(Toast.LENGTH_SHORT);
		Thread t = new Thread() {
			long timeElapsed = 0l;
			public void run() {
				try {
					while (timeElapsed <= durationInMilliseconds) {
						long start = System.currentTimeMillis();
						toast.show();
						sleep(1750);
						timeElapsed += System.currentTimeMillis() - start;
					}
				} catch (InterruptedException e) {
				}
			}
		};
		t.start();
	}
}
