import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import utils.jdbcUtils;

public class Runner {
	private static String url = "jdbc:mysql://localhost:3306/Hotel_Reservation";
	private static String user = "root";
	private static String password = "Juyeol123~";

	public static void main(String[] args) {
		System.out.println("\nWelcome to Hotel Management System.");
		showOption(); 
	}


	private static void showOption() {
		Scanner sc = new Scanner(System.in);
		String option = "";

		while(option != "q"){
			System.out.println();
			System.out.println("================================");
			System.out.println("1. Show available hotels");
			System.out.println("2. Show reservations");
			System.out.println("3. Show available hotel over three");      
			System.out.println("4. Show Best Rating Hotel");
			System.out.println("5. Show Most Expensive Hotel");
			System.out.println("6. Show Cheapest Hotel");     
			System.out.println("7. Sort worst rating Hotel");
			System.out.println("8. Show sorted by highest price");
			System.out.println("9. Show sorted by lowest price");     
			System.out.println("10. Show sorted by highest rating");
			System.out.println("11. Show sorted by lowest rating");
			System.out.println("12. Show hotels < $200 and > 3 stars");     
			System.out.println("13. Show hotels > $200 and < 2 stars");
			System.out.println("14. Show 3days price");
			System.out.println("15. Show difference highest and lowest price of hotel");      
			System.out.println("16. Archive");           
			System.out.println("17. Key constraints");           
			System.out.println("18. Foreign key constraints");            
			System.out.println("q. Quit");
			System.out.println("================================");

			option = sc.next().toLowerCase();
			System.out.println();

			switch (option) {
				case "1":  showEmptyRoom(); 					break;
				case "2":  showReservations(); 				break;
				case "3":  availableHotelOverThree(); break;
				case "4":  showBestHotel(); 					break;
				case "5":  showExpensive(); 					break;
				case "6":  showCheapest(); 						break;
				case "7":  showWorstRating(); 				break;
				case "8":  sortHighPrice(); 					break;
				case "9":  sortLowPrice(); 						break;
				case "10": sortHighRate(); 						break;
				case "11": sortLowRate(); 						break;
				case "12": showGoodValue(); 					break;		
				case "13": showBadValue(); 						break;
				case "14": threeDays(); 							break;
				case "15": showDiffPrice(); 					break;
				case "16": archive(); 								break;
				case "17": kConstraints(); 						break;
				case "18": fkConstants(); 						break;
				case "q":
					System.out.println("\nBye.");
					sc.close();
					return;
				default:
					System.out.println("invalid input.");
					break;
			}
		}
	}


