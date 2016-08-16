package com.model;

import java.util.List;

public class ExpandableModel {
	List<GroupModel> listDataGroup;
	List<GroupItemModel> listDataGroupChild;

	public ExpandableModel() {}

	public ExpandableModel(List<GroupModel> listDataGroup, List<GroupItemModel> listDataGroupChild) {
		super();
		this.listDataGroup = listDataGroup;
		this.listDataGroupChild = listDataGroupChild;
	}

	public List<GroupModel> getListDataGroup() {
		return listDataGroup;
	}

	public void setListDataGroup(List<GroupModel> listDataGroup) {
		this.listDataGroup = listDataGroup;
	}

	public List<GroupItemModel> getListDataGroupChild() {
		return listDataGroupChild;
	}

	public void setListDataGroupChild(List<GroupItemModel> listDataGroupChild) {
		this.listDataGroupChild = listDataGroupChild;
	}
}
