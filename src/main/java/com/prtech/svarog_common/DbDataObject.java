/*******************************************************************************
 *   Copyright (c) 2013, 2019 Perun Technologii DOOEL Skopje.
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Apache License
 *   Version 2.0 or the Svarog License Agreement (the "License");
 *   You may not use this file except in compliance with the License. 
 *  
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See LICENSE file in the project root for the specific language governing 
 *   permissions and limitations under the License.
 *******************************************************************************/
package com.prtech.svarog_common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import com.google.gson.JsonObject;
import com.prtech.svarog.SvException;
import com.prtech.svarog.svCONST;

public class DbDataObject extends Jsonable {
	/**
	 * PKID Unique versioning identifier of the object in the repo table
	 */
	Long pkid = 0L;
	/**
	 * Persistent ID of the object. Doesn't change with versioning updates
	 */
	Long object_id = 0L;
	/**
	 * Timestamp when the object version was saved to the database
	 */
	DateTime dt_insert;
	/**
	 * Timestamp when the object version was invalidated, i.e. a new version has
	 * become current If the object is still valid the value is equal to
	 * svCONST.MAX_DATE
	 */
	DateTime dt_delete;
	/**
	 * Object Id of the parent
	 */
	Long parent_id = 0L;
	/**
	 * Object Id of the type of object (as per repo_tables)
	 */
	Long object_type = 0L;
	/**
	 * Status of the object. Constrained by the
	 */
	String status;
	Long user_id = 0L;
	/**
	 * Hashmap containing all meta data about the object
	 */
	LinkedHashMap<SvCharId, Object> values;

	boolean isReadOnly = false;
	/**
	 * Flag to mark the object as dirty when cached
	 */
	private boolean is_dirty = true;

	/**
	 * Flag to mark if the object is of geometry type or not
	 */
	private boolean isGeometryType = false;
	/**
	 * Flag to mark if the object has the geometry fields loaded or not
	 */
	private boolean hasGeometry = false;

	/**
	 * Default constructor
	 */
	public DbDataObject() {
		values = new LinkedHashMap<SvCharId, Object>();
	}

	public DbDataObject(Long objectType, LinkedHashMap<SvCharId, Object> keyMap) {
		this.object_type = objectType;
		values = new LinkedHashMap<SvCharId, Object>(keyMap);
		// values.values().clear();
	}

	public DbDataObject(Long objectType) {
		this();
		this.object_type = objectType;
	}

	public DbDataObject(String objectTypeName) throws Exception {
		throw (new Exception("Invalid constructor"));
	}

	@Override
	public Boolean fromJson(JsonObject obj) {
		if (!isReadOnly) {
			is_dirty = true;
			return super.jsonIO.setMembersFromJson("", this, obj);
		}
		return false;
	}

	public Object getRepoVal(String key) {
		Object retval = null;
		switch (key) {
		case "OBJECT_ID":
			retval = object_id;
			break;
		case "OBJECT_TYPE":
			retval = object_type;
			break;
		case "PARENT_ID":
			retval = parent_id;
			break;
		case "PKID":
			retval = pkid;
			break;
		case "DT_INSERT":
			retval = dt_insert;
			break;
		case "DT_DELETE":
			retval = dt_delete;
			break;
		case "STATUS":
			retval = status;
			break;
		case "USER_ID":
			retval = user_id;
			break;
		default:
			break;
		}
		return retval;
	}

	public Object getVal(String key, boolean includeRepoFields) {
		Object retval = null;
		if (includeRepoFields && svCONST.repoFieldNames.indexOf(key) >= 0) {
			retval = getRepoVal(key);
		} else
			retval = getVal(key);

		return retval;
	}

	public Object getVal(SvCharId svKey) {
		return values.get(svKey);
	}

	public Object getVal(String key) {
		SvCharId svKey = new SvCharId(key);
		return values.get(svKey);
	}

	public void setVal(String key, Object obj) {
		if (!isReadOnly) {
			is_dirty = true;
			SvCharId svKey = new SvCharId(key);
			values.put(svKey, obj);
		}
	}

	public void setVal(SvCharId key, Object obj) {
		if (!isReadOnly) {
			is_dirty = true;
			values.put(key, obj);
		}
	}

	public JsonIO getMembersJson() {
		return jsonIO;
	}

	public Set<SvCharId> getMapKeys() {
		return values.keySet();
	}

