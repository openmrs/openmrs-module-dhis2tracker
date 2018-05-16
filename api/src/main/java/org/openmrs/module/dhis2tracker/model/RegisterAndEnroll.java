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

import java.util.ArrayList;
import java.util.List;

public class RegisterAndEnroll {
	
	private String trackedEntityType;
	
	private String orgUnit;
	
	private List<Attribute> attributes = new ArrayList<>();
	
	private List<Enrollment> enrollments = new ArrayList<>();
	
	public RegisterAndEnroll(String trackedEntityType, String orgUnit, List<Attribute> attributes, Enrollment enrollment) {
		this.trackedEntityType = trackedEntityType;
		this.orgUnit = orgUnit;
		this.attributes = attributes;
		enrollments.add(enrollment);
	}
	
	public String getTrackedEntityType() {
		return trackedEntityType;
	}
	
	public String getOrgUnit() {
		return orgUnit;
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	public List<Enrollment> getEnrollments() {
		return enrollments;
	}
}
