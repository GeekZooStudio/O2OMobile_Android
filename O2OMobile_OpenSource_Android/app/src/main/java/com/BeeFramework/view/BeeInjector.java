//
//       _/_/_/                      _/            _/_/_/_/_/
//    _/          _/_/      _/_/    _/  _/              _/      _/_/      _/_/
//   _/  _/_/  _/_/_/_/  _/_/_/_/  _/_/              _/      _/    _/  _/    _/
//  _/    _/  _/        _/        _/  _/          _/        _/    _/  _/    _/
//   _/_/_/    _/_/_/    _/_/_/  _/    _/      _/_/_/_/_/    _/_/      _/_/
//
//
//  Copyright (c) 2015-2016, Geek Zoo Studio
//  http://www.geek-zoo.com
//
//
//  Permission is hereby granted, free of charge, to any person obtaining a
//  copy of this software and associated documentation files (the "Software"),
//  to deal in the Software without restriction, including without limitation
//  the rights to use, copy, modify, merge, publish, distribute, sublicense,
//  and/or sell copies of the Software, and to permit persons to whom the
//  Software is furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
//  IN THE SOFTWARE.
//

package com.BeeFramework.view;

import android.app.Activity;

import java.lang.reflect.Field;

public class BeeInjector {
	private static BeeInjector instance;

	private BeeInjector() {

	}

	public static BeeInjector getInstance() {
		if (instance == null) {
			instance = new BeeInjector();
		}
		return instance;
	}

	public void injectView(Activity activity) {
		// TODO Auto-generated method stub
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(BeeInjectId.class)) {
					injectView(activity, field);
				}
			}
		}
	}

	private void injectView(Activity activity, Field field) {
		// TODO Auto-generated method stub
		if (field.isAnnotationPresent(BeeInjectId.class)) {
			BeeInjectId viewInject = field.getAnnotation(BeeInjectId.class);
			int viewId = viewInject.id();
			try {
				field.setAccessible(true);
				field.set(activity, activity.findViewById(viewId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
