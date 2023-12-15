package dao;

import connection.Connect;
import model.PresentNewsArticle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PresentNewsArticleDAO {

    Connection conn ;
    PreparedStatement ps;
    ResultSet rs;

    public List<PresentNewsArticle> getAllNews(){
        List<PresentNewsArticle> list = new ArrayList<>();
        String query = "SELECT * FROM PresentNewsArticles";
        try{
            conn = new Connect().getconnecttion();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                list.add(new PresentNewsArticle(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getDate(8),
                        rs.getString(9)));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public PresentNewsArticle getNewsById(String id){
        String query = "SELECT * FROM PresentNewsArticles WHERE id=?";
        try{
            conn = new Connect().getconnecttion();
            ps = conn.prepareStatement(query);
            ps.setString(1, id);
            rs = ps.executeQuery();
            while(rs.next()){
                return new PresentNewsArticle(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getDate(8),
                        rs.getString(9));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> loadCategory(){
        String query = "SELECT category FROM PresentNewsArticles GROUP BY category";
        try{
            conn = new Connect().getconnecttion();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            ArrayList<String> list = new ArrayList<>();
            while(rs.next()){
                list.add(rs.getString(1));
            }
            return list;
        } catch(Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        PresentNewsArticleDAO dao = new PresentNewsArticleDAO();
        List<PresentNewsArticle> news = dao.getAllNews();
        System.out.println(news);
    }
}
