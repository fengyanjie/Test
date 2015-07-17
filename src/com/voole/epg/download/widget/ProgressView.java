package com.voole.epg.download.widget;

import java.text.NumberFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.voole.epg.download.DisplayManager;
import com.voole.epg.download.base.BaseView;
import com.voole.epg.f4k.R;

class ProgressView extends BaseView {
	private Context context;
	private int background = -1;
	private int indexBackground = -1;
	private double progress;
	private double max;
	private Paint paint;
	private int textSize = DisplayManager.TEXTSIZE;
	private Rect clipBounds;

	public ProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ProgressView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
	}

	public void setProgressBackground(int background) {
		this.background = background;
	}

	public void setProgressIndexBackground(int indexBackground) {
		this.indexBackground = indexBackground;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public void setProgress(double progress) {
		if (progress <= max) {
			this.progress = progress;
		}
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (paint == null) {
			paint = new Paint();
			paint.setAntiAlias(true);
		}
		if (background == -1) {
			background = R.drawable.bg_progressbar;
		}
		if (indexBackground == -1) {
			indexBackground = R.drawable.bg_progress_index;
		}
		if (clipBounds == null) {
			clipBounds = canvas.getClipBounds();
			clipBounds.top = clipBounds.top + 8;
			clipBounds.bottom = clipBounds.bottom - 8;
		}

		if (max > 0 && progress >= max) {
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(textSize);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.WHITE);
			FontMetrics fontMetrics = paint.getFontMetrics();
			float fontHeight = fontMetrics.bottom - fontMetrics.top;
			float textBaseY = clipBounds.bottom
					- (clipBounds.height() - fontHeight) / 2
					- fontMetrics.bottom;
			canvas.drawText(getContext().getString(R.string.has_down), clipBounds.width() / 2, textBaseY, paint);
			return;
		}

		Bitmap backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), background);
		NinePatch backgroundDrawable = new NinePatch(backgroundBitmap,backgroundBitmap.getNinePatchChunk(), null);
		backgroundDrawable.draw(canvas, clipBounds);

		if (max > progress && progress > 0) {
			Bitmap indexbackgroundBitmap = BitmapFactory.decodeResource(context.getResources(), indexBackground);
			NinePatch indexbackground = new NinePatch(indexbackgroundBitmap,indexbackgroundBitmap.getNinePatchChunk(), null);
			int width = clipBounds.right - clipBounds.left;
			double section = (double)width / max;
			
			Rect indexRect = new Rect();
			indexRect.left = clipBounds.left;
			indexRect.top = clipBounds.top;
			indexRect.right = clipBounds.left + (int)(section * progress);
			indexRect.bottom = clipBounds.bottom;
			indexbackground.draw(canvas, indexRect);
		}
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(textSize);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		FontMetrics fontMetrics = paint.getFontMetrics();
		float fontHeight = fontMetrics.bottom - fontMetrics.top;
		float textBaseY = clipBounds.bottom- (clipBounds.height() - fontHeight) / 2 - fontMetrics.bottom;
		canvas.drawText(getCurrentPercent(), clipBounds.width() - 26,textBaseY, paint);

	}

	private String getCurrentPercent() {
		if (max < 0) {
			return "0%";
		}
		return NumberFormat.getPercentInstance().format(
				((double) progress / max));
	}

	public double getMaxCount() {
		return max;
	}

	public double getCurrentProgress() {
		return progress;
	}

}