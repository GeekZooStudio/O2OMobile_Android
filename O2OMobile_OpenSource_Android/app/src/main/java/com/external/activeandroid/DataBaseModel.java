package com.external.activeandroid;

import android.database.Cursor;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/*
 *	 ______    ______    ______
 *	/\  __ \  /\  ___\  /\  ___\
 *	\ \  __<  \ \  __\_ \ \  __\_
 *	 \ \_____\ \ \_____\ \ \_____\
 *	  \/_____/  \/_____/  \/_____/
 *
 *
 *	Copyright (c) 2013-2014, {Bee} open source community
 *	http://www.bee-framework.com
 *
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a
 *	copy of this software and associated documentation files (the "Software"),
 *	to deal in the Software without restriction, including without limitation
 *	the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *	and/or sell copies of the Software, and to permit persons to whom the
 *	Software is furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in
 *	all copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *	IN THE SOFTWARE.
 */
public class DataBaseModel extends Model implements Serializable
{
    @Column(name = "jsonString")
    private String jsonString = null;

    public void fromJson(JSONObject jsonObject) throws JSONException {
        this.jsonString = jsonObject.toString();
    }

    public JSONObject toJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject)
        {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    @Override
    public void loadFromCursor(Class<? extends Model> type, Cursor cursor)
    {
        final int columnIndex = cursor.getColumnIndex("jsonString");

        if (columnIndex < 0)
        {
            super.loadFromCursor(type,cursor);
            return;
        }

        boolean columnIsNull = cursor.isNull(columnIndex);

        if(!columnIsNull)
        {
            jsonString = cursor.getString(columnIndex);

            try
            {
                JSONObject jsonObject = new JSONObject(jsonString);
                fromJson(jsonObject);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
