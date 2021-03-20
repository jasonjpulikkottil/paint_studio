package com.jdots.paint.test.command.implementation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.jdots.paint.command.implementation.PointCommand;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.LayerModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PointCommandTest {
	@Mock
	private Paint paint;

	@Mock
	private PointF point;

	@InjectMocks
	private PointCommand command;

	@Test
	public void testSetUp() {
		verifyZeroInteractions(paint, point);
	}

	@Test
	public void testDrawOnePoint() {
		Canvas canvas = mock(Canvas.class);
		LayerContracts.Model model = new LayerModel();

		point.x = 3;
		point.y = 7;

		command.run(canvas, model);

		verify(canvas).drawPoint(3, 7, paint);
		verifyZeroInteractions(paint);
		verifyZeroInteractions(point);
	}
}
