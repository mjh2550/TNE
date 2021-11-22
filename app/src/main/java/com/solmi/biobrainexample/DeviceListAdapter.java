package com.solmi.biobrainexample;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * BioBrainExample
 * Class: DeviceListAdapter
 * Created by solmitech on 31/07/2019.
 * Description: Device list view item adapter
 */
public class DeviceListAdapter extends BaseAdapter {

    private final String TAG = DeviceListAdapter.class.getSimpleName();

    private LayoutInflater mLayoutInflater = null;
    /**
     * Item list
     */
    private ArrayList<BluetoothDevice> mItemList = null;

    public DeviceListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.list_item_device, null);
            holder.tvDeviceName = (TextView) view.findViewById(R.id.tv_listItemDeviceName);
            holder.tvDeviceAddress = (TextView) view.findViewById(R.id.tv_listItemDeviceAddress);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        BluetoothDevice device = mItemList.get(position);
        String name = device.getName();
        String address = device.getAddress();
        if (name == null) {
            holder.tvDeviceName.setText("null");
        } else {
            holder.tvDeviceName.setText(name);
        }

        holder.tvDeviceAddress.setText(address);
        return view;
    }

    /**
     * Initialize function
     */
    public void reset() {
        mItemList.clear();
        notifyDataSetInvalidated();
    }

    /**
     * Function to add items
     * @param device Item to add
     */
    public void addItem(BluetoothDevice device) {
        boolean isExistItem = isExistItem(device.getAddress());
        if (isExistItem) {
            return;
        }

        mItemList.add(device);
        notifyDataSetInvalidated();
    }

    /**
     * Function to check if items to add are duplicates
     * @param address Device address value
     * @return Whether the item is a duplicate
     */
    private boolean isExistItem(String address) {
        boolean result = false;
        for (BluetoothDevice device :
                mItemList) {
            if (device.getAddress().equals(address)) {
                result = true;
                break;
            }
        }

        return result;
    }
}

/**
 * ViewHolder
 * Use it to speed up getView.
 * Use viewHolder to reuse as a single findViewByID.
 */
class ViewHolder {
    public TextView tvDeviceName = null;
    public TextView tvDeviceAddress = null;
}
