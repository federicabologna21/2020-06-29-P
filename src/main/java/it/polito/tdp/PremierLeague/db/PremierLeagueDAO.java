package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllMatches(Map<Integer, Match> idMap){
		String sql = "SELECT m.MatchID AS id, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		// List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(!idMap.containsKey(res.getInt("id"))) {
					Match match = new Match(res.getInt("id"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
					idMap.put(match.getMatchID(), match);
				}
				
				
			

			}
			conn.close();
		//  	return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
		//	System.out.println("Errore connessione al db!");
			// throw new RuntimeException("Error connection database");
			
		// 	return null;
		}
	}
	
	public List<Match> getVertici(int mese, Map<Integer, Match>idMap){
		String sql = "SELECT DISTINCT m.MatchID AS id "
				+ "FROM matches m "
				+ "WHERE MONTH(m.Date) =?";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(idMap.get(res.getInt("id")));
				
			

			}
			conn.close();
		return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
		
			return null;
		}
		
	}
	
	public List<Adiacenza> getAdiacenze(int min, Map<Integer, Match>idMap){
		String sql="SELECT a1.MatchID AS m1, a2.MatchID AS m2, COUNT(a1.PlayerID) AS peso "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.PlayerID = a2.PlayerID AND a1.TimePlayed >=? "
				+ "AND a2.TimePlayed>=? AND a1.MatchID > a2.MatchID "
				+ "GROUP BY a1.MatchID, a2.MatchID";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, min);
			st.setInt(2, min);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Match m1 = idMap.get(res.getInt("m1"));
				Match m2 = idMap.get(res.getInt("m2"));
				
				if (m1 != null && m2!=null) {
					if (res.getDouble("peso")>0) {
					Adiacenza a = new Adiacenza(m1,m2,res.getDouble("peso"));
					
					result.add(a);
					}
				}
			

			}
			conn.close();
		return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
		
			return null;
		}
		
	}
	
}
