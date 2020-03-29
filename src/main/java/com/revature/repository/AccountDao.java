package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.revature.model.Account;

public class AccountDao implements DaoContract<Account, Integer> {

  @Override
  public List<Account> findAll() {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "select * from accounts order by id asc";
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      List<Account> list = new ArrayList<>();
      while (rs.next()) {
        list.add(new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
            rs.getDouble(5), rs.getBoolean(6)));
      }
      return list;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Account findById(Integer id) {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "select * from accounts where id = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      rs.next();
      return new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
          rs.getDouble(5), rs.getBoolean(6));
    } catch (SQLException e) {
      System.out.println("ID does not exist.");
    }
    return null;
  }

  @Override
  public Account findByDouble(double d) {
    // NOT USED
    return null;
  }

  @Override
  public Account findByString(String s) {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "select * from accounts where username = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, s);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        return new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
            rs.getDouble(5), rs.getBoolean(6));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Account insert(Account t) {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "insert into accounts (username, password, name) values (?,?,?)";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, t.getUsername());
      ps.setString(2, t.getPassword());
      ps.setString(3, t.getName());
      ps.execute();
      return findByString(t.getUsername());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean delete(Account t) {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "delete from accounts where username = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, t.getUsername());
      ps.execute();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean updateFunds(Account t, Double d) {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "update accounts set funds = ? where username = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setDouble(1, d);
      ps.setString(2, t.getUsername());
      ps.execute();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean updateEmployeeStatus(Account t, boolean b) {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "update accounts set isEmployee = ? where username = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setBoolean(1, b);
      ps.setString(2, t.getUsername());
      ps.execute();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
