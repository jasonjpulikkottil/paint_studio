package com.jdots.paint.test.listener;

import android.view.View;

import com.jdots.paint.listener.BottomBarScrollListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class BottomBarScrollListenerTest {

	@Mock
	private View previous;

	@Mock
	private View next;

	private BottomBarScrollListener bottomBarScrollListener;

	@Before
	public void setUp() {
		bottomBarScrollListener = new BottomBarScrollListener(previous, next);
	}

	@Test
	public void testOnScrollMostRight() {
		bottomBarScrollListener.onScrollMostRight();

		verify(next).setVisibility(View.GONE);
		verifyNoMoreInteractions(previous);
		verifyNoMoreInteractions(next);
	}

	@Test
	public void testOnScrollMostLeft() {
		bottomBarScrollListener.onScrollMostLeft();

		verify(previous).setVisibility(View.GONE);
		verifyNoMoreInteractions(previous);
		verifyNoMoreInteractions(next);
	}

	@Test
	public void testOnScrollFromMostLeft() {
		bottomBarScrollListener.onScrollFromMostLeft();

		verify(previous).setVisibility(View.VISIBLE);
		verifyNoMoreInteractions(previous);
		verifyNoMoreInteractions(next);
	}

	@Test
	public void testOnScrollFromMostRight() {
		bottomBarScrollListener.onScrollFromMostRight();

		verify(next).setVisibility(View.VISIBLE);
		verifyNoMoreInteractions(previous);
		verifyNoMoreInteractions(next);
	}
}
