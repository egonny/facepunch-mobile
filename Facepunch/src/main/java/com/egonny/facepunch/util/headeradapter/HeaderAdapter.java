package com.egonny.facepunch.util.headeradapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * An adapter for lists with two types of elements, namely items and headers that categorize these items.
 *
 * @author Egonny
 * @version 0.1
 */
public class HeaderAdapter<S extends HeaderListHeader, T extends HeaderListItem> extends ArrayAdapter<HeaderListElement> {

    public final static int HEADER = 0;
    public final static int ITEM = 1;

    /**
     * A List containing the headers of the adapter in the order that they will be shown in the list.
     */
    private List<HeaderListHeader> mHeaderOrder;

    /**
     * A List containing the number of items of each header, in the order defined by mHeaderOrder.
     */
    private List<Integer> mHeaderItemIndex;

    public HeaderAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mHeaderOrder = new ArrayList<HeaderListHeader>();
        mHeaderItemIndex = new ArrayList<Integer>();
    }

    /**
     * Adds a header to the adapter.
     * @param   header
     *          The header to add.
     */
    public void addHeader(S header) {
        addHeader(header, mHeaderItemIndex.size());
    }

    /**
     * Adds a header to the adapter at a certain position in the order of headers.
     * @param   header
     *          The header to add.
     * @param   position
     *          The position where the header will be added.
     *
     * @throws  IndexOutOfBoundsException
     *          Thrown when the position is smaller than zero or greater than the amount of headers currently in the adapter.
     */
    public void addHeader(S header, int position) {
        if (position < 0 || position > mHeaderOrder.size()) throw new IndexOutOfBoundsException("The given position is out of bounds.");
        mHeaderOrder.add(position, header);
        if (mHeaderItemIndex.isEmpty()) {
            mHeaderItemIndex.add(0);
            add(header);
        } else {
            int index = 0;
            if (position != 0) index = mHeaderItemIndex.get(position - 1) + 1;
            mHeaderItemIndex.add(position, index);
            insert(header, index);
        }
    }

    /**
     * Adds an item to the adapter.
     * @param   item
     *          The item to add.
     *
     * @throws  IllegalArgumentException
     *          Thrown when the items header is not yet added to this adapter.
     */
    public void addItem(T item) {
        HeaderListHeader header = item.getHeader();
        if (!mHeaderOrder.contains(header)) throw new IllegalArgumentException("The header of this item is not added to this Adapter.");
        int headerIndex = mHeaderOrder.indexOf(header);
        insert(item, mHeaderItemIndex.get(headerIndex) + 1);
        while (headerIndex < getNbHeaders()) {
            mHeaderItemIndex.set(headerIndex, mHeaderItemIndex.get(headerIndex++) + 1);
        }
    }

    /**
     * Removes a header and all items belonging to this header from the adapter.
     * @param   header
     *          The header to remove.
     */
    public void removeHeader(S header) {
        if (mHeaderOrder.contains(header)) {
            // Remove the header from the adapter
            remove(header);
            // Remove all items associated with the header
            int start = mHeaderItemIndex.get(mHeaderOrder.indexOf(header) - 1) + 1;
            int end = getLastItemIndexOf(header);
            while (start < end) {
                remove(getItem(start++));
            }
            // Adjust mHeaderItemIndex
            int index = mHeaderOrder.indexOf(header);
            int size = getNbItemsOf(header);
            for (int i = index + 1; i < getNbHeaders(); i++) {
                mHeaderItemIndex.set(i, mHeaderItemIndex.get(i) - size);
            }
            // Remove the header from the lists
            mHeaderItemIndex.remove(index);
            mHeaderOrder.remove(index);
        }
    }

    /**
     * Removes an item from the adapter.
     * @param   item
     *          The item to remove.
     */
    public void removeItem(T item) {
        HeaderListHeader header = item.getHeader();
        if (mHeaderOrder.contains(header)) {
            int index = mHeaderOrder.indexOf(header);
            remove(item);
            while (index < mHeaderItemIndex.size()) {
                mHeaderItemIndex.set(index, mHeaderItemIndex.get(index) - 1);
                index++;
            }
        }
    }

    /**
     * Returns the amount of headers currently added to the Adapter.
     */
    private int getNbHeaders() {
        assert mHeaderOrder.size() == mHeaderItemIndex.size();
        return mHeaderItemIndex.size();
    }

    /**
     * Returns the index of the last item belonging to a header.
     * @param   header
     *          The header to check the last item of.
     */
    private int getLastItemIndexOf(S header) {
        if (!mHeaderOrder.contains(header)) return -1;
        return mHeaderItemIndex.get(mHeaderOrder.indexOf(header));
    }

    /**
     * Returns the amount of items in the Adapter belonging to a header.
     * @param   header
     *          The header to check.
     */
    private int getNbItemsOf(S header) {
        if (!mHeaderOrder.contains(header)) return 0;
        int index = mHeaderOrder.indexOf(header);
        int total = 0;
        for (int i = 0; i < index; i++) total += mHeaderItemIndex.get(i);
        return mHeaderItemIndex.get(index) - total;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return getItem(position).getView(inflater, convertView);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof HeaderListHeader) return HEADER;
        else return ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == ITEM;
    }
}
