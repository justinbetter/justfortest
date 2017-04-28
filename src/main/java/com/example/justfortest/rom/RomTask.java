package com.example.justfortest.rom;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by justi on 2017/4/21.
 */
@JsonObject
public class RomTask {

    public RomTask() {
    }

    public RomTask(int status,String description,RomInfoResult romInfoResult) {
        this.status = status;
        this.description = description;
        this.romInfoResult = romInfoResult;
    }

    @JsonField
    public int status;
    @JsonField
    public String description;
    @JsonField
    public RomInfoResult romInfoResult;

    @Override
    public String toString() {
        return "RomTask{" +
                "status=" + status +
                ", description='" + description + '\'' +
                ", romInfoResult=" + romInfoResult +
                '}';
    }
}
