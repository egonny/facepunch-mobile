package com.egonny.facepunch.util.headeradapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * An adapter for lists with three types of elements, namely headers, items contained by those headers and subitems
 * contained by those items.
 *
 * @author Egonny
 * @version 0.1
 */
public class HeaderAdapter<S extends HeaderListHeader, T extends HeaderListItem, U extends HeaderListSubItem>
		extends ArrayAdapter<HeaderListElement> {

    public final static int HEADER = 0;
    public final static int ITEM = 1;
	public final static int SUBITEM = 2;

	private List<S> mHeaderList;

	public HeaderAdapter(Context context, int resource) {
		super(context, resource);

		mHeaderList = new ArrayList<S>();
	}

	public void addHeader(S header) {
		addHeader(header, mHeaderList.size());
	}

	/**
	 * Adds a header and its children to the adapter.
	 *
	 * @param header
	 *        The header to be added.
	 * @param position
	 *        The position in the adapter where the header will be placed.
	 */
	public void addHeader(S header, int position) {
		if (position < 0 || position > mHeaderList.size()) {
			throw new IndexOutOfBoundsException("The given position is out of bounds.");
		}
		// Add the header to the end of the list
		if (position == mHeaderList.size()) {
			mHeaderList.add(header);
			add(header);
		} else {
			mHeaderList.add(position, header);
			insert(header, getPosition(mHeaderList.get(position + 1)));
		}
		int index = getPosition(header);
		for (HeaderListItem item: header.getItems()) {
			insert(item, ++index);
			for (HeaderListSubItem subItem: item.getSubItems()) {
				insert(subItem, ++index);
			}
		}
	}

	/**
	 * Remove a header and its children from the adapter
	 *
	 * @param header
	 *        The header to be removed.
	 */
	public void removeHeader(S header) {
		if (!mHeaderList.contains(header)) return;
		remove(header);
		for (HeaderListItem item: header.getItems()) {
			remove(item);
			for (HeaderListSubItem subItem: item.getSubItems()) {
				remove(subItem);
			}
		}
		mHeaderList.remove(header);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return getItem(position).getView(inflater, convertView);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof HeaderListHeader) return HEADER;
        else if (getItem(position) instanceof  HeaderListItem) return ITEM;
	    else return SUBITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == ITEM || getItemViewType(position) == SUBITEM;
    }
}
