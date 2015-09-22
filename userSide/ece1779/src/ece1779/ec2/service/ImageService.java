package ece1779.ec2.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ece1779.ec2.mode.imageMode;

public class ImageService {

	Connection con = null;
	PreparedStatement ptmt = null;
	ResultSet rs = null;
	
	public void addImageToSQL(imageMode image){
		try{
			con = ConnectionPool.getInstance().getConnection();
            String sql = "insert into images (userId, key1,key2,key3,key4) VALUES(?,?,?,?,?)";
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, image.getUserId());
            ptmt.setString(2,image.getKey1());
            ptmt.setString(3,image.getKey2());
            ptmt.setString(4,image.getKey3());
            ptmt.setString(5,image.getKey4());
            
            ptmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				con.close();
				ptmt.close();
			}
			catch (SQLException e) {
			     e.printStackTrace();
			} catch (Exception e) {
			     e.printStackTrace();
			}
		}
	}
	
	public List<imageMode> getUserImage(int userId){
		try{
			con = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM images WHERE userId = ?";
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, userId);
          
            rs = ptmt.executeQuery();
            List<imageMode> images = new ArrayList<imageMode>();
            while(rs.next()){
            	imageMode image = getImageFromRS(rs);
            	images.add(image);
            }
            return images;
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
	
	private imageMode getImageFromRS(ResultSet rs) throws SQLException{
		imageMode image = new imageMode();
		image.setId(rs.getInt(1));
		image.setUserId(rs.getInt(2));
		image.setKey1(rs.getString(3));
		image.setKey2(rs.getString(4));
		image.setKey3(rs.getString(5));
		image.setKey4(rs.getString(6));
		return image; 
	}
	
	public imageMode getImage(String key1){
		try{
			con = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM images WHERE key1 = ?";
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, key1);
            rs = ptmt.executeQuery();
            if(rs.next()){
            	imageMode image = getImageFromRS(rs);
            	return image;
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
