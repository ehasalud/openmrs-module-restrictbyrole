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
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Cohort;
import org.openmrs.api.PatientService;
import org.openmrs.api.PatientSetService;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyrole.UserRestrictionResult;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * Advisor that intercepts the following methods: {@link org.openmrs.api.PatientSetService#getAllPatients()},
 * {@link org.openmrs.api.PatientService#findPatients(String, boolean)} and 
 * {@link org.openmrs.api.PatientService#getPatients()}
 */
public class GetManyPatientsAdvisor extends StaticMethodMatcherPointcutAdvisor implements Advisor {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());

	public boolean matches(Method method, Class targetClass) {
		return
			(PatientSetService.class.isAssignableFrom(targetClass) && method.getName().equals("getAllPatients")) ||
			(PatientService.class.isAssignableFrom(targetClass) && method.getName().equals("findPatients")) ||
			(PatientService.class.isAssignableFrom(targetClass) && method.getName().equals("getPatients"));
	}
	
	public Advice getAdvice() {
		return new GetManyPatientAdvice();
	}
	
	private class GetManyPatientAdvice implements MethodInterceptor {
		public Object invoke(MethodInvocation invocation) throws Throwable {
			RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
			UserRestrictionResult result = service.getCurrentUserRestrictionResult();
			if (!result.isRestricted())
				return invocation.proceed();
			if (invocation.getMethod().getName().equals("getAllPatients")) {
				return result.getCohort();
			} else { // "findPatients"
				List<Patient> originalResult = (List<Patient>) invocation.proceed();
				Cohort restriction = result.getCohort();
				
				List<Patient> newResult = new ArrayList<Patient>();
				for (Patient p : originalResult)
					if (restriction.contains(p.getPatientId()))
						newResult.add(p);
				return newResult;
			}
		}
	}

}
