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

import java.util.HashMap;


public interface ISvCodeList {

	public HashMap<Long, String> getCodeCategoriesId();

	public HashMap<Long, String> getCodeListId(String languageId, Long codeListObjectId);

	public HashMap<Long, String> getCodeListId(Long codeListObjectId);

	public DbDataArray getCodeListBase(Long codeListObjectId);

	public HashMap<Long, String> getCodeCategoriesId(String languageId);

	public HashMap<String, String> getCodeCategories();

	public HashMap<String, String> getCodeList(String languageId, Long codeListObjectId, Boolean includeLabels);

	public HashMap<String, String> getCodeList(Long codeListObjectId, Boolean includeLabels);

	public HashMap<String, String> getCodeCategories(String languageId, Boolean includeLabels);
}
