package com.xin.lib.dbridge.listener;

public interface JavascriptCloseWindowListener {
    /**
     * @return If true, close the current activity, otherwise, do nothing.
     */
    boolean onClose();
}