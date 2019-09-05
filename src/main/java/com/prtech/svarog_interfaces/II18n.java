package com.prtech.svarog_interfaces;

public interface II18n {
	/**
	 * The method getText returns a text representation for a label code in the
	 * default configured locale
	 * 
	 * @param labelCode
	 *            the label code for which i18n will return a localised string
	 * @return String representation of the label
	 */
	public String getText(String labelCode); 
}
