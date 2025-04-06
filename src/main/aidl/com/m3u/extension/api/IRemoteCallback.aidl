package com.m3u.extension.api;

interface IRemoteCallback {
    void onSuccess(String module, String method, in byte[] param);
    void onError(String module, String method, int errorCode, String errorMessage);
}