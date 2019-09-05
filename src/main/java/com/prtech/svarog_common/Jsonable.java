/*******************************************************************************
 * Copyright (c) 2013, 2017 Perun Technologii DOOEL Skopje.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License
 * Version 2.0 or the Svarog License Agreement (the "License");
 * You may not use this file except in compliance with the License. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See LICENSE file in the project root for the specific language governing 
 * permissions and limitations under the License.
 *
 *******************************************************************************/
package com.prtech.svarog_common;

import com.google.gson.JsonObject;
import com.prtech.svarog.SvException;
import com.prtech.svarog.svCONST;

/**
 * Interface for implementing JSON serialisation
 * @author PR01
 *
 */
public abstract class Jsonable {
	
	/**
	 * An instance of MemberJson object which is doing the JSON serialisation
	 */
	JsonIO jsonIO = new JsonIO();
	/**
	 * A getter function for the JsonIO instance. Will be removed in 3.0
	 * 
	 * @return instance of 
	 */
	@Deprecated
	public JsonIO getMembersJson() {
		return jsonIO;
	}

	/**
	 * A getter function for the memebersJson variable. It MUST be named exactly
	 * "getJsonIO" in order for the JSON serialization to work correctly
	 * 
	 * @return instance of JsonIO
	 */
	public JsonIO getJsonIO() {
		return jsonIO;
	}

	/**
	 * Quick access functions for populating the Object from JSON
	 * a wrapper for this.memberJson.setMembersFromJson("", this, obj);
	 * @return TODO
	 */
	public Boolean fromJson(JsonObject obj)
	{
		return this.jsonIO.setMembersFromJson("", this, obj);
	}
	/**
	 * Quick access functions for creating a JsonObject from the implementing class instance
	 * a wrapper for this.memberJson.getMembersToJson("", this);
	 */
	public JsonObject toJson()
	{
		return this.jsonIO.getMembersToJson("", this,null);
	}

	/**
	 * Quick access functions for populating the Object from JSON
	 * a wrapper for this.memberJson.setMembersFromJson("", this, obj);
	 * @return TODO
	 */
	public Boolean fromSimpleJson(JsonObject obj)
	{
		return this.jsonIO.setMembersFromJson("", this, obj,true);
	}
	/**
	 * Quick access functions for creating a JsonObject from the implementing class instance
	 * a wrapper for this.memberJson.getMembersToJson("", this);
	 */
	public JsonObject toSimpleJson()
	{
		return this.jsonIO.getMembersToJson("", this,null,true);
	}
	
	public JsonObject toTabularJson(DbDataArray objTypeDbt, DbDataArray objTypeDbf,
			JsonObject exParams)
	{
		return this.jsonIO.getTabularJson("", this, objTypeDbt, objTypeDbf, exParams, null);

	}

	public JsonObject toTabularJson(DbDataArray objTypeDbt, DbDataArray objTypeDbf,
			JsonObject exParams, ISvCodeList codeList)
	{
		return this.jsonIO.getTabularJson("", this, objTypeDbt, objTypeDbf, exParams, codeList);

	}
	
}
