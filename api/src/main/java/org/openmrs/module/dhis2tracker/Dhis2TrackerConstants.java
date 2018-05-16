/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhis2tracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Dhis2TrackerConstants {
	
	public static final String MODULE_ID = "dhis2tracker";
	
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final String HEADER_ACCEPT = "Accept";
	
	public static final String HEADER_AUTH = "Authorization";
	
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	
	public static final String CONTENT_TYPE_JSON = "application/json";
	
	public static final String CONTENT_TYPE_XML = "application/xml";
	
	public static final String LOINC_CODE_CASE_REPORT = "55751-2";
	
	public static final String PERSON_ATTRIBUTE_TYPE_UUID = "ab1e48f4-5528-11e8-9ed7-c05155b794c3";
	
	public static final String TRIGGER_CONCEPT_SOURCE = "SNOMED CT";
	
	public static final String TRIGGER_CONCEPT_CODE = "410658008";
	
	public static final String GP_URL = MODULE_ID + ".dhis2Url";
	
	public static final String GP_USERNAME = MODULE_ID + ".username";
	
	public static final String GP_PASSWORD = MODULE_ID + ".password";
	
	public static final String GP_TRACKED_ENTITY_TYPE_UID = MODULE_ID + ".trackedEntityTypeUID";
	
	public static final String GP_PROGRAM_UID = MODULE_ID + ".programUID";
	
	public static final String GP_PROGRAM_STATE_UID = MODULE_ID + ".newHivCaseProgramStateUID";
	
	public static final String GP_ATTRIB_FIRSTNAME_UID = MODULE_ID + ".firstnameUID";
	
	public static final String GP_ATTRIB_MIDDLENAME_UID = MODULE_ID + ".middlenameUID";
	
	public static final String GP_ATTRIB_LASTNAME_UID = MODULE_ID + ".lastnameAttributeUID";
	
	public static final String GP_ATTRIB_GENDER_UID = MODULE_ID + ".genderAttributeUID";
	
	public static final String GP_ATTRIB_BIRTHDATE_UID = MODULE_ID + ".birthdateUID";
	
}
