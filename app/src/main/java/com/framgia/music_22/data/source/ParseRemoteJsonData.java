package com.framgia.music_22.data.source;

import com.framgia.music_22.data.GetSongDataSource;
import com.framgia.music_22.data.OnFetchDataListener;
import com.framgia.music_22.data.RequestCallbackData;
import com.framgia.music_22.data.model.Artist;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.utils.Constant;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseRemoteJsonData implements GetSongDataSource.RemoteDataSource {
    private static ParseRemoteJsonData sInstance;

    public static synchronized ParseRemoteJsonData getInstance() {
        if (sInstance == null) {
            sInstance = new ParseRemoteJsonData();
        }
        return sInstance;
    }

    public MoreSong parseSongList(String jsonInput) {
        ArrayList<Song> songs = new ArrayList<>();
        MoreSong moreSong = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonInput);
            JSONArray songJsons = jsonObject.getJSONArray(Constant.ARRAY_JSON_NAME);
            int jsonArrayLength = songJsons.length();

            for (int i = 0; i < jsonArrayLength; i++) {
                JSONObject songJson = songJsons.getJSONObject(i);
                JSONObject artistJson = songJson.getJSONObject("user");
                Song song = new Song.Builder().withSongId(
                        songJson.getString(Song.APISongProperties.SONG_ID))
                        .withTitle(songJson.getString(Song.APISongProperties.TITLE))
                        .withGenre(songJson.getString(Song.APISongProperties.GENRE))
                        .withStreamUrl(songJson.getString(Song.APISongProperties.STREAM_URL))
                        .withDuaration(Integer.parseInt(
                                songJson.getString(Song.APISongProperties.DUARATION)))
                        .withUri(songJson.getString(Song.APISongProperties.SONG_URI))
                        .withUserId(songJson.getString(Song.APISongProperties.USER_ID))
                        .withArtist(
                                new Artist(artistJson.getString(Artist.APIArtistProperties.USER_ID),
                                        artistJson.getString(Artist.APIArtistProperties.USER_NAME),
                                        artistJson.getString(
                                                Artist.APIArtistProperties.AVATAR_URL)))
                        .build();
                songs.add(song);
            }
            String nextHref = jsonObject.getString(Constant.NEXT_HREF);
            moreSong = new MoreSong(songs, nextHref);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moreSong;
    }

    @Override
    public void getSongByGenre(String genre, final RequestCallbackData<MoreSong> callbackData) {
        new GetDataFromUrl(new OnFetchDataListener() {

            @Override
            public void onFetchDataSuccess(String resutlData) {
                MoreSong moreSong = null;
                moreSong = parseSongList(resutlData);
                callbackData.onGetDataSuccess(moreSong);
            }

            @Override
            public void onFetchDataError(Exception errorEx) {
                callbackData.onGetDataError(errorEx);
            }
        }).execute(Constant.GENRES_URL + genre + Constant.LIMIT_NUMBER);
    }
}
