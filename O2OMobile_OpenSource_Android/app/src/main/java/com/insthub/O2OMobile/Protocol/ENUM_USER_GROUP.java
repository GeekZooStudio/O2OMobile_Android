
package com.insthub.O2OMobile.Protocol;

public enum ENUM_USER_GROUP
{
     NEWBEE(0),
     FREEMAN_INREVIEW(1),
     FREEMAN(2);

     private int value = 0;
     private ENUM_USER_GROUP(int initValue)
     {
         this.value = initValue;
     }

     public int value()
     {
         return this.value;
     }
}
