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

import java.util.Date;

import org.openmrs.module.dhis2tracker.Dhis2TrackerConstants;

public class Enrollment {
	
	private String orgUnit;
	
	private String program;
	
	private String enrollmentDate;
	
	private String incidentDate;
	
	public Enrollment(String orgUnit, String program, String incidentDate) {
		this.orgUnit = orgUnit;
		this.program = program;
		this.enrollmentDate = Dhis2TrackerConstants.DATE_FORMATTER.format(new Date());
		this.incidentDate = incidentDate;
	}
	
	public String getOrgUnit() {
		return orgUnit;
	}
	
	public String getProgram() {
		return program;
	}
	
	public String getEnrollmentDate() {
		return enrollmentDate;
	}
	
	public String getIncidentDate() {
		return incidentDate;
	}
}
