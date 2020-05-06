package com.prtech.svarog_interfaces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This class describes the process for designing an automated menu lists per
 * bundle/module It may be inherited/used in any bundle-plugin in order to
 * provide the automated list
 * 
 * @author zpetr
 *
 */
public abstract class MenuGenerator {

	static final Logger log4j = LogManager.getLogger(MenuGenerator.class.getName());

	// GSON instanse
	Gson gson;
	// JsonObject description
	JsonObject initialJsonObject;
	// the first key in the json array, which keeps information about the module
	// title; for example for OTSC module the moduleCode is
	// "perun.otsc.module_menu.navigation"
	String moduleCode = "";
	// the first key in the value of the first key, which keeps the information
	// about the menu title; for example for the main menu in the OTSC module,
	// the menuCode is "otsc.module_menu.firstModuleMenuItems"
	String menuCode = "";
	// ISvCore instance
	ISvCore svr;
	// Final result
	JsonArray result;

	public JsonArray getResult() {
		return result;
	}

	public void setResult(JsonArray result) {
		this.result = result;
	}

	public MenuGenerator(JsonObject initialJsonObject, String moduleCode, String menuCode, ISvCore svr) {
		super();
		// First construct the required menu list object
		this.gson = new Gson();
		this.initialJsonObject = initialJsonObject;
		this.moduleCode = moduleCode;
		this.menuCode = menuCode;
		this.svr = svr;
		// Then calculate the menu list items
		this.result = generateMenuItems();
	}

	/**
	 * Main method in order to get the final results with menu items for
	 * specific moduleCode and menuCode
	 * 
	 * It does not have any specific params, because it use the one defined in
	 * the class and sent through the constructor
	 * 
	 * @author zpetr
	 *
	 */
	JsonArray generateMenuItems() {
		JsonArray result = null;
		if (this.initialJsonObject == null || this.initialJsonObject.get(moduleCode) == null) {
			return null;
		}
		try {
			JsonObject getObject = (JsonObject) this.initialJsonObject.get("perun.otsc.module_menu.navigation");
			result = (JsonArray) getObject.get(this.menuCode);
			JsonObject tempObj;
			for (int i = 0; i < result.size(); i++) {
				tempObj = (JsonObject) result.get(i);
				if (!checkIfMenuItemIsPermitable(tempObj, svr) || !checkIfMenuItemHasProperStructure(tempObj)) {
					result.remove(i);
				} else {
					parseSubMenuItems(tempObj, svr);
				}
			}
		} catch (Exception e) {
			log4j.error(e);
		}
		return result;
	}

	/**
	 * Boolean check to estimate if the menu item is accessible for the current
	 * user
	 * 
	 * @param menuItem
	 *            - the menu item for which we are checking the permission for
	 *            the logged in user
	 * @param svr
	 *            -SvCore instance in order to get the user permission list
	 *            through the session
	 * @author zpetr
	 *
	 */
	private Boolean checkIfMenuItemIsPermitable(JsonObject menuItem, ISvCore svr) {
		Boolean result = true;
		if (menuItem.has("permissionCode") && menuItem.get("permissionCode") != null) {
			if (!svr.hasPermission(menuItem.get("permissionCode").toString())) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Iterative method in order to do additional checks of the subMenuItems
	 * 
	 * @param menuItem
	 *            - the menu item for which we want to validate the sub menu
	 *            items
	 * @param svr
	 *            -SvCore instance in order to get the user permission list
	 *            through the session
	 * @author zpetr
	 *
	 */
	private void parseSubMenuItems(JsonObject menuItem, ISvCore svr) {
		if (menuItem.has("sub-menu") && menuItem.get("sub-menu") != null) {
			JsonArray subMeniItems = (JsonArray) menuItem.get("sub-menu");
			JsonObject tempObj;
			for (int i = 0; i < subMeniItems.size(); i++) {
				tempObj = (JsonObject) subMeniItems.get(i);
				if (!checkIfMenuItemIsPermitable(tempObj, svr) || !checkIfMenuItemHasProperStructure(tempObj)) {
					subMeniItems.remove(i);
				}
			}
		}
	}

	/**
	 * Boolean check if the menu item satisfies the structure defined with
	 * convention\ It can be changed according user/project needs, but it can
	 * affect data lose for previous versions, so a proper consolidation is
	 * needed before changing it
	 * 
	 * @param menuItem
	 *            - the menu item which is the subject of validation
	 * 
	 * @author zpetr
	 *
	 */
	Boolean checkIfMenuItemHasProperStructure(JsonObject menuItem) {
		Boolean result = true;
		if (!menuItem.has("labelCode") || !menuItem.has("id") || !menuItem.has("menuItemLevel")
				|| !menuItem.has("order") || !menuItem.has("permissionCode") || !menuItem.has("contextMenuLabelCode")) {
			result = false;
		}
		return result;
	}

}
