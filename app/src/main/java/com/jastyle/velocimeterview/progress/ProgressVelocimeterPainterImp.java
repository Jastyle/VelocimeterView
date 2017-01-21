package com.jastyle.velocimeterview.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;

import com.jastyle.velocimeterview.utils.DimensionUtils;


/**
 * @author Adrián García Lomas
 */
public class ProgressVelocimeterPainterImp implements ProgressVelocimeterPainter {

  private RectF circle;
  protected Paint paint;
  private int color;
  private int startAngle = 158;
  private int width;
  private int height;
  private float plusAngle = 24;
  private float max;
  private int strokeWidth;
  private int blurMargin;
  private int lineWidth;
  private int lineSpace;
  private Context context;
  private SweepGradient gradient;
  public ProgressVelocimeterPainterImp(int color, float max, int margin, Context context) {
    this.color = color;
    this.max = max;
    this.blurMargin = margin;
    this.context = context;
    initSize();
    init();
  }

  private void initSize() {
    this.lineWidth = DimensionUtils.getSizeInPixels(6, context);
//    this.lineSpace = DimensionUtils.getSizeInPixels(2, context);
    this.lineSpace = DimensionUtils.getSizeInPixels(4, context);
    this.strokeWidth = DimensionUtils.getSizeInPixels(20, context);
  }

  private void init() {
    initPainter();
  }

  private void initPainter() {
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(strokeWidth);
    paint.setColor(color);
    paint.setStyle(Paint.Style.STROKE);
    paint.setPathEffect(new DashPathEffect(new float[] { lineWidth, lineSpace }, 0));

  }

  private void initExternalCircle() {
    int padding = strokeWidth / 2 + blurMargin;
    circle = new RectF();
    circle.set(padding, padding, width - padding, height - padding);
    float[] dist = {0f,0.0523f,0.105f,0.157f,0.210f,0.262f,0.314f,0.366f,0.419f,0.471f,0.523f,0.576f,0.628f};
    int[] colors = {Color.parseColor("#f93d3b"),Color.parseColor("#ea404a"),Color.parseColor("#d14666"),
            Color.parseColor("#b24d87"),Color.parseColor("#8559b6"),Color.parseColor("#6463d6"),
            Color.parseColor("#4a6fef"),Color.parseColor("#387cfc"),Color.parseColor("#298efc"),
            Color.parseColor("#1da2fc"),Color.parseColor("#14b7fc"),Color.parseColor("#0dccfc"),Color.parseColor("#05e3fc")};
    gradient = new SweepGradient(width/2,height/2,colors,dist);
    Matrix matrix = new Matrix();
    matrix.setRotate(156, width / 2, height / 2);
    gradient.setLocalMatrix(matrix);
    paint.setShader(gradient);
  }

  @Override public void draw(Canvas canvas) {
    canvas.drawArc(circle, startAngle, plusAngle, false, paint);
  }

  @Override public void setColor(int color) {
    this.color = color;
    paint.setColor(color);
  }

  @Override public int getColor() {
    return color;
  }

  @Override public void onSizeChanged(int height, int width) {
    this.width = width;
    this.height = height;
    initExternalCircle();
  }

  public void setValue(float value) {
    this.plusAngle = (226f * value) / max;//圆环226度
  }

  public float getMax() {
    return max;
  }

  public void setMax(float max) {
    this.max = max;
  }
}
