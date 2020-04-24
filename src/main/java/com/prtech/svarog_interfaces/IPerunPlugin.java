package com.prtech.svarog_interfaces;

import java.util.HashMap;

import com.google.gson.JsonObject;

public interface IPerunPlugin {

	String getName();

	String getHttpContextPath();

	String getJsPluginUrl();

	String getIconPath();

	String getLabelCode();

	int getSortOrder();

	JsonObject getMenu();

	JsonObject getContextMenu(HashMap<String, String> contextMap);
}
