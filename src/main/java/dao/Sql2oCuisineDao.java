package dao;

import models.Cuisine;
import models.Restaurant;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;


public class Sql2oCuisineDao implements CuisineDao{
    private final Sql2o sql2o;

    public Sql2oCuisineDao(Sql2o sql2o){
        this.sql2o = sql2o;

    }
    @Override
    public void add(Cuisine cuisine) {
        String sql = "INSERT INTO cuisines (type) VALUES (:type)";
        try(Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .addParameter("type", cuisine.getType())
                    .addColumnMapping("TYPE", "type")
                    .executeUpdate()
                    .getKey();
            cuisine.setId(id);
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
    @Override
    public Cuisine findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM cuisines WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Cuisine.class);
        }

    }
    @Override
    public List<Cuisine> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM cuisines")
                    .executeAndFetch(Cuisine.class);
        }
    }
    @Override
    public void update(int id, String newType){
        String sql = "UPDATE cuisines SET (type) = (:type) WHERE id = :id";
        try(Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("type", newType)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void deleteCuisineById(int id) {
        String sql = "DELETE from cuisines WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
    @Override
    public void clearAllCuisines() {
        String sql = "DELETE from cuisines";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
    @Override
    public List<Restaurant> getAllRestaurantsByCuisine(int cuisineId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM restaurants WHERE cuisineId = :cuisineId")
                    .addParameter("cuisineId", cuisineId)//raw sql
                    .executeAndFetch(Restaurant.class); //fetch a list
        }
    }
}
