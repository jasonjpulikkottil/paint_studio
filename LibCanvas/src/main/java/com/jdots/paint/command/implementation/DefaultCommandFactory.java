package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandFactory;
import com.jdots.paint.common.CommonFactory;
import com.jdots.paint.command.implementation.FlipCommand.FlipDirection;
import com.jdots.paint.tools.drawable.ShapeDrawable;
import com.jdots.paint.tools.helper.Conversion;
import com.jdots.paint.tools.helper.JavaFillAlgorithmFactory;

import java.util.List;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class DefaultCommandFactory implements CommandFactory {

	private CommonFactory commonFactory = new CommonFactory();

	@Override
	public Command createInitCommand(int width, int height) {
		CompositeCommand command = new CompositeCommand();
		command.addCommand(new SetDimensionCommand(width, height));
		command.addCommand(new AddLayerCommand(commonFactory));
		return command;
	}

	@Override
	public Command createInitCommand(Bitmap bitmap) {
		CompositeCommand command = new CompositeCommand();
		command.addCommand(new SetDimensionCommand(bitmap.getWidth(), bitmap.getHeight()));
		command.addCommand(new LoadCommand(bitmap.copy(ARGB_8888, false)));
		return command;
	}

	@Override
	public Command createInitCommand(List<Bitmap> bitmapList) {
		CompositeCommand command = new CompositeCommand();
		command.addCommand(new SetDimensionCommand(bitmapList.get(0).getWidth(), bitmapList.get(0).getHeight()));
		command.addCommand(new LoadBitmapListCommand(bitmapList));
		return command;
	}

	@Override
	public Command createResetCommand() {
		CompositeCommand command = new CompositeCommand();
		command.addCommand(new ResetCommand());
		command.addCommand(new AddLayerCommand(commonFactory));
		return command;
	}

	@Override
	public Command createAddLayerCommand() {
		return new AddLayerCommand(commonFactory);
	}

	@Override
	public Command createSelectLayerCommand(int position) {
		return new SelectLayerCommand(position);
	}

	@Override
	public Command createRemoveLayerCommand(int index) {
		return new RemoveLayerCommand(index);
	}

	@Override
	public Command createReorderLayersCommand(int position, int swapWith) {
		return new ReorderLayersCommand(position, swapWith);
	}

	@Override
	public Command createMergeLayersCommand(int position, int mergeWith) {
		return new MergeLayersCommand(position, mergeWith);
	}

	@Override
	public Command createRotateCommand(RotateCommand.RotateDirection rotateDirection) {
		return new RotateCommand(rotateDirection);
	}

	@Override
	public Command createFlipCommand(FlipDirection flipDirection) {
		return new FlipCommand(flipDirection);
	}

	@Override
	public Command createCropCommand(int resizeCoordinateXLeft, int resizeCoordinateYTop, int resizeCoordinateXRight, int resizeCoordinateYBottom, int maximumBitmapResolution) {
		return new CropCommand(resizeCoordinateXLeft, resizeCoordinateYTop, resizeCoordinateXRight, resizeCoordinateYBottom, maximumBitmapResolution);
	}

	@Override
	public Command createPointCommand(Paint paint, PointF coordinate) {
		return new PointCommand(
				commonFactory.createPaint(paint),
				commonFactory.createPointF(coordinate)
		);
	}

	@Override
	public Command createFillCommand(int x, int y, Paint paint, float colorTolerance) {
		return new FillCommand(new JavaFillAlgorithmFactory(),
				commonFactory.createPoint(x, y),
				commonFactory.createPaint(paint), colorTolerance);
	}

	@Override
	public Command createGeometricFillCommand(ShapeDrawable shapeDrawable, Point position, RectF box, float boxRotation, Paint paint) {
		RectF destRectF = commonFactory.createRectF(box);
		return new GeometricFillCommand(shapeDrawable, position.x, position.y, destRectF, boxRotation, commonFactory.createPaint(paint));
	}

	@Override
	public Command createPathCommand(Paint paint, Path path) {
		return new PathCommand(commonFactory.createPaint(paint), commonFactory.createPath(path));
	}

	@Override
	public Command createTextToolCommand(String[] multilineText, Paint textPaint, int boxOffset, float boxWidth, float boxHeight, PointF toolPosition, float boxRotation) {
		return new TextToolCommand(multilineText, commonFactory.createPaint(textPaint),
				boxOffset, boxWidth, boxHeight, commonFactory.createPointF(toolPosition),
				boxRotation);
	}

	@Override
	public Command createResizeCommand(int newWidth, int newHeight) {
		return new ResizeCommand(newWidth, newHeight);
	}

	@Override
	public Command createStampCommand(Bitmap bitmap, PointF toolPosition, float width, float height, float rotation) {
		return new StampCommand(bitmap, Conversion.toPoint(toolPosition), width, height, rotation);
	}

	@Override
	public Command createSprayCommand(float[] sprayedPoints, Paint paint) {
		return new SprayCommand(sprayedPoints, paint);
	}

	@Override
	public Command createCutCommand(PointF toolPosition, float boxWidth, float boxHeight, float boxRotation) {
		return new CutCommand(Conversion.toPoint(toolPosition), boxWidth, boxHeight, boxRotation);
	}
}
