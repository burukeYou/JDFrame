package io.github.burukeyou.data;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class UserInfo {

    private String key1;
    private int key2;
    private String key3;
    private String key4;

    public UserInfo() {
    }

    public UserInfo(String key1, int key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    public UserInfo(String key1, int key2, String key3) {
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