	/**
	 * 1. A method that shows empty rooms
	 */
	private static void showEmptyRoom() {
		System.out.println("\tAvailable Hotel");

		String query = 
		"SELECT hotelName, Hotel.cityName, countryName " +
		"FROM Hotel, Country, City " +
		"WHERE Hotel.hotelID IN" +
		"(SELECT hotelID " +
		"FROM Room " + 
		"WHERE roomStatus = 'empty') and " +
		"Hotel.cityName = City.cityName and " +
		"City.countryID = Country.countryID ";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +----------------------------------------------------+");
			System.out.format(" | %19s | %11s | %14s |\n", "Hotel", "City", "Country");
			System.out.println(" +----------------------------------------------------+");
			
			while(rs.next()) {
				System.out.format(" | %19s | %11s | %14s |\n", rs.getString(1), rs.getString(2), rs.getString(3));
			}
			System.out.println(" +----------------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 2. A method that show current reservations
	 */
	private static void showReservations() {
		System.out.println("\tCurrent Reservation");

		String query = 
		"SELECT guestName, hotelName, City.cityName " +
		"FROM Guest, Hotel, City " +
		"WHERE Guest.HotelID = Hotel.HotelID and " +
		"City.cityName = Hotel.cityName";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +------------------------------------+");
			System.out.format(" | %6s | %12s | %10s |\n", "Guest", "Hotel", "City");
			System.out.println(" +------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %6s | %12s | %10s |\n", rs.getString(1), rs.getString(2), rs.getString(3));
			}
			System.out.println(" +------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 3. Show the available hotel with rating over 3
	 */
	private static void availableHotelOverThree() {
		System.out.println("\tAvailable Hotel Over 3 stars");

		String query = 
		"SELECT hotelName, price, City.cityName " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID, City " +
		"WHERE starsRating >= 3 and " +
		"roomStatus = 'empty' and " + 
		"Hotel.cityName = City.cityName";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +-----------------------------------------+");
			System.out.format(" | %18s | %5s | %10s |\n", "Hotel", "Price", "City");
			System.out.println(" +-----------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %18s | %5s | %10s |\n", rs.getString(1), rs.getString(2), rs.getString(3));
			}
			System.out.println(" +-----------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 4. Show the best rating hotel
	 */
	private static void showBestHotel() {
		System.out.println("\tBest Rated Hotel");

		String query = 
		"SELECT distinct hotelName, Hotel.cityName, Country.countryName " +
		"FROM Hotel inner join City on Hotel.cityName = City.cityName " +
		"inner join Country on Country.countryID = City.countryID " +
		"WHERE starsRating >= all (select starsRating from Hotel)";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +---------------------------------+");
			System.out.format(" | %10s | %7s | %8s |\n", "Hotel", "City", "Country");
			System.out.println(" +---------------------------------+");
			while(rs.next()) {
				System.out.format(" | %10s | %7s | %8s |\n", rs.getString(1), rs.getString(2), rs.getString(3));
			}
			System.out.println(" +---------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 5. Show most expensive hotel
	 */
	private static void showExpensive() {
		System.out.println("\tMost expensive hotel");

		String query = 
		"SELECT hotelName, Hotel.cityName, Country.countryName " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " +
		"WHERE price >= all " +
		"(select price from Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on Country.countryID = City.countryID)";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +-------------------------------------------+");
			System.out.format(" | %12s | %10s | %13s |\n", "Hotel", "City", "Country");
			System.out.println(" +-------------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %12s | %10s | %13s |\n", rs.getString(1), rs.getString(2), rs.getString(3));
			}
			System.out.println(" +-------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 6. Show Cheapest hotel
	 */
	private static void showCheapest() {
		System.out.println("\tCheapest hotel");

		String query = 
		"SELECT hotelName, Hotel.cityName, Country.countryName " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " +
		"WHERE price <= all " +
		"(select price from Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on Country.countryID = City.countryID)";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +-----------------------------------+");
			System.out.format(" | %12s | %7s | %8s |\n", "Hotel", "City", "Country");
			System.out.println(" +-----------------------------------+");
			while(rs.next()) {
				System.out.format(" | %10s | %7s | %8s |\n", rs.getString(1), rs.getString(2), rs.getString(3));
			}
			System.out.println(" +-----------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 7. Show worst rating hotel
	 */
	private static void showWorstRating() {
		System.out.println("\tWorst Rating");

		String query = 
		"SELECT hotelName, Room.Price, City.cityName, Country.countryName " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " +
		"WHERE starsRating <= all (select starsRating from Hotel)";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +----------------------------------------------+");
			System.out.format(" | %12s | %7s | %8s | %8s |\n", "Hotel", "Price", "City", "Country");
			System.out.println(" +----------------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %10s | %7s | %8s | %8s |\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}
			System.out.println(" +----------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 8. sorted hotel by highest price
	 */
	private static void sortHighPrice() {
		System.out.println("\tSorted by Highest price");

		String query = 
		"SELECT hotelName, Price, Hotel.cityName, countryName " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " +
		"ORDER BY price desc";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +----------------------------------------------------------+");
			System.out.format(" | %19s | %5s | %10s | %13s |\n", "Hotel", "Price", "City", "Country");
			System.out.println(" +----------------------------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %19s | %5s | %10s | %13s |\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}
			System.out.println(" +----------------------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 9. sorted hotel by lowest price
	 */
	private static void sortLowPrice() {
		System.out.println("\tSorted by Lowest price");

		String query = 
		"SELECT hotelName, Price, Hotel.cityName, countryName " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " +
		"ORDER BY price asc";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +----------------------------------------------------------+");
			System.out.format(" | %19s | %5s | %10s | %13s |\n", "Hotel", "Price", "City", "Country");
			System.out.println(" +----------------------------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %19s | %5s | %10s | %13s |\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}
			System.out.println(" +----------------------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 10. sorted hotel by highest rate
	 */
	private static void sortHighRate() {
		System.out.println("\tSorted by Highest Rating");

		String query = 
		"SELECT hotelName, starsRating, Hotel.cityName, countryName " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " +
		"ORDER BY starsRating desc";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +----------------------------------------------------------+");
			System.out.format(" | %19s | %5s | %10s | %13s |\n", "Hotel", "Stars", "City", "Country");
			System.out.println(" +----------------------------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %19s | %5s | %10s | %13s |\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}
			System.out.println(" +----------------------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 11. sorted hotel by lowest rate
	 */
	private static void sortLowRate() {
		System.out.println("\tSorted by Lowest Rating");

		String query = 
		"SELECT hotelName, starsRating, Hotel.cityName, countryName " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " +
		"ORDER BY starsRating asc";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +----------------------------------------------------------+");
			System.out.format(" | %19s | %5s | %10s | %13s |\n", "Hotel", "Stars", "City", "Country");
			System.out.println(" +----------------------------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %19s | %5s | %10s | %13s |\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}
			System.out.println(" +----------------------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 12. Show hotels < $200 and > 3 stars
	 */
	private static void showGoodValue() {
		System.out.println("\tHotels < $200 and > 3 stars");

		String query = 
		"SELECT hotelName, Hotel.cityName, starsRating, Price " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " +
		"WHERE price <= 200 and starsRating >= 3";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +------------------------------------------------+");
			System.out.format(" | %18s | %9s | %5s | %5s |\n", "Hotel", "City", "Stars", "Price");
			System.out.println(" +------------------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %18s | %9s | %5s | %5s |\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}
			System.out.println(" +------------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 13. Show hotels >= $200 and <= 2 stars
	 */
	private static void showBadValue() {
		System.out.println("\tHotels > $200 or < 2 stars");

		String query = 
		"(SELECT hotelName, Hotel.cityName, starsRating, price " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " +
		"WHERE price >= 200) union all " +
		"(SELECT distinct hotelName, Hotel.cityName, starsRating, price " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID " + 
		"WHERE starsRating <= 2)";


		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +-------------------------------------------------+");
			System.out.format(" | %18s | %10s | %5s | %5s |\n", "Hotel", "City", "Stars", "Price");
			System.out.println(" +-------------------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %18s | %10s | %5s | %5s |\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}
			System.out.println(" +-------------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 14. Show threedays price
	 */
	private static void threeDays() {
		System.out.println("\tPrice if stays 3 days");

		String query = 
		"SELECT distinct hotelName, Hotel.cityName, countryName, Room.roomType, price * 3 " +
		"FROM Hotel inner join Room on Hotel.hotelID = Room.hotelID " +
		"inner join City on City.cityName = Hotel.cityName " +
		"inner join Country on City.countryID = Country.countryID ";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +------------------------------------------------------------------+");
			System.out.format(" | %18s | %10s | %13s | %6s | %5s |\n", "Hotel", "City", "Country", "Type", "Price");
			System.out.println(" +------------------------------------------------------------------+");
			while(rs.next()) {
				System.out.format(" | %18s | %10s | %13s | %6s | %5s |\n", 
					rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5));
			}
			System.out.println(" +------------------------------------------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 15. Show difference highest and lowest price of hotel
	 */
	private static void showDiffPrice() {
		System.out.println(" Hotel with biggest Difference between highest and lowest price");

		String query = 
		"SELECT HID, difference FROM" +
		"(select hotelName as HID, max(price) - min(price) as difference " +
		"from Hotel, Room " +
		"where Hotel.hotelID = Room.hotelID " +
		"group by Hotel.hotelID) DHP " +
		"WHERE difference >= all (select max(price) - min(price) as difference " +
		"from Hotel, Room " +
		"where Hotel.hotelID = Room.hotelID " +
		"group by Hotel.hotelID)";

		try {
			ResultSet rs = jdbcUtils.getResultSet(url, user, password, query);

			System.out.println(" +---------------------------------+");
			System.out.format(" | %18s | %10s |\n", "Hotel", "Difference");
			System.out.println(" +---------------------------------+");
			while(rs.next()) {
				System.out.format(" | %18s | %10s |\n", rs.getString(1), rs.getString(2));
			}
			System.out.println(" +---------------------------------+");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 16. archive data using cut off time entered by a user
	 */
	private static void archive() {
		Scanner sc2 = new Scanner(System.in);
		System.out.println(" Archive - please enter timestamp (YYYY-MM-DD HH:MM:SS)");
		String cutoffdate = sc2.nextLine();
		sc2.close();
		String query1 = "CALL getGuestList('" + cutoffdate + "')";
		String query2 = "SELECT guestID, guestName, updatedAt FROM Guest";
		String query3 = "SELECT guestID, guestName, updatedAt FROM Archive";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();

			ResultSet rs1 = stmt.executeQuery(query2);
			System.out.println("\n Table Guest (OLD)");
			System.out.println(" +------------------------------------+");
			System.out.format(" | %3s | %6s | %19s |\n", "ID", "Name", "Updated At");
			System.out.println(" +------------------------------------+");
			while(rs1.next()) {
				System.out.format(" | %3s | %6s | %19s |\n", rs1.getString(1), rs1.getString(2), rs1.getString(3));
			}
			System.out.println(" +------------------------------------+\n");


			ResultSet rs2 = stmt.executeQuery(query3);
			System.out.println(" Table Archive (OLD)");
			System.out.println(" +------------------------------------+");
			System.out.format(" | %3s | %6s | %19s |\n", "ID", "Name", "Updated At");
			System.out.println(" +------------------------------------+");
			while(rs2.next()) {
				System.out.format(" | %3s | %6s | %19s |\n", rs2.getString(1), rs2.getString(2), rs2.getString(3));
			}
			System.out.println(" +------------------------------------+\n\n");

			ResultSet rs3 = stmt.executeQuery(query1);
			System.out.println(" The procedure getGuestList has been called ("+cutoffdate+")");

			ResultSet rs4 = stmt.executeQuery(query2);
			System.out.println("\n Table Guest (NEW)");
			System.out.println(" +------------------------------------+");
			System.out.format(" | %3s | %6s | %19s |\n", "ID", "Name", "Updated At");
			System.out.println(" +------------------------------------+");
			while(rs4.next()) {
				System.out.format(" | %3s | %6s | %19s |\n", rs4.getString(1), rs4.getString(2), rs4.getString(3));
			}
			System.out.println(" +------------------------------------+\n");


			ResultSet rs5 = stmt.executeQuery(query3);
			System.out.println(" Table Archive (NEW)");
			System.out.println(" +------------------------------------+");
			System.out.format(" | %3s | %6s | %19s |\n", "ID", "Name", "Updated At");
			System.out.println(" +------------------------------------+");
			while(rs5.next()) {
				System.out.format(" | %3s | %6s | %19s |\n", rs5.getString(1), rs5.getString(2), rs5.getString(3));
			}
			System.out.println(" +------------------------------------+\n\n");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 17. key constraints
	 */
	private static void kConstraints() {
		System.out.println("Key Constraints");
		System.out.println("Executed Query: INSERT INTO Hotel Values(1, 'Hyatt', 'Shanghai', 5)");
		System.out.println("Error msg from mysql: ERROR 1062 (23000): Duplicate entry '1' for key 'hotel.PRIMARY'");

		String query = "INSERT INTO Hotel Values(1, 'Hyatt', 'Santa Clara', 5)";

		try {
			jdbcUtils.getResultSet(url, user, password, query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



		/**
	 * 18. foreign key constraints
	 */
	private static void fkConstants() {
		System.out.println("Foreign Key Constraints");
		System.out.println("Executed Query: INSERT INTO Hotel Values(15, 'Hyatt', 'Santa Clara', 5)");
		System.out.println("Error msg from mysql: ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails (`hotel_reservation`.`hotel`, CONSTRAINT `hotel_ibfk_1` FOREIGN KEY (`cityName`) REFERENCES `city` (`cityName`))");

		String query = "INSERT INTO Hotel Values(15, 'Hyatt', 'Santa Clara', 5)";

		try {
			jdbcUtils.getResultSet(url, user, password, query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
