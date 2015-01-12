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

package com.insthub.O2OMobile.Utils;

import com.insthub.O2OMobile.Protocol.ENUM_ORDER_STATUS;

public class StringUtils
{
    public static String getOrderStatusName(int status)
    {
        if(status == ENUM_ORDER_STATUS.OS_PUBLISHED.value())
        {
            return "客户发单";
        }
        else if(status == ENUM_ORDER_STATUS.OS_KNOCK_DOWN.value()) 
        {
        	return "已确认接单";
        }
        else if(status == ENUM_ORDER_STATUS.OS_WORK_DONE.value())
        {
            return "工作完成";
        }
        else if(status == ENUM_ORDER_STATUS.OS_PAYED.value())
        {
            return "已付款";
        }
        else if(status == ENUM_ORDER_STATUS.OS_PAY_CONFORMED.value())
        {
            return "付款已确认";
        }
        else if(status == ENUM_ORDER_STATUS.OS_EMPLOYEE_COMMENTED.value())
        {
            return "雇员已评价";
        }
        else if(status == ENUM_ORDER_STATUS.OS_EMPLOYER_COMMENTED.value())
        {
            return "雇主已评价";
        }
        else if(status == ENUM_ORDER_STATUS.OS_FINISHED.value())
        {
            return "订单结束";
        }
        else if(status == ENUM_ORDER_STATUS.OS_CANCELED.value())
        {
            return "订单取消";
        }

        return "未知状态";

    }
}
