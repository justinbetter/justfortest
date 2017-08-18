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

    public ConfigureForOTA(String type, String mode,String motivate) {
        this.type = type;
        this.mode = mode;
        this.motivate = motivate;
    }

    public interface Constants {
        //type：升级类型
        String TYPE_APP   = "app";
        String TYPE_ROM   = "rom";
        String TYPE_PATCH = "patch";

        //mode：升级方式
        String MODE_FULL_UPGRADE = "full_upgrade";
        String MODE_BSDIFF       = "bsdiff";
        String MODE_VENDOR       = "vendor";

        //motivate：manual,auto 手动升级/自动升级
        String MOTIVATE_MANUAL = "manual";
        String MOTIVATE_AUTO   = "auto";


        //patch category
        int PATCH_TINKER = 1;

    }


    @JsonField
    public String type = Constants.TYPE_APP;  // app rom patch
    @JsonField
    public String mode  = Constants.MODE_BSDIFF;  // full bsdiff vendor
    @JsonField
    public String motivate  = Constants.MOTIVATE_AUTO;  // auto manual


    @Override
    public String toString() {
        return "ConfigureForOTA{" +
                "type='" + type + '\'' +
                ", mode='" + mode + '\'' +
                ", motivate='" + motivate + '\'' +
                '}';
    }
}
