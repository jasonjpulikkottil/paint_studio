package com.jdots.paint.test.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.implementation.AddLayerCommand;
import com.jdots.paint.command.implementation.CompositeCommand;
import com.jdots.paint.command.implementation.CropCommand;
import com.jdots.paint.command.implementation.DefaultCommandFactory;
import com.jdots.paint.command.implementation.FlipCommand;
import com.jdots.paint.command.implementation.MergeLayersCommand;
import com.jdots.paint.command.implementation.PointCommand;
import com.jdots.paint.command.implementation.RemoveLayerCommand;
import com.jdots.paint.command.implementation.ReorderLayersCommand;
import com.jdots.paint.command.implementation.ResizeCommand;
import com.jdots.paint.command.implementation.RotateCommand;
import com.jdots.paint.command.implementation.SelectLayerCommand;
import org.junit.Before;
import org.junit.Test;

import static com.jdots.paint.command.implementation.FlipCommand.FlipDirection.FLIP_HORIZONTAL;
import static com.jdots.paint.command.implementation.RotateCommand.RotateDirection.ROTATE_LEFT;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class DefaultCommandFactoryTest {

	private DefaultCommandFactory commandFactory;

	@Before
	public void setUp() {
		commandFactory = new DefaultCommandFactory();
	}

	@Test
	public void testCreateInitCommand() {
		Command command = commandFactory.createInitCommand(10, 20);
		assertThat(command, is(instanceOf(CompositeCommand.class)));
	}

	@Test
	public void testCreateInitCommandWithBitmap() {
		Command command = commandFactory.createInitCommand(mock(Bitmap.class));
		assertThat(command, is(instanceOf(CompositeCommand.class)));
	}

	@Test
	public void testCreateResetCommand() {
		Command command = commandFactory.createResetCommand();
		assertThat(command, is(instanceOf(CompositeCommand.class)));
	}

	@Test
	public void testCreateAddLayerCommand() {
		Command command = commandFactory.createAddLayerCommand();
		assertThat(command, is(instanceOf(AddLayerCommand.class)));
	}

	@Test
	public void testCreateSelectLayerCommand() {
		Command command = commandFactory.createSelectLayerCommand(0);
		assertThat(command, is(instanceOf(SelectLayerCommand.class)));
	}

	@Test
	public void testCreateRemoveLayerCommand() {
		Command command = commandFactory.createRemoveLayerCommand(0);
		assertThat(command, is(instanceOf(RemoveLayerCommand.class)));
	}

	@Test
	public void testCreateReorderLayersCommand() {
		Command command = commandFactory.createReorderLayersCommand(0, 1);
		assertThat(command, is(instanceOf(ReorderLayersCommand.class)));
	}

	@Test
	public void testCreateMergeLayersCommand() {
		Command command = commandFactory.createMergeLayersCommand(0, 1);
		assertThat(command, is(instanceOf(MergeLayersCommand.class)));
	}

	@Test
	public void testCreateRotateCommand() {
		Command command = commandFactory.createRotateCommand(ROTATE_LEFT);
		assertThat(command, is(instanceOf(RotateCommand.class)));
	}

	@Test
	public void testCreateFlipCommand() {
		Command command = commandFactory.createFlipCommand(FLIP_HORIZONTAL);
		assertThat(command, is(instanceOf(FlipCommand.class)));
	}

	@Test
	public void testCreateCropCommand() {
		Command command = commandFactory.createCropCommand(0, 0, 1, 1, 2);
		assertThat(command, is(instanceOf(CropCommand.class)));
	}

	@Test
	public void testCreatePointCommand() {
		PointF coordinate = mock(PointF.class);
		Paint paint = mock(Paint.class);

		Command command = commandFactory.createPointCommand(paint, coordinate);
		assertThat(command, is(instanceOf(PointCommand.class)));
	}

	@Test
	public void testCreateResizeCommand() {
		Command command = commandFactory.createResizeCommand(10, 20);
		assertThat(command, is(instanceOf(ResizeCommand.class)));
	}
}
