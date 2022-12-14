package study.projects.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import study.projects.entity.Booking;
import study.projects.entity.Ride;

@CrossOrigin
@RestController
@RequestMapping(path="/sharesteer")
public class RideController {

	@Autowired
	JdbcTemplate temp;

	@PostMapping("/addride")
	public String f1(@RequestBody Ride r)
	{	String messege="";	
	if(r==null)
		return "Enter Valid Data";
	try {
		temp.update("insert into ride (user_id,vehicle_id,date_of_journey,time_of_journey,from_city,to_city,total_seats,available_seats,Status,ride_cost,description,Only_females) values(?,?,?,?,?,?,?,?,?,?,?,?)",r.getUser_id(),r.getVehicle_id(),r.getDate_of_journey(),
				r.getTime_of_journey(),r.getFrom_city(),r.getTo_city(),r.getTotal_seats(),r.getAvailable_seats(),r.getStatus(),
				r.getRide_cost(),r.getDescription(),r.isOnly_females());
		messege="Ride added successfully";
	} catch (DataAccessException e) {
		e.printStackTrace();
		messege="Query Failed"+e.getMessage();

	}

	return messege;
	}

	@PostMapping("/getrides")
	public ArrayList<Ride> f2(@RequestBody Ride r)
	{
		String c1=r.getFrom_city();
		String c2=r.getTo_city();
		String d=r.getDate_of_journey();

		List<Ride> al=new ArrayList<>();
		try {
			al = temp.query("select * from ride where from_city='"+c1+"' and to_city='"+c2+"' and date_of_journey>='"+d+"'",(rs,rownum)->{return new Ride(rs.getInt(1),
					rs.getInt(2),rs.getInt(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8),rs.getInt(9),rs.getString(10),rs.getInt(11),
					rs.getString(12),rs.getString(13));});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return (ArrayList<Ride>)al;
	}

	@GetMapping("/getAllrides")
	public ArrayList<Ride> f3()
	{
		String queryString="select ride_id,user_id,vehicle_id,date_of_journey,time_of_journey,from_city.city_name,to_city.city_name,total_seats,available_seats,ride_cost,description,status from ride,from_city,to_city where ride.from_city=from_city.city_id and ride.to_city=to_city.city_id order by date_of_journey";

		List<Ride> list =new ArrayList<>();
		try {
			list = temp.query(queryString,(rs,rownum)->{return new Ride(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8),rs.getInt(9),rs.getInt(10),rs.getString(11),rs.getString(12));});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return (ArrayList<Ride>)list;
	}

	@GetMapping("/getMyRide/{id}")
	public Ride f4(@PathVariable int id)
	{
		Ride ride=new Ride();
		try {
			ride = temp.queryForObject("select * from Ride where ride_id="+id,(rs,rownum)->{return new Ride(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8),rs.getInt(9),rs.getInt(10),rs.getString(11),rs.getString(12));});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return ride;
	}
//*************************************************************************************************************	
	@GetMapping("/getRequest/{id}")
	public ArrayList<Booking> allRequests(@PathVariable int id)
	{
		String queryString="select first_name,last_name,contact,email_id,number_of_seats,status ,booking_id from booking,user where booking.user_id=user.user_id and ride_id="+id;

		List<Booking> book=new ArrayList<Booking>();
		try {
			                                                              //// first_name | last_name | contact | email_id | number_of_seats | status
			book = temp.query(queryString,(rs,rownum)->{return new Booking(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5),rs.getString(6),rs.getInt(7));});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		System.out.println(book);
		return (ArrayList<Booking>) book;
	}
	
	@GetMapping("/getMyOnlyRide/{id}")
	public ArrayList<Ride> getMyRide(@PathVariable int id)
	{
		String queryString="select ride_id,user_id,vehicle_id,date_of_journey,time_of_journey,from_city.city_name,to_city.city_name,total_seats,available_seats,ride_cost,description,status from ride,from_city,to_city where ride.from_city=from_city.city_id and ride.to_city=to_city.city_id and date_of_journey>=now() and user_id="+id;

		List<Ride> list =new ArrayList<>();
		try {
			list = temp.query(queryString,(rs,rownum)->{return new Ride(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8),rs.getInt(9),rs.getInt(10),rs.getString(11),rs.getString(12));});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return (ArrayList<Ride>)list;
	}
}
