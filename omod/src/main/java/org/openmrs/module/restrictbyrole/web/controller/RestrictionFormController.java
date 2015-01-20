package org.openmrs.module.restrictbyrole.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.SerializedObject;
import org.openmrs.module.restrictbyrole.RoleRestriction;
import org.openmrs.module.restrictbyrole.RoleRestrictionValidator;
import org.openmrs.module.restrictbyrole.SerializedObjectEditor;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.openmrs.propertyeditor.RoleEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/module/restrictbyrole/restrictionForm")
public class RestrictionFormController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private RoleRestrictionValidator roleRestrictionValidator;
	
	@RequestMapping(method = RequestMethod.GET)
	public void initForm(){
		
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(java.lang.Integer.class,
                new CustomNumberEditor(java.lang.Integer.class, true));
        binder.registerCustomEditor(org.openmrs.Role.class, new RoleEditor());
        binder.registerCustomEditor(org.openmrs.api.db.SerializedObject.class, new SerializedObjectEditor());
	}
	
	@ModelAttribute("restriction")
	protected RoleRestriction formBackingObject(HttpServletRequest request) throws ServletException {
		if (!Context.isAuthenticated())
			return new RoleRestriction();
		
		RoleRestriction restriction = null;
		String idStr = request.getParameter("restrictionId");
		if (idStr != null) {
			RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
			restriction = service.getRoleRestriction(Integer.valueOf(idStr));
		}
		if (restriction == null)
			restriction = new RoleRestriction();
		
		return restriction;
    }
	
	@ModelAttribute("serializedObjects")
    protected List<SerializedObject> initSerializedObjects() {
		if (!Context.isAuthenticated())
			return new ArrayList<SerializedObject>();
		
		RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
		List<SerializedObject> serializedObjects = service.getAllSerializedObjects();
		
		return serializedObjects;
	}
		
	@RequestMapping(method = RequestMethod.POST)
	protected String processSubmit(@ModelAttribute("restriction")RoleRestriction rr) {
		if (Context.isAuthenticated()) {
			RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
			if (rr.getId() == null) {
				log.info("Creating new RoleRestriction");
				service.createRoleRestriction(rr);
			} else {
				log.info("Updating RoleRestriction");
				service.updateRoleRestriction(rr);
			}
		}
		return "redirect:restrictionList.form";
	}
	
}
