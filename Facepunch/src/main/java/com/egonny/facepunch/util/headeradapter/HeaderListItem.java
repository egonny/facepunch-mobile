package com.egonny.facepunch.util.headeradapter;

import java.util.List;

public interface HeaderListItem extends HeaderListElement {
    HeaderListHeader getHeader();

	List<HeaderListSubItem> getSubItems();

	int getSubItemCount();
}
