package net.nigne.yzrproject.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.nigne.yzrproject.domain.CouponVO;
import net.nigne.yzrproject.domain.MemberVO;
import net.nigne.yzrproject.domain.MovieVO;
import net.nigne.yzrproject.domain.PlexVO;
import net.nigne.yzrproject.domain.ReservationVO;
import net.nigne.yzrproject.domain.SeatVO;
import net.nigne.yzrproject.domain.TempLocal;
import net.nigne.yzrproject.domain.TempSeatTime;
import net.nigne.yzrproject.domain.TheaterVO;
import net.nigne.yzrproject.domain.TimetableVO;
import net.nigne.yzrproject.service.CouponService;
import net.nigne.yzrproject.service.MemberService;
import net.nigne.yzrproject.service.MovieService;
import net.nigne.yzrproject.service.PlexService;
import net.nigne.yzrproject.service.ReservationService;
import net.nigne.yzrproject.service.SeatService;
import net.nigne.yzrproject.service.TheaterService;
import net.nigne.yzrproject.service.TimetableService;

/** 
* @Package  : net.nigne.yzrproject.controller 
* @FileName : HomeController.java 
* @Date     : 2016. 7. 11. 
* @작성자	: 장내호
* @프로그램 	: 설명...
*/
@Controller
public class ReservationController {
	@Autowired
	private MovieService movieService;
	
	@Autowired
	private TheaterService theaterService;
	
	@Autowired
	private PlexService plexService;
	
	@Autowired
	private TimetableService timetableService;
	
	@Autowired
	private SeatService seatService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private MemberService memberService;

	/** 
	* @Method Name : home  
	* @Method	   : 설명... 
	* @param locale
	* @param model
	* @return
	* @throws Exception 
	*/
	@RequestMapping(value = "/ticket", method = RequestMethod.GET)
	public String home(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		HttpSession session= request.getSession();
		session.setAttribute("menu", "RESERVATION");
		String memberId = (String)session.getAttribute("member_id");
		System.out.println("111111111 = " + memberId);
		
		MemberVO member = new MemberVO();
		List<CouponVO> couponList = new ArrayList<>();
		
		List<MovieVO> movieList = movieService.getMovieList("reservation_rate");
		List<TheaterVO> theaterList = theaterService.getList("서울");
		List<String> localList = theaterService.getLocal();
		List<Long> localTheaterNum = theaterService.getLocalTheaterNum();
		if(memberId != null){
			
			couponList = couponService.getCouponList(memberId);
			member = memberService.getMember(memberId);
		}
		
		
		List<TempLocal> local = new ArrayList<>();
		
		for(int i = 0; i < localList.size(); i++) {
			TempLocal vo = new TempLocal();
			vo.setLocalCount(localTheaterNum.get(i));
			vo.setLocalName(localList.get(i));
			local.add(i,vo);
			
		}

		model.addAttribute("movieList", movieList);
		model.addAttribute("theaterList", theaterList);
		model.addAttribute("localList", local);
		model.addAttribute("couponList", couponList);
		model.addAttribute("member", member);
						
		return "ticket";
	}
	
	@RequestMapping(value = "/ticket", method = RequestMethod.POST)
	public String reservation(Locale locale, Model model,
							  @RequestParam("theaterid") String theater_id,
							  @RequestParam("reservationcode") String resrevationCode) throws Exception {
		
		List<TheaterVO> theaterList = theaterService.getTheaterList(theater_id);		
		List<ReservationVO> reservationList = reservationService.getReservationInfo(resrevationCode);
		Map<String,Object> reservationMap = reservationService.getReservationEndPage(resrevationCode);
		
		model.addAttribute("theaterList", theaterList);
		model.addAttribute("reservationList", reservationList);
		model.addAttribute("reservationMap", reservationMap);
		
		return "map";
	}
	