	public void setMapKeys(Collection<SvCharId> keys) {
		values.keySet().addAll(keys);
	}

	/*
	 * public String getRepo_name() { return repo_name; }
	 * 
	 * public void setRepo_name(String repo_name) { this.repo_name = repo_name;
	 * }
	 * 
	 * public String getSchema() { return schema; }
	 * 
	 * public void setSchema(String schema) { this.schema = schema; }
	 * 
	 * public String getTable_name() { return table_name; }
	 * 
	 * public void setTable_name(String table_name) { this.table_name =
	 * table_name; }
	 */
	public Long getObject_id() {
		return object_id;
	}

	public void setObject_id(Long object_id) {
		if (!isReadOnly) {
			is_dirty = true;
			this.object_id = object_id;
		}
	}

	public void setValuesMap(LinkedHashMap<SvCharId, Object> newValues) {
		this.values = newValues;
	}

	public LinkedHashMap<SvCharId, Object> getValuesMap() {
		@SuppressWarnings("unchecked")
		LinkedHashMap<SvCharId, Object> copy = (LinkedHashMap<SvCharId, Object>) values.clone();
		return copy;
	}

	@Deprecated
	public Set<Map.Entry<String, Object>> getValues() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>(values.size());
		for (Map.Entry<SvCharId, Object> entry : values.entrySet())
			map.put(entry.getKey().toString(), entry.getValue());
		return map.entrySet();
	}

	@Deprecated
	public void setValues(LinkedHashMap<String, Object> values) {
		if (!isReadOnly) {
			is_dirty = true;
			this.values.clear();
			for (Map.Entry<String, Object> entry : values.entrySet()) {
				this.values.put(SvCharId.toSvCharId(entry.getKey()), entry.getValue());

			}
		}
	}

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		if (!isReadOnly) {
			is_dirty = true;
			this.pkid = pkid;
		}
	}

	public DateTime getDt_insert() {
		return dt_insert;
	}

	public void setDt_insert(DateTime dt_insert) {
		if (!isReadOnly) {
			is_dirty = true;
			this.dt_insert = dt_insert;
		}
	}

	public DateTime getDt_delete() {
		return dt_delete;
	}

	public void setDt_delete(DateTime dt_delete) {
		if (!isReadOnly) {
			is_dirty = true;
			this.dt_delete = dt_delete;
		}
	}

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parent_id) {
		if (!isReadOnly) {
			is_dirty = true;
			this.parent_id = parent_id;
		}
	}

	public Long getObject_type() {
		return object_type;
	}

	public void setObject_type(Long object_type) {
		if (!isReadOnly) {
			is_dirty = true;
			this.object_type = object_type;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (!isReadOnly) {
			is_dirty = true;
			this.status = status;
		}
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		if (!isReadOnly) {
			is_dirty = true;
			this.user_id = user_id;
		}
	}

	public boolean getIs_dirty() {
		return is_dirty;
	}

	public void setIs_dirty(boolean is_dirty) {
		if (!isReadOnly) {
			this.is_dirty = is_dirty;
		}
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public boolean isGeometryType() {
		return isGeometryType;
	}

	public void setGeometryType(boolean isGeometryType) {
		this.isGeometryType = isGeometryType;
	}

	public boolean getHasGeometry() {
		return hasGeometry;
	}

	public void setHasGeometry(boolean hasGeometry) {
		this.hasGeometry = hasGeometry;
	}

	/**
	 * Method that return least value (skipping null values) of target column
	 * for each DbDataObject
	 * 
	 * @param targetColumn[]
	 *            Column names for checking least value in
	 * @return Object
	 */
	public Double leastWithNullSkip(String[] targetColumn) {
		Double min = null;
		ArrayList<Double> arrList = new ArrayList<>();

		if (targetColumn == null || targetColumn.length == 0) {
			return null;
		}

		for (int i = 0; i < targetColumn.length; i++) {
			String currColumnName = targetColumn[i];
			if (currColumnName.trim().length() > 0 && this.getVal(currColumnName) != null) {
				String val = this.getVal(currColumnName).toString();
				arrList.add(Double.valueOf(val));
			}
		}

		Collections.sort(arrList);
		min = (Double) arrList.get(0);

		return min;
	}

	/**
	 * Method that return least value (with nvl if null) of target column for
	 * each DbDataObject
	 * 
	 * @param targetColumn[]
	 *            Column names for checking least value in
	 * @return Object
	 */
	public Double leastWithNvl(String[] targetColumn) {
		Double min = null;
		ArrayList<Double> arrList = new ArrayList<>();

		if (targetColumn == null || targetColumn.length == 0) {
			return null;
		}

		for (int i = 0; i < targetColumn.length; i++) {
			String currColumnName = targetColumn[i];
			if (currColumnName.trim().length() > 0) {
				String val;
				if (this.getVal(currColumnName) != null) {
					val = this.getVal(currColumnName).toString();
				} else {
					val = "0";
				}

				arrList.add(Double.valueOf(val));
			}
		}

		Collections.sort(arrList);
		min = (Double) arrList.get(0);

		return min;
	}

	/**
	 * Method that return least value (if there is a null value, returns null)
	 * of target column for each DbDataObject
	 * 
	 * @param targetColumn[]
	 *            Column names for checking least value in
	 * @return Object
	 */
	public Double least(String[] targetColumn) {
		Double min = null;
		ArrayList<Double> arrList = new ArrayList<>();

		if (targetColumn == null || targetColumn.length == 0) {
			return null;
		}

		for (int i = 0; i < targetColumn.length; i++) {
			String currColumnName = targetColumn[i];
			if (currColumnName.trim().length() > 0) {
				String val;
				if (this.getVal(currColumnName) != null) {
					val = this.getVal(currColumnName).toString();
				} else {
					return null;
				}

				arrList.add(Double.valueOf(val));
			}
		}

		Collections.sort(arrList);
		min = (Double) arrList.get(0);

		return min;
	}

	/**
	 * Method that return greatest value (skipping null values) of target column
	 * for each DbDataObject
	 * 
	 * @param targetColumn[]
	 *            Column names for checking least value in
	 * @return Object
	 */
	public Double greatestWithNullSkip(String[] targetColumn) {
		Double max = null;
		ArrayList<Double> arrList = new ArrayList<>();

		if (targetColumn == null || targetColumn.length == 0) {
			return null;
		}

		for (int i = 0; i < targetColumn.length; i++) {
			String currColumnName = targetColumn[i];
			if (currColumnName.trim().length() > 0 && this.getVal(currColumnName) != null) {
				String val = this.getVal(currColumnName).toString();
				arrList.add(Double.valueOf(val));
			}
		}

		Collections.sort(arrList);
		max = (Double) arrList.get(arrList.size() - 1);

		return max;
	}

	/**
	 * Method that return greatest value (with nvl if null) of target column for
	 * each DbDataObject
	 * 
	 * @param targetColumn[]
	 *            Column name for checking greatest value
	 * @return Object
	 */
	public Double greatestWithNvl(String[] targetColumn) {
		Double max = null;
		ArrayList<Double> arrList = new ArrayList<>();

		if (targetColumn == null || targetColumn.length == 0) {
			return null;
		}

		for (int i = 0; i < targetColumn.length; i++) {
			String currColumnName = targetColumn[i];
			if (currColumnName.trim().length() > 0) {
				String val;
				if (this.getVal(currColumnName) != null) {
					val = this.getVal(currColumnName).toString();
				} else {
					val = "0";
				}

				arrList.add(Double.valueOf(val));
			}
		}

		Collections.sort(arrList);
		max = (Double) arrList.get(arrList.size() - 1);

		return max;
	}

	/**
	 * Method that return greatest value (if there is a null value, returns
	 * null) of target column for each DbDataObject
	 * 
	 * @param targetColumn[]
	 *            Column name for checking greatest value
	 * @return Object
	 */
	public Double greatest(String[] targetColumn) {
		Double min = null;
		ArrayList<Double> arrList = new ArrayList<>();

		if (targetColumn == null || targetColumn.length == 0) {
			return null;
		}

		for (int i = 0; i < targetColumn.length; i++) {
			String currColumnName = targetColumn[i];
			if (currColumnName.trim().length() > 0) {
				String val;
				if (this.getVal(currColumnName) != null) {
					val = this.getVal(currColumnName).toString();
				} else {
					return null;
				}

				arrList.add(Double.valueOf(val));
			}
		}

		Collections.sort(arrList);
		min = (Double) arrList.get(arrList.size() - 1);

		return min;
	}

}
