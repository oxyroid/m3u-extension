package com.m3u.data.extension;

import com.m3u.data.extension.IRemoteCallback;

interface IRemoteService {
    void call(String module, String method, in byte[] param, IRemoteCallback callback);
}