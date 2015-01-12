
package com.insthub.O2OMobile.Protocol;

public enum ENUM_ORDER_STATUS
{
     OS_EMPLOYEE_COMMENTED(5),
     OS_PUBLISHED(0),
     OS_EMPLOYER_COMMENTED(6),
     OS_PAYED(3),
     OS_FINISHED(7),
     OS_KNOCK_DOWN(1),
     OS_CANCELED(8),
     OS_PAY_CONFORMED(4),
     OS_WORK_DONE(2);

     private int value = 0;
     private ENUM_ORDER_STATUS(int initValue)
     {
         this.value = initValue;
     }

     public int value()
     {
         return this.value;
     }
}
