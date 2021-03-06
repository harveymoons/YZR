package net.nigne.yzrproject.persistence;

import java.util.List;

import net.nigne.yzrproject.domain.TheaterVO;

public interface TheaterDAO {
	public List<TheaterVO> getTheater(String location);
	public List<TheaterVO> getList(String theater);
	public List<Long> getLocalTheaterNum();
	public List<String> getLocal();
	public List<TheaterVO> getTheaterId(String theaterName);
	public List<TheaterVO> getTheaterList(String theaterId);
}
