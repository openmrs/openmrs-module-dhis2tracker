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

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportSummary {
	
	public static final String STATUS_SUCCESS = "SUCCESS";
	
	private String status;
	
	private String reference;
	
	private ImportSummary enrollments;
	
	private List<ImportSummary> importSummaries;
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public ImportSummary getEnrollments() {
		return enrollments;
	}
	
	public void setEnrollments(ImportSummary enrollments) {
		this.enrollments = enrollments;
	}
	
	public List<ImportSummary> getImportSummaries() {
		return importSummaries;
	}
	
	public void setImportSummaries(List<ImportSummary> importSummaries) {
		this.importSummaries = importSummaries;
	}
	
}
