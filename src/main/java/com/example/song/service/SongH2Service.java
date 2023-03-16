package com.example.song.service;
import com.example.song.repository.SongRepository;
import com.example.song.model.Song;
import com.example.song.model.SongRowMapper;
import java.util.*;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.*;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class SongH2Service implements SongRepository{
    @Autowired
    private JdbcTemplate db;
     
    @Override
    public ArrayList<Song> getSong(){
        List<Song> songList = db.query("select * from playlist", new SongRowMapper());
        ArrayList<Song> songs = new ArrayList<>(songList);
        return songs;
    }

    @Override
    public Song getSongById(int songId){
        try{
            Song song = db.queryForObject("select * from playlist where songId = ?", new SongRowMapper(), songId);
            return song;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Song addSong(Song song){
        db.update("insert into playlist (songName, lyricist, singer, musicDirector) values (?, ?, ?, ?)", song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());
        Song newSong = db.queryForObject("select * from playlist where songName = ? and lyricist = ? and singer = ? and musicDirector = ?", new SongRowMapper(),
        song.getSongName(), song.getLyricist(),song.getSinger(), song.getMusicDirector());
        return newSong;
    }

    @Override
    public Song updateSong(int songId, Song song){
        
        if(song.getSongName() != null){
            db.update("update playlist set songName = ? where songId = ?", song.getSongName(), songId);
        }
        if(song.getLyricist() != null){
            db.update("update playlist set lyricist = ? where songId = ?", song.getLyricist(), songId);
        }
        if(song.getSinger() != null){
            db.update("update playlist set singer = ? where songId = ?", song.getSinger(), songId);
        }
        if(song.getSinger() != null){
            db.update("update playlist set musicDirector = ? where songId = ?", song.getMusicDirector(), songId);
        }
        return getSongById(songId);
    }

    @Override
    public void deleteSong(int songId){
        db.update("delete from playlist where songId = ?", songId);
    }

    
}