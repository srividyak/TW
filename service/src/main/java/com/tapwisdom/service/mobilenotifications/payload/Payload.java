package com.tapwisdom.service.mobilenotifications.payload;

import com.tapwisdom.core.common.util.Utils;

import java.io.Serializable;

public class Payload implements Serializable {
    public String toString() {
        return Utils.getStringFromObject(this);
    }
}
