package com.iseed.crm.android.common;

public class AgeGroup {
	public int ageId;
	public String groupName;
	public int menNumber;
	public int womenNumber;
	
	public AgeGroup(int id, String group, int men, int women){
		ageId = id;
		groupName = group;
		menNumber = men;
		womenNumber = women;
	}

}
