/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.restrictbyrole.api.db;

import java.util.List;

import org.openmrs.Role;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.restrictbyrole.RoleRestriction;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;

/**
 *  Database methods for {@link RestrictByRoleService}.
 */
public interface RestrictByRoleDAO {
	
	public void createRoleRestriction(RoleRestriction roleRestriction);
	
	public void updateRoleRestriction(RoleRestriction roleRestriction);
	
	public void deleteRoleRestriction(RoleRestriction roleRestriction);
	
	public RoleRestriction getRoleRestriction(Integer id);
	
	public List<RoleRestriction> getRoleRestrictions();

	public List<RoleRestriction> getRoleRestrictions(Role role, Boolean includeRetired);
	
	public void retireRestrictionsWithCohortDefinition(CohortDefinition cohort);
}