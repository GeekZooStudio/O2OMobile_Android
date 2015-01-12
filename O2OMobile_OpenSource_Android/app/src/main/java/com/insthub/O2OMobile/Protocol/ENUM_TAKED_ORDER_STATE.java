
package com.insthub.O2OMobile.Protocol;

public enum ENUM_TAKED_ORDER_STATE
{
     TAKED_ORDER_UNDONE(1),
     TAKED_ORDER_TENDER(0),
     TAKED_ORDER_DONE(2),
     TAKED_ORDER_ALL(3);

     private int value = 0;
     private ENUM_TAKED_ORDER_STATE(int initValue)
     {
         this.value = initValue;
     }

     public int value()
     {
         return this.value;
     }
}
