package com.egonny.facepunch.util.headeradapter;

import java.util.List;

public interface HeaderListHeader extends HeaderListElement {
    String getTitle();

	List<HeaderListItem> getItems();

	int getItemCount();
}
