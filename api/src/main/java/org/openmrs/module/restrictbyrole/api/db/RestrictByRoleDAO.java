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
	
	/**
	 * Create a new RoleRestriction
	 * @param roleRestriction
	 */
	public void createRoleRestriction(RoleRestriction roleRestriction);
	
	/**
	 * Update an existing RoleRestriction
	 * @param roleRestriction
	 */
	public void updateRoleRestriction(RoleRestriction roleRestriction);
	
	/**
	 * Delete a RoleRestriction
	 * @param roleRestriction
	 */
	public void deleteRoleRestriction(RoleRestriction roleRestriction);
	
	/**
	 * Get the RoleRestriction with the id provided
	 * @param id Id of the RoleRestriction
	 * @return
	 */
	public RoleRestriction getRoleRestriction(Integer id);
	
	/**
	 * Get all RoleRestrictions defined
	 * @return List of RoleRestrictions
	 */
	public List<RoleRestriction> getRoleRestrictions();

	/**
	 * Get the RoleRestrictions assigned to the provided Role. 
	 * @param role Role to get the restrictions for
	 * @param includeRetired If include or not include retired RoleRestrictions
	 * @return List of RoleRestrictions associated to a Role
	 */
	public List<RoleRestriction> getRoleRestrictions(Role role, Boolean includeRetired);
	
	/**
	 * Retire the RoleRestrictions that contain the CohortDefinition provided
	 * @param cohort The CohortDefinition
	 */
	public void retireRestrictionsWithCohortDefinition(CohortDefinition cohort);
}