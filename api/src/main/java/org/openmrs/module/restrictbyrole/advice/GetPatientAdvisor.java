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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyrole.RestrictByRoleAuthorizationException;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * Advisor that intercepts the following methods: {@link org.openmrs.api.PatientService#getPatient(Integer)}
 *  and {@link org.openmrs.api.PatientService#updatePatient(Patient)}
 */
public class GetPatientAdvisor extends StaticMethodMatcherPointcutAdvisor implements Advisor {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());
	
	public boolean matches(Method method, Class targetClass) {
		String methodName = method.getName();
		return methodName.equals("getPatient") || methodName.equals("updatePatient");
	}
	
	public Advice getAdvice() {
		return new GetPatientAdvice();
	}
	
	private class GetPatientAdvice implements MethodBeforeAdvice {
		public void before(Method m, Object[] args, Object target) throws Throwable {
			RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
			Integer patientId = null;
			if (m.getName().equals("getPatient"))
				patientId = (Integer) args[0];
			else
				patientId = ((Patient) args[0]).getPatientId();
			if (patientId != null && !service.doesCurrentUserHavePermission(patientId))
				throw new RestrictByRoleAuthorizationException("No permission on " + patientId);
		}
	}

}
