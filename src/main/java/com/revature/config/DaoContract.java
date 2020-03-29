package com.revature.config;

import java.util.List;

public interface DaoContract<T, I> {
  
  List<T> findAll();
  T findById(I id);
  T findByDouble(double d);
  T findByString(String s);
  T insert(T t);
  boolean updateFunds(T t, Double d);
  boolean delete(T t);

}
