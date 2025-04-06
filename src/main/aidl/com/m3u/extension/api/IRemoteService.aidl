package com.m3u.extension.api;

import com.m3u.extension.api.IRemoteCallback;

interface IRemoteService {
    void call(String module, String method, in byte[] param, IRemoteCallback callback);
}