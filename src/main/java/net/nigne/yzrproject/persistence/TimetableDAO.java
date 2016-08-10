package net.nigne.yzrproject.persistence;

import java.util.List;
import java.util.Map;

import net.nigne.yzrproject.domain.GenreVO;
import net.nigne.yzrproject.domain.MovieVO;
import net.nigne.yzrproject.domain.PlexVO;
import net.nigne.yzrproject.domain.TimetableVO;

public interface TimetableDAO {
	public Map<String,Object> getTimetable(String theater_id, String day);
	public List<MovieVO> getMovieInfo(String theater_id, String day);
	public List<GenreVO> getMovieGenre(String theater_id, String day);
	public List<PlexVO> getPlexInfo(String theater_id, String day);
}
