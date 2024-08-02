package io.github.burukeyou.data;

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
}
