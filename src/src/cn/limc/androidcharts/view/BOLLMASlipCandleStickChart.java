/*
 * BOLLMASlipCandleStickChart.java
 * Android-Charts
 *
 * Created by limc on 2014.
 *
 * Copyright 2011 limc.cn All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package cn.limc.androidcharts.view;

import java.util.List;

import cn.limc.androidcharts.entity.DateValueEntity;
import cn.limc.androidcharts.entity.LineEntity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;

/** 
 * <p>en</p>
 * <p>jp</p>
 * <p>cn</p>
 *
 * @author limc 
 * @version v1.0 2014/01/23 16:27:58 
 *  
 */
public class BOLLMASlipCandleStickChart extends MASlipCandleStickChart {

	private List<LineEntity<DateValueEntity>> bandData;
	
	/** 
	 * <p>Constructor of BOLLMASlipCandleStickChart</p>
	 * <p>BOLLMASlipCandleStickChart类对象的构造函数</p>
	 * <p>BOLLMASlipCandleStickChartのコンストラクター</p>
	 *
	 * @param context
	 * @param attrs
	 * @param defStyle 
	 */
	public BOLLMASlipCandleStickChart(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/** 
	 * <p>Constructor of BOLLMASlipCandleStickChart</p>
	 * <p>BOLLMASlipCandleStickChart类对象的构造函数</p>
	 * <p>BOLLMASlipCandleStickChartのコンストラクター</p>
	 *
	 * @param context
	 * @param attrs 
	 */
	public BOLLMASlipCandleStickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/** 
	 * <p>Constructor of BOLLMASlipCandleStickChart</p>
	 * <p>BOLLMASlipCandleStickChart类对象的构造函数</p>
	 * <p>BOLLMASlipCandleStickChartのコンストラクター</p>
	 *
	 * @param context 
	 */
	public BOLLMASlipCandleStickChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * <p>Called when is going to draw this chart<p> <p>チャートを書く前、メソッドを呼ぶ<p>
	 * <p>绘制图表时调用<p>
	 * 
	 * @param canvas
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// draw lines
		if (null != bandData && bandData.size() >= 2) {
			drawAreas(canvas);
			drawBandBorder(canvas);
		}
	}

	/**
	 * <p>
	 * draw lines
	 * </p>
	 * <p>
	 * ラインを書く
	 * </p>
	 * <p>
	 * 绘制线条
	 * </p>
	 * 
	 * @param canvas
	 */
	protected void drawAreas(Canvas canvas) {
		// distance between two points
		float lineLength = ((super.getWidth() - super.getAxisMarginLeft()) / displayNumber) - 1;
		// start point‘s X
		float startX;
		float lastY = 0;
		float lastX = 0;

		LineEntity<DateValueEntity> line1 = (LineEntity<DateValueEntity>) bandData
				.get(0);
		LineEntity<DateValueEntity> line2 = (LineEntity<DateValueEntity>) bandData
				.get(1);

		if (line1.isDisplay() && line2.isDisplay()) {
			Paint mPaint = new Paint();
			mPaint.setColor(line1.getLineColor());
			mPaint.setAlpha(70);
			mPaint.setAntiAlias(true);
			List<DateValueEntity> line1Data = line1.getLineData();
			List<DateValueEntity> line2Data = line2.getLineData();
			// set start point’s X
			startX = super.getAxisMarginLeft() + lineLength / 2f;
			Path areaPath = new Path();
			if (line1Data != null && line2Data != null) {
				for (int j = displayFrom; j < displayFrom + displayNumber; j++) {
					float value1 = line1Data.get(j).getValue();
					float value2 = line2Data.get(j).getValue();

					// calculate Y
					float valueY1 = (float) ((1f - (value1 - this.getMinValue())
							/ (this.getMaxValue() - this.getMinValue())) * (super
							.getHeight() - super.getAxisMarginBottom()));
					float valueY2 = (float) ((1f - (value2 - this.getMinValue())
							/ (this.getMaxValue() - this.getMinValue())) * (super
							.getHeight() - super.getAxisMarginBottom()));

					// 绘制线条路径
					if (j == displayFrom) {
						areaPath.moveTo(startX, valueY1);
						areaPath.lineTo(startX, valueY2);
						areaPath.moveTo(startX, valueY1);
					} else {
						areaPath.lineTo(startX, valueY1);
						areaPath.lineTo(startX, valueY2);
						areaPath.lineTo(lastX, lastY);
						
						areaPath.close();
						areaPath.moveTo(startX, valueY1);
					}

					lastX = startX;
					lastY = valueY2;
					startX = startX + 1 + lineLength;
				}
				areaPath.close();
				canvas.drawPath(areaPath, mPaint);
			}
		}
	}
	
	/**
	 * <p>
	 * draw lines
	 * </p>
	 * <p>
	 * ラインを書く
	 * </p>
	 * <p>
	 * 绘制线条
	 * </p>
	 * 
	 * @param canvas
	 */
	protected void drawBandBorder(Canvas canvas) {
		// distance between two points
		float lineLength = ((super.getWidth() - super.getAxisMarginLeft()) / displayNumber) - 1;
		// start point‘s X
		float startX;

		// draw lines
		for (int i = 0; i < bandData.size(); i++) {
			LineEntity<DateValueEntity> line = (LineEntity<DateValueEntity>) bandData.get(i);
			if (line.isDisplay()) {
				Paint mPaint = new Paint();
				mPaint.setColor(line.getLineColor());
				mPaint.setAntiAlias(true);
				List<DateValueEntity> lineData = line.getLineData();
				// set start point’s X
				startX = super.getAxisMarginLeft() + lineLength / 2f;
				// start point
				PointF ptFirst = null;
				if (lineData != null) {
					for (int j = displayFrom; j < displayFrom + displayNumber; j++) {
						float value = lineData.get(j).getValue();
						// calculate Y
						float valueY = (float) ((1f - (value - this
								.getMinValue())
								/ (this.getMaxValue() - this.getMinValue())) * (super
								.getHeight() - super.getAxisMarginBottom()));

						// if is not last point connect to previous point
						if (j > displayFrom) {
							canvas.drawLine(ptFirst.x, ptFirst.y, startX,
									valueY, mPaint);
						}
						// reset
						ptFirst = new PointF(startX, valueY);
						startX = startX + 1 + lineLength;
					}
				}
			}
		}
	}

	/**
	 * @return the bandData
	 */
	public List<LineEntity<DateValueEntity>> getBandData() {
		return bandData;
	}

	/**
	 * @param bandData the bandData to set
	 */
	public void setBandData(List<LineEntity<DateValueEntity>> bandData) {
		this.bandData = bandData;
	}

}
