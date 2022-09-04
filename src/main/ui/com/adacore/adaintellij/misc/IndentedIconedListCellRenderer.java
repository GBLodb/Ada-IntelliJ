package com.adacore.adaintellij.misc;

import com.adacore.adaintellij.UIUtils;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * List cell renderer adding a configurable icon and
 * discrete indentation.
 */
public class IndentedIconedListCellRenderer extends BasicListCellRenderer {

	/**
	 * Cell content indentation.
	 */
	private final int indentation;

	/**
	 * Cell icon.
	 */
	private final Icon icon;

	/**
	 * Constructs a new IndentedIconedListCellRenderer given
	 * an indentation level and an icon.
	 *
	 * @param indentation The indentation level to set for
	 *                    list cells.
	 * @param icon The icon to attach to list cells.
	 */
	public IndentedIconedListCellRenderer(
		         int  indentation,
		@NotNull Icon icon
	) {
		this.indentation = indentation;
		this.icon        = icon;
	}

	/**
	 * @see BasicListCellRenderer#getListCellRendererComponent(JList, Object, int, boolean, boolean)
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

		UIUtils.addIconWithGap(label, icon);
		/**
		 * Cell content indentation factor.
		 */
		int INDENTATION_FACTOR = 20;
		label.setBorder(JBUI.Borders.empty(2, 2 + indentation * INDENTATION_FACTOR, 2, 2));

		return label;

	}

}
