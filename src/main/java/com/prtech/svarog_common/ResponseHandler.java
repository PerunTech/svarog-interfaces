package com.prtech.svarog_common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prtech.svarog.SvException;
import com.prtech.svarog_common.Jsonable;
import com.prtech.svarog_interfaces.II18n;

public class ResponseHandler extends Jsonable {
	/**
	 * Enum type for the type of response
	 * @author ristepejov
	 *
	 */
	public enum MessageType {
		SUCCESS, ERROR, WARNING, INFO, EXCEPTION
	}

	static II18n i18n = null;
	static String userLocale = null;
	
	private MessageType rType;
	private String rTitle;
	private String rMessage;
	private String sData;
	private JsonElement jData;
	private JsonObject responseObject;

	/**
	 * method to create response handler in case of SvException but when is
	 * returned as JsonObject , we read the error_message and error_id and
	 * create error handler, we can also overwrite the error message if we
	 * specify svarog.label_code as parameter that will be DB translated
	 * 
	 * @param e
	 *            SvException
	 * @param str
	 *            String if we want to overwrite the message we put label_code,
	 *            else we can use null or ""
	 * @return ResponseHandler "EXCEPTION" with formated title and message and
	 *         no data
	 */
	public static ResponseHandler responseHandlerByException(JsonObject afterJson, String titleOverwrite) {
		ResponseHandler jrh = new ResponseHandler();
		String errCOde = "";

		if (afterJson.has("ERROR_ID"))
			errCOde = afterJson.get("ERROR_ID").getAsString();
		String errTitle = "";
		if (titleOverwrite != null)
			errTitle = titleOverwrite;
		if ((titleOverwrite == null || "".equals(titleOverwrite)) && afterJson.has("Error_Message"))
			errTitle = afterJson.get("Error_Message").getAsString();
		String customType = "ERROR";
		if ("error.invalid_session".equalsIgnoreCase(errTitle) || "system.under.maintenance".equalsIgnoreCase(errTitle))
			customType = "EXCEPTION";
		jrh.create(customType, 
				(i18n!=null?i18n.getI18nText(userLocale,errTitle):errTitle)
				, errCOde, new JsonObject());
		return jrh;
	}

	/**
	 * method to create response handler in case of SvException, we read the
	 * error_message and error_id and create error handler, we can also
	 * overwrite the error message if we specify svarog.label_code as parameter
	 * that will be DB translated
	 * 
	 * @param e
	 *            SvException
	 * @param str
	 *            String if we want to overwrite the message we put label_code,
	 *            else we can use null or ""
	 * @return ResponseHandler "EXCEPTION" with formated title and message and
	 *         no data
	 */
	public static ResponseHandler responseHandlerByException(SvException e, String titleOverwrite) {
		Gson gs = new Gson();
		String beforeJson = e.getJsonMessage();
		JsonObject afterJson = gs.fromJson(beforeJson, JsonObject.class);
		return responseHandlerByException(afterJson, titleOverwrite);
	}

	/**
	 * method to create response handler in case of SvException, we read the
	 * error_message and error_id and create error handler,
	 * 
	 * @param e
	 *            SvException
	 * @return ResponseHandler "EXCEPTION" with formated title and message and
	 *         no data
	 */
	public static ResponseHandler responseHandlerByException(SvException e) {
		return responseHandlerByException(e, "");
	}

	/**
	 * method to return the values from the response handles as an JsonObject,
	 * this one will just return the responseObject
	 * 
	 * @return JsonObject value that is internally saved in responseObject
	 */
	public JsonObject getAll() {
		return responseObject;
	}

	/**
	 * method to return the values from the response handles as an JsonObject,
	 * this one will generate the response Object from all the values like:
	 * responseType, responseMessage , rsponseTitle , and data that we are
	 * returning back
	 * 
	 * @return JsonObject created in time of calling this method
	 */
	public JsonObject getAllv1() {
		JsonObject rObject = new JsonObject();
		if (rType != null)
			rObject.addProperty("type", rType.toString());
		if (rTitle != null && rTitle != "")
			rObject.addProperty("title", rTitle);
		if (rMessage != null && sData != "")
			rObject.addProperty("message", rMessage);
		if (sData != null && sData != "")
			rObject.addProperty("data", sData);
		if (jData != null)
			rObject.addProperty("data", jData.toString());
		return rObject;
	}

