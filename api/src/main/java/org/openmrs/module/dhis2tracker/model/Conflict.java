/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhis2tracker.model;

public class Conflict {
	
	private String value;
	
	private String object;
	
	/**
	 * Gets the value
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value
	 *
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the object
	 *
	 * @return the object
	 */
	public String getObject() {
		return object;
	}
	
	/**
	 * Sets the object
	 *
	 * @param object the object to set
	 */
	public void setObject(String object) {
		this.object = object;
	}
	
	@Override
	public String toString() {
		String ret = "";
		if (value != null) {
			ret += value;
		}
		if (object != null) {
			ret += ("[" + object + "]");
		}
		
		return ret;
	}
	
}
