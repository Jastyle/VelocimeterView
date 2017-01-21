package com.jastyle.velocimeterview.progress;

import com.jastyle.velocimeterview.painter.Painter;

/**
 * @author Adrián García Lomas
 */
public interface ProgressVelocimeterPainter extends Painter {

  public void setValue(float value);
}
