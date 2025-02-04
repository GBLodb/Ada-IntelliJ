package com.adacore.adaintellij.misc;

import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

/**
 * Basic list cell renderer adding small borders.
 */
public class BasicListCellRenderer extends DefaultListCellRenderer {

	/**
	 * Returns the component to be rendered, representing the cell
	 * in the given list at the given index.
	 *
	 * @param list The list containing the cell.
	 * @param value The value representing by the cell.
	 * @param index The index of the cell in the list.
	 * @param isSelected Whether the cell is selected.
	 * @param cellHasFocus Whether the cell has focus.
	 * @return The cell component to be rendered.
	 */
	@Override
	public Component getListCellRendererComponent(
		JList<?> list,
		Object   value,
		int      index,
		boolean  isSelected,
		boolean  cellHasFocus
	) {

		JLabel label = (JLabel)super.getListCellRendererComponent(
			list, value, index, isSelected, cellHasFocus);

		label.setBorder(JBUI.Borders.empty(2));

		return label;

	}

}
