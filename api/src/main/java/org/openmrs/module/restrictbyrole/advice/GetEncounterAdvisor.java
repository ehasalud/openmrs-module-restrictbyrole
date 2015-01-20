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
package org.openmrs.module.restrictbyrole.advice;

import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Encounter;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * Advisor that intercepts the following methods: {@link org.openmrs.api.EncounterService#getEncounter(Integer)}
 */
public class GetEncounterAdvisor extends StaticMethodMatcherPointcutAdvisor implements Advisor {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return (EncounterService.class.isAssignableFrom(targetClass) && 
				method.getName().equals("getEncounter"));
	}
	
	public Advice getAdvice() {
		return new GetEncounterAdvice();
	}
	
	private class GetEncounterAdvice implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
			Cohort restrictedResult = service.getCurrentUserRestrictedPatientSet();
			if (restrictedResult == null) {
				return invocation.proceed();
			}
			if (invocation.getMethod().getName().equals("getEncounter")) {
				Encounter encounter = (Encounter) invocation.proceed();
				Integer patientId = encounter.getPatient().getId();
				if(restrictedResult.contains(patientId)){
					return encounter;
				}
			}
			return null;
		}
		
	}

}
