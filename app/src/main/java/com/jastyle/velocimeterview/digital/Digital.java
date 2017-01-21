package com.jastyle.velocimeterview.digital;


import com.jastyle.velocimeterview.painter.Painter;

/**
 * @author Adrián García Lomas
 */
public interface Digital extends Painter {

  void setValue(float value);
  void setCreditRank(String creditRanks);
  void setReportTime(String reportTime);
}
