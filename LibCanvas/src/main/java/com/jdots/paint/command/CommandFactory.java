package com.jdots.paint.command;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import com.jdots.paint.command.implementation.FlipCommand.FlipDirection;
import com.jdots.paint.command.implementation.RotateCommand.RotateDirection;
import com.jdots.paint.tools.drawable.ShapeDrawable;

import java.util.List;

public interface CommandFactory {

	Command createInitCommand(int width, int height);

	Command createInitCommand(Bitmap bitmap);

	Command createInitCommand(List<Bitmap> bitmapList);

	Command createResetCommand();

	Command createAddLayerCommand();

	Command createSelectLayerCommand(int position);

	Command createRemoveLayerCommand(int index);

	Command createReorderLayersCommand(int position, int swapWith);

	Command createMergeLayersCommand(int position, int mergeWith);

	Command createRotateCommand(RotateDirection rotateDirection);

	Command createFlipCommand(FlipDirection flipDirection);

	Command createCropCommand(int resizeCoordinateXLeft, int resizeCoordinateYTop,
			int resizeCoordinateXRight, int resizeCoordinateYBottom,
			int maximumBitmapResolution);

	Command createPointCommand(Paint paint, PointF coordinate);

	Command createFillCommand(int x, int y, Paint paint, float colorTolerance);

	Command createGeometricFillCommand(ShapeDrawable shapeDrawable, Point position, RectF box, float boxRotation, Paint paint);

	Command createPathCommand(Paint paint, Path path);

	Command createTextToolCommand(String[] multilineText, Paint textPaint, int boxOffset, float boxWidth, float boxHeight, PointF toolPosition, float boxRotation);

	Command createResizeCommand(int newWidth, int newHeight);

	Command createStampCommand(Bitmap bitmap, PointF toolPosition, float boxWidth, float boxHeight, float boxRotation);

	Command createSprayCommand(float[] sprayedPoints, Paint paint);

	Command createCutCommand(PointF toolPosition, float boxWidth, float boxHeight, float boxRotation);
}
