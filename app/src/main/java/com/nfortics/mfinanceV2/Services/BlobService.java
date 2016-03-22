package com.nfortics.mfinanceV2.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.nfortics.mfinanceV2.Request.BlobRequest;
import com.nfortics.mfinanceV2.Request.HTTPRequest;
import com.nfortics.mfinanceV2.Request.Request;
import com.nfortics.mfinanceV2.Service;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class BlobService extends Service {

	public static final String AVATAR_ID = "avatarId";
	private static final String AVATAR_PREFIX = "avatar_";

	private final Context context;

	public BlobService(Handler handler, Context context) {
		super(handler);
		this.context = context;
	}

	@Override
	public void processRequest(Request request) {
		// System.out.println("INFO >>> blob request"+request.getURL());
		BitmapDrawable drawable = getCachedPicture(request);
		if (drawable != null) {
			Log.i(getClass().getName(), "Found a cached image that matches");
			sendMessage(drawable, ((BlobRequest) request).getAvatarId());
		} else {
			BlobRequestProcessor processor = new BlobRequestProcessor(request);
			new Thread(processor).start();
		}
	}

	private BitmapDrawable getCachedPicture(Request request) {
		try {
			if (request instanceof BlobRequest) {
				BlobRequest blobRequest = (BlobRequest) request;
				FileInputStream inputStream = context
						.openFileInput(createFileName(blobRequest));
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				// Bitmap bitmap = decodeSampledBitmapFromResource(inputStream,
				// 30, 30);
				return new BitmapDrawable(bitmap);
			}
		} catch (FileNotFoundException e) {
			Log.i(getClass().getName(), "No cached picture available");
		} catch (Exception e) {
			Log.e(getClass().getName(),
					"Failed to retrieve picture from cache", e);
		}
		return null;
	}

	public static Bitmap decodeSampledBitmapFromResourceOld(
			InputStream inputStream, int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inJustDecodeBounds = true;
		// options.outHeight = 100;
		// options.outWidth = 100;
		Rect rect = new Rect(1, 1, 1, 1);
		// BitmapFactory.decodeStream(inputStream, rect, options);

		// Calculate inSampleSize
		options.inSampleSize = 2;
		// = calculateInSampleSize(options, reqWidth,
		// reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(inputStream, rect, options);
	}

	// trying to fix blurring problem with this method
	public static Bitmap decodeSampledBitmapFromResource(
			InputStream inputStream, int reqWidth, int reqHeight) {

		Rect rect = new Rect(1, 1, 1, 1);

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeStream(inputStream, rect, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeStream(inputStream, rect, options);
	}

	public static int calculateInSampleSizeOld(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	// trying to fix blurring problem with this method
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	private String createFileName(BlobRequest blobRequest) {

		String file = AVATAR_PREFIX + blobRequest.getAvatarId();
		// + "_" + blobRequest.getWidth() + "x" + blobRequest.getHeight();
		return file;
	}

	private void sendMessage(BitmapDrawable drawable, String avatarId) {
		Message message = handler.obtainMessage(1, drawable);
		Bundle data = new Bundle();
		data.putString(AVATAR_ID, avatarId);
		message.setData(data);
		handler.sendMessage(message);
	}

	private class BlobRequestProcessor implements Runnable {

		private final Request request;

		public BlobRequestProcessor(Request request) {
			this.request = request;
		}

		@Override
		public void run() {
			try {
				InputStream inputStream = new HTTPRequest()
						.processGetRequest(request);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				// BitmapFactory.decodeByteArray(bitmap.getNinePatchChunk(), 0,
				// bitmap.getNinePatchChunk().length);
				sendMessage(new BitmapDrawable(bitmap),
						((BlobRequest) request).getAvatarId());
				savePicture(bitmap);

			} catch (Exception e) {
				Log.e(getClass().getName(),
						"Failed to retrieve blob for request: "
								+ request.getURL(), e);
			}
		}

		private void savePicture(Bitmap bitmap) {
			try {
				if (request instanceof BlobRequest) {
					BlobRequest blobRequest = (BlobRequest) request;
					FileOutputStream outputStream = context.openFileOutput(
							createFileName(blobRequest), 0);
					boolean status = bitmap.compress(
							Bitmap.CompressFormat.JPEG, 90, outputStream);
					Log.i(getClass().getName(), "Bitmap compress status "
							+ status);
				}
			} catch (Exception e) {
				// sendMessage(new BitmapDrawable(bitmap), ((BlobRequest)
				// request).getAvatarId());
				Log.e(getClass().getName(),
						"Failed to cache picture with avatarId", e);
			}
		}
	}

}
