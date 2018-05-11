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

import org.mockito.ArgumentMatcher;

import java.util.List;

public class ListMatcher extends ArgumentMatcher<List> {

    private List list;

    public ListMatcher(List list) {
        this.list = list;
    }

    @Override
    public boolean matches(Object object) {

        if (list == null) {
            return object == null;
        } else if (object == null) {
            return list == null;
        }

        List otherList = (List) object;
        if (list.size() != otherList.size()) {
            return false;
        }

        boolean matches = false;
        for (Object item : list) {
            matches = otherList.contains(item);
        }

        return matches;
    }
}
