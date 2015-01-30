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
package org.openmrs.module.restrictbyrole.api;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyrole.RoleRestriction;
import org.openmrs.module.restrictbyrole.UserRestrictionResult;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests {@link ${RestrictByRoleService}}.
 */
public class  RestrictByRoleServiceTest extends BaseModuleContextSensitiveTest {
	
	@Test
	public void shouldSetupContext() {
		assertNotNull(Context.getService(RestrictByRoleService.class));
	}
	
	@Test
	public void shouldGetRoleRestrictions() throws Exception {
		executeDataSet("org/openmrs/module/restrictbyrole/include/RestrictByRoleRecords.xml");
		
		RestrictByRoleService service = Context.getService(RestrictByRoleService.class);
		List<RoleRestriction> list = service.getRoleRestrictions();
		
		Assert.assertEquals(9, list.size());
	}
	
	@Test
	public void shouldGetRoleRestrictionById() throws Exception {
		executeDataSet("org/openmrs/module/restrictbyrole/include/RestrictByRoleRecords.xml");
		
		RestrictByRoleService service = Context.getService(RestrictByRoleService.class);
		RoleRestriction rr = service.getRoleRestriction(1);
		
		Assert.assertNotNull(rr);
		Assert.assertEquals(new Integer(1), rr.getId());
	}
	
	@Test
	public void shouldDeleteRoleRestrictionById() throws Exception {
		executeDataSet("org/openmrs/module/restrictbyrole/include/RestrictByRoleRecords.xml");
		
		RestrictByRoleService service = Context.getService(RestrictByRoleService.class);
		RoleRestriction rr = service.getRoleRestriction(1);
		
		service.deleteRoleRestriction(rr);
		
		RoleRestriction newRr = service.getRoleRestriction(rr.getId());
		
		Assert.assertNull(newRr);
		
	}
	
	@Test
	public void shouldGetRoleRestrictionsByRole() throws Exception {
		executeDataSet("org/openmrs/module/restrictbyrole/include/RestrictByRoleRecords.xml");
		
		RestrictByRoleService service = Context.getService(RestrictByRoleService.class);
		Role restricted = new Role("Restricted");
		List<RoleRestriction> list = service.getRoleRestrictions(restricted);
		
		Assert.assertEquals(4, list.size());
		Assert.assertEquals("Restricted", list.get(0).getRole().getName());
		
		List<RoleRestriction> activeList = service.getActiveRoleRestrictions(restricted);
		
		Assert.assertEquals(3, activeList.size());
		Assert.assertEquals("Restricted", activeList.get(0).getRole().getName());
	}
	
	@Test
	public void shouldGetCurrentUserRestrictions() throws Exception{
		executeDataSet("org/openmrs/module/restrictbyrole/include/RestrictByRoleRecords.xml");
		
		if(Context.isAuthenticated()){
			Context.logout();
		}
		
		Context.authenticate("restrict", "Byrole123");
		
		RestrictByRoleService service = Context.getService(RestrictByRoleService.class);
		
		Set<RoleRestriction> list = service.getCurrentUserRestrictions(true);
		Assert.assertEquals(3, list.size());
		
		Set<RoleRestriction> activeList = service.getCurrentUserRestrictions(false);
		Assert.assertEquals(2, activeList.size());
	}
	
	@Test
	public void shouldGetCurrentUserRestrictions_multipleRoles() throws Exception {
		executeDataSet("org/openmrs/module/restrictbyrole/include/RestrictByRoleRecords.xml");
		executeDataSet("org/openmrs/module/restrictbyrole/include/AddExtraRole.xml");
		
		if(Context.isAuthenticated()){
			Context.logout();
		}
		
		Context.authenticate("restrict", "Byrole123");
		
		RestrictByRoleService service = Context.getService(RestrictByRoleService.class);
		
		Set<RoleRestriction> list = service.getCurrentUserRestrictions(true);
		Assert.assertEquals(7, list.size());
		
		Set<RoleRestriction> activeList = service.getCurrentUserRestrictions(false);
		Assert.assertEquals(5, activeList.size());
	}
	
	@Test
	public void shouldGetCurrentUserRestrictedPatientSet() throws Exception{
		executeDataSet("org/openmrs/module/restrictbyrole/include/RestrictByRoleRecords.xml");
		executeDataSet("org/openmrs/module/restrictbyrole/include/CohortQueries.xml");
		
		if(Context.isAuthenticated()){
			Context.logout();
		}
		
		Context.authenticate("restrict", "Byrole123");
		
		RestrictByRoleService service = Context.getService(RestrictByRoleService.class);
		service.invalidateGetUserRestrictedPatientSetCache();
		
		UserRestrictionResult result = service.getCurrentUserRestrictionResult();
		Assert.assertTrue(result.isRestricted());
		Assert.assertEquals(2, result.getCohort().getSize());
		
	}
	
	@Test
	public void shouldGetCurrentUserRestrictedPatientSet_multipleRoles() throws Exception{
		executeDataSet("org/openmrs/module/restrictbyrole/include/RestrictByRoleRecords.xml");
		executeDataSet("org/openmrs/module/restrictbyrole/include/CohortQueries.xml");
		executeDataSet("org/openmrs/module/restrictbyrole/include/AddExtraRole.xml");

		if(Context.isAuthenticated()){
			Context.logout();
		}
		
		Context.authenticate("restrict", "Byrole123");
		
		RestrictByRoleService service = Context.getService(RestrictByRoleService.class);
		service.invalidateGetUserRestrictedPatientSetCache();
		
		UserRestrictionResult result = service.getCurrentUserRestrictionResult();
		Assert.assertTrue(result.isRestricted());
		Assert.assertEquals(1, result.getCohort().getSize());
		
	}

}
