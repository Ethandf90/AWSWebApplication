package ece1779.ec2.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ece1779.ec2.mode.userMode;

public class UserService {

	public boolean authenticate(userMode user){
		boolean isAuthenticated = false;
		Connection con = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try{
			String sql = "SELECT COUNT(*) FROM users WHERE login = ? AND password = ?"; 
			con = ConnectionPool.getInstance().getConnection();
			ptmt = con.prepareStatement(sql);
            ptmt.setString(1, user.getLogin());
            ptmt.setString(2, user.getPassword());
            rs = ptmt.executeQuery();
            if(rs.next()) {
            	int count = rs.getInt(1);
            	if(count > 0)
            		isAuthenticated = true;
            }
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally{
			try{
				con.close();
				ptmt.close();
				rs.close();
			}
			catch (SQLException e) {
                e.printStackTrace();
			} catch (Exception e) {
                e.printStackTrace();
			}
		}
		
		return isAuthenticated;
	}
	
	public boolean createAccount(userMode user){
		boolean isUserExisting = false;
		Connection con = null;
		PreparedStatement ptmt = null;
		try{
			con = ConnectionPool.getInstance().getConnection();
            String sql = "insert into users (login, password) "+
                      "value(?,?)";
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1,user.getLogin());
            ptmt.setString(2,user.getPassword());
            
            ptmt.executeUpdate();
            
		}
		catch(SQLException e){
			isUserExisting = true;
			e.printStackTrace();
		}
		finally {
			try {
				ptmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return isUserExisting;
	}
	
	public userMode getUser(String login){
		Connection con = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try{
			con = ConnectionPool.getInstance().getConnection();
			String sql = "SELECT * FROM users WHERE login = ?";
			ptmt = con.prepareStatement(sql);
            ptmt.setString(1,login);
            rs = ptmt.executeQuery();
            if(rs.next()){
            	userMode user = new userMode();
            	user.setId(rs.getInt(1));
            	user.setLogin(rs.getString(2));
            	user.setPassword(rs.getString(3));
            	return user;
            }
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				con.close();
				ptmt.close();
				rs.close();
			}
			catch (SQLException e) {
			     e.printStackTrace();
			} catch (Exception e) {
			     e.printStackTrace();
			}
		}
		return null;
	}
}
