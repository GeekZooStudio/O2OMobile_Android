
package com.insthub.O2OMobile.Protocol;

public enum ENUM_ERROR_CODE
{
     OK(0),
     SESSION_EXPIRED(2),
     UNKNOWN_ERROR(1);

     private int value = 0;
     private ENUM_ERROR_CODE(int initValue)
     {
         this.value = initValue;
     }

     public int value()
     {
         return this.value;
     }
}