	@RequestMapping(value = "/ticket/movie/{page}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> moviePage(
			@PathVariable("page") String page
			) {
		ResponseEntity<Map<String, Object>> entity = null;
		System.out.println(page);
		
		try{
			List<MovieVO> list = movieService.getMovieList(page);
			
			Map<String, Object> map = new HashMap<>();
			map.put("l", list);
			

			//브라우저로 전송한다
			entity = new ResponseEntity<>(map, HttpStatus.OK);
			
		} catch(Exception e){
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value = "/ticket/theater/{page}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> theaterPage(
			@PathVariable("page") String page
			) {
		ResponseEntity<Map<String, Object>> entity = null;
		System.out.println(page);
		
		try{
			List<TheaterVO> list = theaterService.getList(page);
			
			Map<String, Object> map = new HashMap<>();
			map.put("l", list);
			

			//브라우저로 전송한다
			entity = new ResponseEntity<>(map, HttpStatus.OK);
			
		} catch(Exception e){
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value = "/ticket/timetable/{movie}/{theater}/{date}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> timetablePage(
			@PathVariable("movie") String movie,
			@PathVariable("theater") String theater,
			@PathVariable("date") String date
			) {
		ResponseEntity<Map<String, Object>> entity = null;
		
		List<MovieVO> movieList = movieService.getMovieId(movie);
		List<TheaterVO> theaterList = theaterService .getTheaterId(theater);
//		List<PlexVO> plexList = 
		
		String movieId = movieList.get(0).getMovie_id();
		String theaterId = theaterList.get(0).getTheater_id();
		
		try{
			List<TimetableVO> timetableList = new ArrayList<>();
			List<String> plexNumList = timetableService.getPlexNum(movieId, theaterId, date);
			List<PlexVO> plexTypeList = new ArrayList<>();
			List<TempSeatTime> extraSeatNum = new ArrayList<>();
			
			//timetableService.getList(movieId, theaterId, date);
			int plexNumCount = 0;
			int timetableNum = 0;
			
			String plexNum[] = new String[plexNumList.size()];

			while(plexNumList.size() > plexNumCount){				
				plexNum[plexNumCount] = plexNumList.get(plexNumCount);
				plexTypeList.addAll(plexService.getList(plexNum[plexNumCount], theaterId));
				timetableList.addAll(timetableService.getList(movieId, theaterId, date, plexNum[plexNumCount]));
				
				for(int i = 0; seatService.getExtraSeatTime(theaterId, plexNum[plexNumCount]) > i; i++) {
										
					TempSeatTime vo = new TempSeatTime();
					
					String startTime = timetableList.get(i).getStart_time();
					
					vo.setExtraSeatCount(seatService.getExtraSeatNum(theaterId, plexNum[plexNumCount], startTime));
					
					vo.setStartTime(timetableList.get(i).getStart_time());
					
					vo.setPlexNumber(plexNum[plexNumCount]);
					
					extraSeatNum.add(i, vo);
				}
				while(timetableList.size() > timetableNum){
					timetableNum++;
				}
				plexNumCount++;
			}
			
			
			
			Map<String, Object> map = new HashMap<>();
			map.put("l", timetableList);
			map.put("t", plexTypeList);
			map.put("n", extraSeatNum);
			

			//브라우저로 전송한다
			entity = new ResponseEntity<>(map, HttpStatus.OK);
			
		} catch(Exception e){
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value = "/ticket/plex/{plexNum}/{startTime}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> PlexPage(
			@PathVariable("plexNum") String plexNum,
			@PathVariable("startTime") String startTime
			) {
		ResponseEntity<Map<String, Object>> entity = null;
		System.out.println(plexNum);
		
		try{
			List<SeatVO> list = seatService.getList(plexNum, startTime);
			List<SeatVO> getIndex = seatService.getIndex(plexNum, startTime);
			
			Map<String, Object> map = new HashMap<>();
			map.put("l", list);
			map.put("i", getIndex);
			

			//브라우저로 전송한다
			entity = new ResponseEntity<>(map, HttpStatus.OK);
			
		} catch(Exception e){
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value = "/ticket/{theaterId}/{plexNum}/seat/{startTime}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> SeatPage(
			@PathVariable("theaterId") String theaterId,
			@PathVariable("plexNum") String plexNum,
			@PathVariable("startTime") String startTime,
			@RequestParam(value="seat1",required=false) String seat1,
			@RequestParam(value="seat2",required=false) String seat2,
			@RequestParam(value="seat3",required=false) String seat3,
			@RequestParam(value="seat4",required=false) String seat4,
			@RequestParam(value="seat5",required=false) String seat5,
			@RequestParam(value="seat6",required=false) String seat6,
			@RequestParam(value="seat7",required=false) String seat7,
			@RequestParam(value="seat8",required=false) String seat8
			) {

		
		ResponseEntity<Map<String, Object>> entity = null;
		
		try{
			List<Integer> getPrimary = seatService.getPrimary(theaterId, plexNum, seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8);
			
			int SeatNo = 0;
			
			for(int i = 0; i < getPrimary.size(); i++){
				SeatNo = getPrimary.get(i);
				seatService.updateReservation(SeatNo);
			}

			List<SeatVO> list = seatService.getList(plexNum, startTime);
			List<SeatVO> getIndex = seatService.getIndex(plexNum, startTime);
			
			Map<String, Object> map = new HashMap<>();
			map.put("l", list);
			map.put("i", getIndex);
			
			entity = new ResponseEntity<>(map, HttpStatus.OK);
			
		} catch(Exception e){
			
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value = "/ticket/cancel/{theaterId}/{plexNum}/{startTime}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> SeatCancel(
			@PathVariable("theaterId") String theaterId,
			@PathVariable("plexNum") String plexNum,
			@PathVariable("startTime") String startTime,
			@RequestParam(value="seat1",required=false) String seat1,
			@RequestParam(value="seat2",required=false) String seat2,
			@RequestParam(value="seat3",required=false) String seat3,
			@RequestParam(value="seat4",required=false) String seat4,
			@RequestParam(value="seat5",required=false) String seat5,
			@RequestParam(value="seat6",required=false) String seat6,
			@RequestParam(value="seat7",required=false) String seat7,
			@RequestParam(value="seat8",required=false) String seat8
			) {

		
		ResponseEntity<Map<String, Object>> entity = null;
		
		try{
			List<Integer> getPrimary = seatService.getPrimary(theaterId, plexNum, seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8);
			
			int SeatNo = 0;
			
			for(int i = 0; i < getPrimary.size(); i++){
				SeatNo = getPrimary.get(i);
				seatService.reservationCancel(SeatNo);
			}

			List<SeatVO> list = seatService.getList(plexNum, startTime);
			List<SeatVO> getIndex = seatService.getIndex(plexNum, startTime);
			
			Map<String, Object> map = new HashMap<>();
			map.put("l", list);
			map.put("i", getIndex);
			
			entity = new ResponseEntity<>(map, HttpStatus.OK);
			
		} catch(Exception e){
			
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value = "/ticket/reservation/{reservationCode}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> ReservationCode(
			@PathVariable("reservationCode") String reservationCode,
			@RequestParam(value="memberId",required=false) String memberId,
			@RequestParam(value="movieId",required=false) String movieId,
			@RequestParam(value="theaterId",required=false) String theaterId,
			@RequestParam(value="plexNumber",required=false) String plexNumber,
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="ticketCnt",required=false) String ticketCnt,
			@RequestParam(value="seat",required=false) String seat,
			@RequestParam(value="pay",required=false) String pay,
			@RequestParam(value="payMethod",required=false) String payMethod,
			@RequestParam(value="reservationDate",required=false) String reservationDate
			) {

		ResponseEntity<Map<String, Object>> entity = null;

		int ticketCntInt = Integer.parseInt(ticketCnt);
		int payInt = Integer.parseInt(pay);

		try{
			
			ReservationVO vo = new ReservationVO();
			vo.setReservation_code(reservationCode);
			vo.setMember_id(memberId);
			vo.setMovie_id(movieId);
			vo.setTheater_id(theaterId);
			vo.setPlex_number(plexNumber);
			vo.setStart_time(startTime);
			vo.setTicket_cnt(ticketCntInt);
			vo.setView_seat(seat);
			vo.setPay(payInt);
			vo.setPay_method(payMethod);
			vo.setReservation_date(reservationDate);
			
			reservationService.reservationPersist(vo);

			//브라우저로 전송한다
			entity = new ResponseEntity<>(HttpStatus.OK);
			
		} catch(Exception e){
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
		
	}
	
	@RequestMapping(value = "/ticket/seatIdentify/{theaterId}/{plexNum}/{startTime}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> SeatIdenfy(
			@PathVariable("theaterId") String theaterId,
			@PathVariable("plexNum") String plexNum,
			@PathVariable("startTime") String startTime,
			@RequestParam(value="seat1",required=false) String seat1,
			@RequestParam(value="seat2",required=false) String seat2,
			@RequestParam(value="seat3",required=false) String seat3,
			@RequestParam(value="seat4",required=false) String seat4,
			@RequestParam(value="seat5",required=false) String seat5,
			@RequestParam(value="seat6",required=false) String seat6,
			@RequestParam(value="seat7",required=false) String seat7,
			@RequestParam(value="seat8",required=false) String seat8
			) {

		
		ResponseEntity<Map<String, Object>> entity = null;
		
		try{
			List<String> list = seatService.getReservationExist(theaterId, plexNum, seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8);
			
			Map<String, Object> map = new HashMap<>();
			map.put("l", list);
			
			entity = new ResponseEntity<>(map, HttpStatus.OK);
			
		} catch(Exception e){
			
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	@RequestMapping(value = "/ticket/coupon/{couponNo}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> CouponUsed(
			@PathVariable("couponNo") String couponNo
			) {
		ResponseEntity<Map<String, Object>> entity = null;
		
		int couponNumber = Integer.parseInt(couponNo);
		try{
			System.out.println("되냐?");
			couponService.couponUsed(couponNumber);
			System.out.println("안되냐?");
			

			//브라우저로 전송한다
			entity = new ResponseEntity<>(HttpStatus.OK);
			
		} catch(Exception e){
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}

}