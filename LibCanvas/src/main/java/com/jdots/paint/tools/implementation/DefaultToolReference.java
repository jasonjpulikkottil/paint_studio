package com.jdots.paint.tools.implementation;

import com.jdots.paint.tools.Tool;
import com.jdots.paint.tools.ToolReference;

public class DefaultToolReference implements ToolReference {
	private Tool tool;

	@Override
	public Tool get() {
		return tool;
	}

	@Override
	public void set(Tool tool) {
		this.tool = tool;
	}
}
