# openmrs-module-restrictbyrole
Allow roles to be restricted to view only patients that match a defined cohort.

If several restrictions are assigned to the same role, the cohort returned is the intersection of them. For example, suppose that there are two restrictions assigned to a role: the first one returns all male patients, and the second one returns all patients under 5 years old. So, the role will only can view male patients that are under 5.
