/*

 */package com.jdots.paint.test.espresso.tools;

import android.graphics.Color;
import android.graphics.Paint;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.test.espresso.util.BitmapLocationProvider;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.tools.ToolReference;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.drawable.DrawableShape;
import com.jdots.paint.tools.drawable.DrawableStyle;
import com.jdots.paint.tools.implementation.BaseToolWithRectangleShape;
import com.jdots.paint.tools.implementation.ShapeTool;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.OffsetLocationProvider.withOffset;
import static com.jdots.paint.test.espresso.util.UiInteractions.touchCenterLeft;
import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static com.jdots.paint.test.espresso.util.wrappers.ShapeToolOptionsViewInteraction.onShapeToolOptionsView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolPropertiesInteraction.onToolProperties;
import static com.jdots.paint.test.espresso.util.wrappers.TopBarViewInteraction.onTopBarView;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ShapeToolIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);
	private ToolReference toolReference;
	private MainActivity mainActivity;

	@Before
	public void setUp() {
		mainActivity = launchActivityRule.getActivity();
		toolReference = mainActivity.toolReference;

		onToolBarView()
				.performSelectTool(ToolType.SHAPE);
	}

	private Paint getCurrentToolBitmapPaint() {
		return ((ShapeTool) toolReference.get()).getShapeBitmapPaint();
	}

	private Paint getToolPaint() {
		return mainActivity.toolPaint.getPaint();
	}

	@Test
	public void testEllipseIsDrawnOnBitmap() {
		onShapeToolOptionsView()
				.performSelectShape(DrawableShape.OVAL);

		BaseToolWithRectangleShape ellipseTool = (BaseToolWithRectangleShape) toolReference.get();
		float rectHeight = ellipseTool.boxHeight;

		onToolBarView()
				.performCloseToolOptionsView();

		onTopBarView()
				.performClickCheckmark();

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE)
				.checkPixelColor(Color.BLACK, withOffset(BitmapLocationProvider.MIDDLE, (int) (rectHeight / 2.5f), 0))
				.checkPixelColor(Color.TRANSPARENT, withOffset(BitmapLocationProvider.MIDDLE, (int) (rectHeight / 2.5f), (int) (rectHeight / 2.5f)));
	}

	@Test
	public void testUndoRedo() {
		onToolBarView()
				.performCloseToolOptionsView();

		onTopBarView()
				.performClickCheckmark();

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);

		onTopBarView()
				.performUndo();

		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.MIDDLE);

		onTopBarView()
				.performRedo();

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);
	}

	@Test
	public void testFilledRectChangesColor() {
		onToolBarView()
				.performCloseToolOptionsView();

		onToolProperties()
				.setColorResource(R.color.paint_studio_color_picker_brown1);

		onTopBarView()
				.performClickCheckmark();

		onDrawingSurfaceView()
				.checkPixelColorResource(R.color.paint_studio_color_picker_brown1, BitmapLocationProvider.MIDDLE);
	}

	@Test
	public void testDrawWithHeartShape() {
		onShapeToolOptionsView()
				.performSelectShape(DrawableShape.HEART);

		onToolBarView()
				.performCloseToolOptionsView();

		onTopBarView()
				.performClickCheckmark();

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);
	}

	@Test
	public void testAntiAliasingIsOffIfShapeOutlineWidthIsOne() {
		onToolBarView()
				.performSelectTool(ToolType.SHAPE);
		onShapeToolOptionsView()
				.performSelectShapeDrawType(DrawableStyle.STROKE);
		onShapeToolOptionsView()
				.performSetOutlineWidth(touchCenterLeft());

		drawShape();

		Paint bitmapPaint = getCurrentToolBitmapPaint();
		Paint toolPaint = getToolPaint();

		assertFalse("BITMAP_PAINT antialiasing should be off", bitmapPaint.isAntiAlias());
		assertTrue("TOOL_PAINT antialiasing should be on", toolPaint.isAntiAlias());
	}

	@Test
	public void testDoNotUseRegularToolPaintInShapeTool() {
		onToolBarView()
				.performSelectTool(ToolType.SHAPE);
		onShapeToolOptionsView()
				.performSelectShapeDrawType(DrawableStyle.FILL);

		drawShape();

		Paint bitmapPaint = getCurrentToolBitmapPaint();
		Paint toolPaint = getToolPaint();

		assertNotEquals("bitmapPaint and toolPaint should differ", bitmapPaint, toolPaint);
	}

	@Test
	public void testShapeWithOutlineAlsoWorksWithTransparentColor() {
		onToolBarView()
				.performSelectTool(ToolType.SHAPE);
		onShapeToolOptionsView()
				.performSelectShape(DrawableShape.RECTANGLE);
		onShapeToolOptionsView()
				.performSelectShapeDrawType(DrawableStyle.FILL);
		onToolProperties()
				.setColor(Color.BLACK);
		drawShape();
		onToolBarView()
				.performClickSelectedToolButton();

		onShapeToolOptionsView()
				.performSelectShape(DrawableShape.OVAL);
		onShapeToolOptionsView()
				.performSelectShapeDrawType(DrawableStyle.STROKE);
		onToolProperties()
				.setColor(Color.TRANSPARENT);
		drawShape();
		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, DrawingSurfaceLocationProvider.TOOL_POSITION);
		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, DrawingSurfaceLocationProvider.TOP_MIDDLE);
	}

	@Test
	public void testShapeToolBoxGetsPlacedCorrectWhenZoomedIn() {
		onToolBarView()
				.performSelectTool(ToolType.BRUSH);

		mainActivity.perspective.setSurfaceTranslationY(200);
		mainActivity.perspective.setSurfaceTranslationX(50);
		mainActivity.perspective.setScale(2.0f);
		mainActivity.refreshDrawingSurface();

		onToolBarView()
				.performSelectTool(ToolType.SHAPE);
		onShapeToolOptionsView()
				.performSelectShape(DrawableShape.RECTANGLE);
		onShapeToolOptionsView()
				.performSelectShapeDrawType(DrawableStyle.FILL);
		onTopBarView()
				.performClickCheckmark();

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, mainActivity.perspective.surfaceCenterX - mainActivity.perspective.surfaceTranslationX,
						mainActivity.perspective.surfaceCenterY - mainActivity.perspective.surfaceTranslationY);
	}

	public void drawShape() {
		onToolBarView()
				.performCloseToolOptionsView();
		onTopBarView()
				.performClickCheckmark();
	}
}
