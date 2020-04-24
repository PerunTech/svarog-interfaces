package com.prtech.svarog_interfaces;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.prtech.svarog_interfaces.ISvCore;
import com.google.gson.JsonObject;

/**
 * This class describes the workflow for designing an automated menu lists per
 * bundle/module It may be inherited/used in any bundle-plugin in order to
 * provide the automated list
 * 
 * @author zpetr
 *
 */
public abstract class MenuGenerator {
	// Json parser
	// Name of the json file, where the menus are described in appropriate
	// notation
	String fileName = "";
	// the first key in the json array, which keeps information about the module
	// title; for example for OTSC module the moduleTitle is
	// "perun.otsc.module_menu.navigation"
	String moduleTitle = "";
	// the first key in the value of the first key, which keeps the information
	// about the menu title; for example for the main menu in the OTSC module,
	// the menuTitle is "otsc.module_menu.firstModuleMenuItems"
	String menuTitle = "";
	// ISvCore instance
	ISvCore svr;
	// Final result
	JsonArray result;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getModuleTitle() {
		return moduleTitle;
	}

	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public ISvCore getSvr() {
		return svr;
	}

	public void setSvr(ISvCore svr) {
		this.svr = svr;
	}

	public JsonArray getResult() {
		return result;
	}

	public void setResult(JsonArray result) {
		this.result = result;
	}

	public MenuGenerator(String fileName, String moduleTitle, String menuTitle, ISvCore svr) {
		super();
		// First construct the required menu list object
		
		this.fileName = fileName;
		this.moduleTitle = moduleTitle;
		this.menuTitle = menuTitle;
		this.svr = svr;
		// Then calculate the menu list items
		this.result = generateMenuItems();
		// Now they can be accessed via the appropriate get method for the
		// result var
	}

	JsonArray generateMenuItems() {
		JsonObject initalJson;
		JsonArray result = null;
		try {
			
			JsonObject initialModuleList = null;
			JsonArray intialMenuList = (JsonArray) initialModuleList.get(menuTitle);
			result = intialMenuList;
			JsonObject tempObj;
			for (int i = 0; i < result.size(); i++) {
				tempObj = (JsonObject) result.get(i);
				if (!checkIfMenuItemIsPermitable(tempObj, svr)) {
					result.remove(i);
				} else {
					parseSubMenuItems(tempObj, svr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private Boolean checkIfMenuItemIsPermitable(JsonObject menuItem, ISvCore svr) {
		Boolean result = true;
		if (menuItem.has("permissionCode") && menuItem.get("permissionCode") != null) {
			if (!svr.hasPermission(menuItem.get("permissionCode").toString())) {
				result = false;
			}
		}
		return result;
	}

	private void parseSubMenuItems(JsonObject menuItem, ISvCore svr) {
		if (menuItem.has("sub-menu") && menuItem.get("sub-menu") != null) {
			JsonArray subMeniItems = (JsonArray) menuItem.get("sub-menu");
			for (int i = 0; i < subMeniItems.size(); i++) {
				JsonObject tempObj = (JsonObject) subMeniItems.get(i);
				if (!checkIfMenuItemIsPermitable(tempObj, svr)) {
					subMeniItems.remove(i);
				}
			}
		}
	}
}
