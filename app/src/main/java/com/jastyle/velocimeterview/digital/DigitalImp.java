package com.jastyle.velocimeterview.digital;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;

import com.jastyle.velocimeterview.utils.DimensionUtils;


/**
 * @author Adrián García Lomas
 */
public class DigitalImp implements Digital {

  private float value;
  private Typeface typeface;
  protected Paint digitPaint;
  protected Paint textPaint;
  private Context context;
  private float textSize;
  private int marginTop;
  private int color;
  private float centerX;
  private float centerY;
  private float correction;
  private String units;
  private Paint paintValue;
  private Paint paintTime;
  private float valueCorrection;
//  private String[] creditRanks = {"信用待检","信用太差","信用良好","信用极好"};
  private String curCreditRank;
  private String reportTime;
  private float gap;
  public DigitalImp(int color, Context context, int marginTop, int textSize, String units) {
    this.context = context;
    this.color = color;
    this.marginTop = marginTop;
    this.textSize = textSize;
    this.units = units;
    initPainter();
    initValues();
  }

  private void initPainter() {
    gap = DimensionUtils.getSizeInPixels(10,context);
    digitPaint = new Paint();
    digitPaint.setAntiAlias(true);
    digitPaint.setTextSize(textSize);
    digitPaint.setColor(color);
    digitPaint.setTextAlign(Paint.Align.CENTER);
    textPaint = new TextPaint();
    textPaint.setAntiAlias(true);
    textPaint.setTextSize(DimensionUtils.pixelsToSp(context,20));
    textPaint.setColor(color);
    textPaint.setTextAlign(Paint.Align.CENTER);
    paintValue = new Paint();
    paintValue.setAntiAlias(true);
    paintValue.setTextSize(DimensionUtils.pixelsToSp(context,15));
    paintValue.setColor(color);
    paintValue.setTextAlign(Paint.Align.CENTER);
    paintTime = new Paint();
    paintTime.setAntiAlias(true);
    paintTime.setTextSize(DimensionUtils.pixelsToSp(context,10));
    paintTime.setColor(color);
    paintTime.setTextAlign(Paint.Align.CENTER);
  }

  private void initValues() {
    correction = DimensionUtils.getSizeInPixels(10, context);
    valueCorrection = DimensionUtils.getSizeInPixels(35,context);

  }

  @Override
  public void setCreditRank(String creditRanks) {
    this.curCreditRank = creditRanks;
  }

  @Override
  public void setReportTime(String reportTime) {
    this.reportTime = reportTime;
  }

  @Override public void setValue(float value) {
    this.value = value;
  }

  @Override public void draw(Canvas canvas) {
    canvas.drawText(String.format("%.0f", value), centerX, centerY+gap,
        digitPaint);
    if (!TextUtils.isEmpty(curCreditRank)) {
      canvas.drawText(curCreditRank, centerX, (centerY) - valueCorrection,
              textPaint);
    }
    paintValue.setColor(Color.parseColor("#f93d3b"));
    canvas.drawText("0",valueCorrection,(int) (centerY*1.5),paintValue);
    paintValue.setColor(Color.parseColor("#05e3fc"));
    canvas.drawText("1000",centerX*2-valueCorrection,(int) (centerY*1.5),paintValue);
    if (!TextUtils.isEmpty(reportTime)) {
      canvas.drawText(reportTime,centerX,(int) (centerY*1.5),paintTime);
    }
  }

  @Override public void setColor(int color) {
    this.color = color;
  }

  @Override public int getColor() {
    return this.color;
  }

  @Override public void onSizeChanged(int height, int width) {
    this.centerX = width / 2;
    this.centerY = height / 2;
  }
}
