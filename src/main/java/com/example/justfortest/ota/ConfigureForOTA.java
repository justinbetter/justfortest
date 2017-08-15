package com.example.justfortest.ota;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by justi on 2017/8/15.
 */
@JsonObject
public class ConfigureForOTA {

    public ConfigureForOTA() {

    }

    public ConfigureForOTA(String type, String mode) {
        this.type = type;
        this.mode = mode;
    }

    public interface Constants {
        //type
        String TYPE_APP   = "app";
        String TYPE_ROM   = "rom";
        String TYPE_PATCH = "patch";

        //mode
        String MODE_FULL_UPGRADE = "full_upgrade";
        String MODE_BSDIFF       = "bsdiff";
        String MODE_VENDOR       = "vendor";

        //patch category
        int PATCH_TINKER = 1;

    }


    @JsonField
    public String type = Constants.TYPE_APP;  // app rom patch
    @JsonField
    public String mode  = Constants.MODE_BSDIFF;  // full bsdiff vendor


}
