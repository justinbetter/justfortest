package com.example.justfortest.rom;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Map;

/**
 * Created by justi on 2017/4/24.
 */
@JsonObject
public class RomInfoResult {

    /**
     * code : 0
     */
    @JsonField
    public StatusBean status;
    /**
     * tts : 妈妈问我为什么跪着, 因为我要变身啦, 不更新啦, 哈哈哈
     * install_mode : force
     * play_count : -1
     * rom_map : {"version":"xxxxx","name":"factory_update.zip","time":"1493004533","url":"D:\\tempdir\\tinker_multi\\factory_update.zip","size":324045340,"version_code":"123","id":"dx0016","md5":"e3bb15f778ac11148a3332c4dac6869c","description":"xxx"}
     * dialog_map : {"content":"升级后唤醒率更高, 识别更准确, 请立即升级","button_negative_text":"下次点火提醒我","button_positive_visual":true,"default_select":"positive","button_positive_text":"去升级","title":"ROM有新版本发布","button_negative_visual":true,"auto_close":true,"auto_close_time":8}
     */
    @JsonField
    public RominfoBean rominfo;
    /**
     * auto_close : true
     */
    @JsonField
    public Map<String,Map<String,String>> extend;

    @Override
    public String toString() {
        return "RomInfoResult{" +
                "status=" + status +
                ", rominfo=" + rominfo +
                ", extend=" + extend.toString() +
                '}';
    }

    @JsonObject
    public static class StatusBean {
        @JsonField
        public int code;

        @Override
        public String toString() {
            return "StatusBean{" +
                    "code=" + code +
                    '}';
        }
    }

    @JsonObject
    public static class RominfoBean {
        @JsonField
        public String tts;
        @JsonField
        public String install_mode;
        @JsonField
        public int play_count;
        /**
         * version : xxxxx
         * name : factory_update.zip
         * time : 1493004533
         * url : D:\tempdir\tinker_multi\factory_update.zip
         * size : 324045340
         * version_code : 123
         * id : dx0016
         * md5 : e3bb15f778ac11148a3332c4dac6869c
         * description : xxx
         */
        @JsonField
        public RomMapBean rom_map;
        /**
         * content : 升级后唤醒率更高, 识别更准确, 请立即升级
         * button_negative_text : 下次点火提醒我
         * button_positive_visual : true
         * default_select : positive
         * button_positive_text : 去升级
         * title : ROM有新版本发布
         * button_negative_visual : true
         * auto_close : true
         * auto_close_time : 8
         */
        @JsonField
        public DialogMapBean dialog_map;

        @Override
        public String toString() {
            return "RominfoBean{" +
                    "tts='" + tts + '\'' +
                    ", install_mode='" + install_mode + '\'' +
                    ", play_count=" + play_count +
                    ", rom_map=" + rom_map +
                    ", dialog_map=" + dialog_map +
                    '}';
        }

        @JsonObject
        public static class RomMapBean {
            @JsonField
            public String version;
            @JsonField
            public String name;
            @JsonField
            public String time;
            @JsonField
            public String url;
            @JsonField
            public long size;
            @JsonField
            public int version_code;
            @JsonField
            public String id;
            @JsonField
            public String md5;
            @JsonField
            public String description;

            @Override
            public String toString() {
                return "RomMapBean{" +
                        "version='" + version + '\'' +
                        ", name='" + name + '\'' +
                        ", time='" + time + '\'' +
                        ", url='" + url + '\'' +
                        ", size=" + size +
                        ", version_code='" + version_code + '\'' +
                        ", id='" + id + '\'' +
                        ", md5='" + md5 + '\'' +
                        ", description='" + description + '\'' +
                        '}';
            }
        }

        @JsonObject
        public static class DialogMapBean {
            @JsonField
            public String content;
            @JsonField
            public String button_negative_text;
            @JsonField
            public boolean button_positive_visual;
            @JsonField
            public String default_select;
            @JsonField
            public String button_positive_text;
            @JsonField
            public String title;
            @JsonField
            public boolean button_negative_visual;
            @JsonField
            public boolean auto_close;
            @JsonField
            public int auto_close_time;

            @Override
            public String toString() {
                return "DialogMapBean{" +
                        "content='" + content + '\'' +
                        ", button_negative_text='" + button_negative_text + '\'' +
                        ", button_positive_visual=" + button_positive_visual +
                        ", default_select='" + default_select + '\'' +
                        ", button_positive_text='" + button_positive_text + '\'' +
                        ", title='" + title + '\'' +
                        ", button_negative_visual=" + button_negative_visual +
                        ", auto_close=" + auto_close +
                        ", auto_close_time=" + auto_close_time +
                        '}';
            }
        }


    }

}
