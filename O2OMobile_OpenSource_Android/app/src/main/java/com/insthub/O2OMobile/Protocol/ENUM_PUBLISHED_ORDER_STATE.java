
package com.insthub.O2OMobile.Protocol;

public enum ENUM_PUBLISHED_ORDER_STATE
{
     PUBLISHED_ORDER_ALL(2),
     PUBLISHED_ORDER_DONE(1),
     PUBLISHED_ORDER_UNDONE(0);

     private int value = 0;
     private ENUM_PUBLISHED_ORDER_STATE(int initValue)
     {
         this.value = initValue;
     }

     public int value()
     {
         return this.value;
     }
}
