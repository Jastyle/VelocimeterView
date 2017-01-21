package com.jastyle.velocimeterview.velocimeter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.jastyle.velocimeterview.R;
import com.jastyle.velocimeterview.digital.Digital;
import com.jastyle.velocimeterview.digital.DigitalImp;
import com.jastyle.velocimeterview.progress.ProgressVelocimeterPainter;
import com.jastyle.velocimeterview.progress.ProgressVelocimeterPainterImp;
import com.jastyle.velocimeterview.utils.DimensionUtils;


/**
 * The type Velocimeter view.
 *
 * @author Adrián García Lomas
 */
public class VelocimeterView extends View {

  private ValueAnimator progressValueAnimator;
  private ValueAnimator nidleValueAnimator;
  private Interpolator interpolator = new AccelerateDecelerateInterpolator();
  private InternalVelocimeterPainter internalVelocimeterPainter;
  private ProgressVelocimeterPainter progressVelocimeterPainter;
  private Digital digitalPainter;
  private int min = 0;
  private float progressLastValue = min;
  private float nidleLastValue = min;
  private int max = 1000;
  private float value;
  private int duration = 500;
  private long progressDelay = 250;
  private int margin = 15;
  private int insideProgressColor = Color.parseColor("#dadada");
  private int externalProgressColor = Color.parseColor("#9cfa1d");
  private int digitalNumberColor = Color.parseColor("#89abe3");
  private String creditRank;
  private String reportTime;

  /**
   * Instantiates a new Velocimeter view.
   *
   * @param context the context
   * @param attrs   the attrs
   */
  public VelocimeterView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  /**
   * Instantiates a new Velocimeter view.
   *
   * @param context      the context
   * @param attrs        the attrs
   * @param defStyleAttr the def style attr
   */
  public VelocimeterView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int size;
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    if (width > height) {
      size = height;
    } else {
      size = width;
    }
    setMeasuredDimension(size, size);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    internalVelocimeterPainter.onSizeChanged(h, w);
    progressVelocimeterPainter.onSizeChanged(h, w);
    digitalPainter.onSizeChanged(h, w);
  }

  private void init(Context context, AttributeSet attributeSet) {
    TypedArray attributes =
        context.obtainStyledAttributes(attributeSet, R.styleable.VelocimeterView);
    initAttributes(attributes);

    int marginPixels = DimensionUtils.getSizeInPixels(margin, getContext());
    setLayerType(LAYER_TYPE_SOFTWARE, null);
    internalVelocimeterPainter =
        new InternalVelocimeterPainterImp(insideProgressColor, marginPixels, getContext());
    progressVelocimeterPainter =
        new ProgressVelocimeterPainterImp(externalProgressColor, max, marginPixels, getContext());
    initValueAnimator();

    digitalPainter = new DigitalImp(digitalNumberColor, getContext(),
        DimensionUtils.getSizeInPixels(45, context), (int) DimensionUtils.pixelsToSp(context,44),
        "");
    digitalPainter.setCreditRank(creditRank);
    digitalPainter.setReportTime(reportTime);
  }

  private void initAttributes(TypedArray attributes) {
    insideProgressColor =
        attributes.getColor(R.styleable.VelocimeterView_inside_progress_color, insideProgressColor);
    externalProgressColor = attributes.getColor(R.styleable.VelocimeterView_external_progress_color,
        externalProgressColor);
    digitalNumberColor =
        attributes.getColor(R.styleable.VelocimeterView_digital_number_color, digitalNumberColor);
    max = attributes.getInt(R.styleable.VelocimeterView_max, max);
    creditRank = "信用待检";
    reportTime = "";
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    digitalPainter.draw(canvas);//数字
    internalVelocimeterPainter.draw(canvas);//默认圆环
    if (value>0) {
      progressVelocimeterPainter.draw(canvas);
    }

//    invalidate();
  }

  /**
   * Sets value.
   *
   * @param value the value
   */
  public void setValue(float value) {
    this.value = value;
    if (value <= max && value >= min) {
      animateProgressValue();
    }
  }

  /**
   * Sets value.
   *
   * @param value   the value
   * @param animate the animate
   */
  public void setValue(float value, boolean animate) {
    this.value = value;
    if (value <= max && value >= min) {
      if (!animate) {
        updateValueProgress(value);
        updateValueNeedle(value);
      } else {
        animateProgressValue();
      }

    }
  }

  private void initValueAnimator() {
    progressValueAnimator = new ValueAnimator();
    progressValueAnimator.setInterpolator(interpolator);
    progressValueAnimator.addUpdateListener(new ProgressAnimatorListenerImp());
    nidleValueAnimator = new ValueAnimator();
    nidleValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    nidleValueAnimator.addUpdateListener(new NeedleAnimatorListenerImp());
  }


  /**
   * Animate progress value.
   */
  public void animateProgressValue() {
    if (progressValueAnimator != null) {
//      progressValueAnimator.setFloatValues(progressLastValue, value);//保持上次的进度，从上次的进度开始走
      progressValueAnimator.setFloatValues(0, value);
      progressValueAnimator.setDuration(duration + progressDelay);
      progressValueAnimator.start();
//      nidleValueAnimator.setFloatValues(nidleLastValue, value);
      nidleValueAnimator.setFloatValues(0, value);
      nidleValueAnimator.setDuration(duration);
      nidleValueAnimator.start();
    }
  }


  /**
   * Sets progress.
   *
   * @param interpolator the interpolator
   */
  public void setProgress(Interpolator interpolator) {
    this.interpolator = interpolator;
    if (progressValueAnimator != null) {
      progressValueAnimator.setInterpolator(interpolator);
    }
  }

  /**
   * Sets credit rank.
   *
   * @param creditRank the credit rank
   */
  public void setCreditRank(String creditRank) {
    this.creditRank = creditRank;
    digitalPainter.setCreditRank(this.creditRank);
  }

  /**
   * Sets report time.
   *
   * @param reportTime the report time
   */
  public void setReportTime(String reportTime) {
    this.reportTime = reportTime;
    digitalPainter.setReportTime(this.reportTime);
  }

  /**
   * Gets max.
   *
   * @return the max
   */
  public float getMax() {
    return max;
  }

  /**
   * Sets max.
   *
   * @param max the max
   */
  public void setMax(int max) {
    this.max = max;
  }

  private void updateValueProgress(float value) {
    progressVelocimeterPainter.setValue(value);
  }

  private void updateValueNeedle(float value) {
    digitalPainter.setValue(value);
  }

  private class ProgressAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
    @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
      Float value = (Float) valueAnimator.getAnimatedValue();
      updateValueProgress(value);
      progressLastValue = value;
//      Toast.makeText(getContext(),"动画特效",Toast.LENGTH_SHORT);
      postInvalidate();
    }
  }

  private class NeedleAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
    @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
      Float value = (Float) valueAnimator.getAnimatedValue();
      updateValueNeedle(value);
      nidleLastValue = value;
      postInvalidate();
    }
  }
}
