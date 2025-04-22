package com.m3u.extension.api.business

import com.m3u.annotation.InheritedOverride
import com.m3u.extension.api.Method
import com.m3u.extension.api.Module
import com.m3u.extension.api.model.AddChannelRequest
import com.m3u.extension.api.model.AddPlaylistRequest
import com.m3u.extension.api.model.Channel
import com.m3u.extension.api.model.ObtainPlaylistsResponse
import com.m3u.extension.api.model.Playlist
import com.m3u.extension.api.model.Result

@Module("subscribe")
interface SubscribeApi {
    @Method("addPlaylist_v2")
    @InheritedOverride
    suspend fun addPlaylist(playlist: Playlist): Result

    @Method("addChannel_v2")
    @InheritedOverride
    suspend fun addChannel(channel: Channel): Result

    @Method("obtainPlaylists")
    @InheritedOverride
    suspend fun obtainPlaylists(): ObtainPlaylistsResponse

    @Method("obtainPlaylists")
    @InheritedOverride
    suspend fun obtainChannels(playlist: Playlist): ObtainPlaylistsResponse

    @Deprecated(
        message = "Use addPlaylist(playlist: Playlist) overload instead",
        replaceWith = ReplaceWith("this.addPlaylist(playlist)")
    )
    @Method("addPlaylist")
    @InheritedOverride
    suspend fun addPlaylist(req: AddPlaylistRequest): Result

    @Deprecated(
        message = "Use addChannel(channel: Channel) overload instead",
        replaceWith = ReplaceWith("this.addChannel(channel)")
    )
    @Method("addChannel")
    @InheritedOverride
    suspend fun addChannel(req: AddChannelRequest): Result
}