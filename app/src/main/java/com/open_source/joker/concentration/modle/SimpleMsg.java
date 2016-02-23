package com.open_source.joker.concentration.modle;

/**
 * 文件名：com.open_source.joker.concentration.modle
 * 描述：
 * 时间：16/2/23
 * 作者: joker
 */
public class SimpleMsg {
    protected String title;
    protected String content;
    protected int icon;

    public SimpleMsg(String title, String content, int icon) {
        this.title = title;
        this.content = content;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
