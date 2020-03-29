package com.revature.repository;

import java.util.List;
import com.revature.model.Account;

public interface DaoContract<T, I> {
  
  List<T> findAll();
  T findById(I id);
  T findByDouble(double d);
  T findByString(String s);
  T insert(T t);
  boolean updateFunds(T t, Double d);
  boolean delete(T t);
  boolean updateEmployeeStatus(Account t, boolean b);

}