	/**
	 * method to create response handler , this is standard response , with type
	 * of the response, messages, and data if execution was success, data to be
	 * returned is String
	 * 
	 * @param typee
	 *            String type of the response, values are: SUCCESS, ERROR,
	 *            WARNING, INFO, EXCEPTION
	 * @param title
	 *            String short title that will be shown on the response
	 * @param message
	 *            String long message like exception dump
	 * @param data
	 *            String if the response was expected to be String we add that
	 *            data here
	 */
	public void create(String typee, String title, String message, String data) {
		responseObject = createBasicData(typee, title, message);
		if (data != null && data != "") {
			responseObject.addProperty("data", data);
			sData = data;
		}
	}

	public void create(MessageType typee, String title, String message, String data) {
		responseObject = createBasicData(typee, title, message);
		if (data != null && data != "") {
			responseObject.addProperty("data", data);
			sData = data;
		}
	}

	/**
	 * method to create response handler , this is standard response , with type
	 * of the response, messages, and data if execution was success, data to be
	 * returned is JsonObject
	 * 
	 * @param typee
	 *            String type of the response, values are: SUCCESS, ERROR,
	 *            WARNING, INFO, EXCEPTION
	 * @param title
	 *            String short title that will be shown on the response
	 * @param message
	 *            String long message like exception dump
	 * @param data
	 *            JsonElement if the response was expected to be JsonObject we
	 *            add that data here
	 */
	public JsonObject create(String typee, String title, String message, JsonElement data) {
		responseObject = createBasicData(typee, title, message);
		if (data != null) {
			responseObject.add("data", data);
			jData = data;
		}
		return responseObject;
	}

	public JsonObject create(MessageType typee, String title, String message, JsonElement data) {
		responseObject = createBasicData(typee, title, message);
		if (data != null) {
			responseObject.add("data", data);
			jData = data;
		}
		return responseObject;
	}

	/**
	 * method to create response handler basic data that is shared between
	 * String JsonArray and JsonObject
	 * 
	 * @param typee
	 *            String type of the response, values are: SUCCESS, ERROR,
	 *            WARNING, INFO, EXCEPTION
	 * @param title
	 *            String short title that will be shown on the response
	 * @param message
	 *            String long message like exception dump
	 * @return JsonObject
	 */
	private JsonObject createBasicData(String typee, String title, String message) {
		responseObject = new JsonObject();
		if (typee != null && typee != "") {
			responseObject.addProperty("type", typee);
			switch (typee.toUpperCase()) {
			case "SUCCESS":
				rType = MessageType.SUCCESS;
				break;
			case "ERROR":
				rType = MessageType.ERROR;
				break;
			case "WARNING":
				rType = MessageType.WARNING;
				break;
			case "INFO":
				rType = MessageType.INFO;
				break;
			case "EXCEPTION":
				rType = MessageType.EXCEPTION;
				break;
			default:
			}
		}
		if (title != null && title != "") {
			responseObject.addProperty("title", title);
			rTitle = title;
		}
		if (message != null && message != "") {
			responseObject.addProperty("message", message);
			rMessage = message;
		}
		return responseObject;
	}

	private JsonObject createBasicData(MessageType typee, String title, String message) {
		responseObject = new JsonObject();
		if (typee != null) {
			responseObject.addProperty("type", typee.toString());
			rType = typee;
		}
		if (title != null && title != "") {
			responseObject.addProperty("title", title);
			rTitle = title;
		}
		if (message != null && message != "") {
			responseObject.addProperty("message", message);
			rMessage = message;
		}
		return responseObject;
	}

	/**
	 * method to create response handler , this is standard response , with type
	 * of the response, messages, and data if execution was success, data to be
	 * returned is empty String
	 * 
	 * @param typee
	 *            String type of the response, values are: SUCCESS, ERROR,
	 *            WARNING, INFO, EXCEPTION
	 * @param title
	 *            String short title that will be shown on the response
	 * @param message
	 *            String long message like exception dump
	 * @param data
	 *            String if the response was expected to be String we add that
	 *            data here
	 */
	public void create(String typee, String title, String message) {
		create(typee, title, message, "");
	}

	public void create(MessageType typee, String title, String message) {
		create(typee, title, message, "");
	}

	public JsonObject addJsonObjectElementData(JsonElement data) {
		if (data != null) {
			responseObject.add("data", data);
			jData = data;
		}
		return responseObject;
	}
}