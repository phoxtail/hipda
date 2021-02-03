/*
 * Copyright 2012 CREADOR GRANOESTE<granoete@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.jejer.hipda.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

/**
 * Key and Value Array Adapter
 */
public class KeyValueArrayAdapter extends ArrayAdapter<KeyValueArrayAdapter.KeyValue> {

    private String[] entries;
    private String[] entryValues;

    public KeyValueArrayAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
    }

    /**
     * Change the string value of the TextView with the value of the KeyValue.
     */
    @NotNull
    @Override
    public View getView(final int position, final View convertView, @NotNull final ViewGroup parent) {
        final TextView view = (TextView) super.getView(position, convertView, parent);

        view.setText(getItem(position).value);
        return view;
    }

    /**
     * Change the string value of the TextView with the value of the KeyValue.
     */
    @Override
    public View getDropDownView(final int position, final View convertView, @NotNull final ViewGroup parent) {
        final TextView view = (TextView) super.getDropDownView(position, convertView, parent);

        view.setText(getItem(position).value);
        return view;
    }

    /**
     * Set the specified Collection at the array.
     */
    public void setKeyValue(final String[] keys, final String[] vaules) {
        if (keys.length != vaules.length) {
            throw new RuntimeException("The length of keys and values is not in agreement.");
        }

        final int N = keys.length;
        for (int i = 0; i < N; i++) {
            add(new KeyValue(keys[i], vaules[i]));
        }
    }

    /**
     * Set the specified Collection at the array.
     */
    public void setEntries(final String[] entries) {
        this.entries = entries;
        if (entryValues != null) {
            setKeyValue(entryValues, entries);
        }
    }

    /**
     * Set the specified Collection at the array.
     */
    public void setEntryValues(final String[] entryValues) {
        this.entryValues = entryValues;
        if (entries != null) {
            setKeyValue(entryValues, entries);
        }
    }

    /**
     * Get the key of the KeyValue with the specified position in the data set.
     */
    public String getKey(final int position) {
        return getItem(position).key;
    }

    /**
     * Get the entry value of the KeyValue with the specified position in the data set.
     */
    public String getEntryValue(final int position) {
        return getKey(position);
    }

    /**
     * Key and Value
     */
    public static class KeyValue {
        public final String key;
        public final String value;

        public KeyValue(final String key, final String value) {
            super();
            this.key = key;
            this.value = value;
        }
    }
}