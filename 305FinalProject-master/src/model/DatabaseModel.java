package model;

import controller.SQLiteConnection;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;

public class DatabaseModel {
    Connection connection = null;

    public DatabaseModel() {
        connection = SQLiteConnection.Connector();
        if( connection == null){
            System.out.println("Connection not successful...");
            System.exit(1);
        }
    }

    public boolean isDatabaseConnected(){
        try{
            return !connection.isClosed();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList getMovies(String name, String searchBy) throws SQLException {
        String searchCriteria = null;
        ArrayList movieIds = new ArrayList();
        int actorFlag = 0;
        int directorFlag = 0;
        int producerFlag = 0;
        //Setup switch case to convert searchBy to SQL variable name
        switch(searchBy){
            case "Name": searchCriteria = "movieName";
                break;
            case "Genre": searchCriteria = "genre";
                break;
            case "Release Date": searchCriteria = "releaseDate";
                break;
            case "Rating": searchCriteria = "rating";
                break;
            case "Actor": actorFlag = 1;
                break;
            case "Director": directorFlag = 1;
                break;
            case "Producer": producerFlag = 1;
                break;
        }
        ArrayList<Movie> movies = new ArrayList<>();
        int id;
        String movieName;
        String genre;
        String releaseDate;
        double rating;
        int duration;
        PreparedStatement statement;
        ResultSet set;
        String query = null;
        if(actorFlag == 1 | directorFlag == 1 | producerFlag == 1){
            //First get actor ID, then go through castee table to get all the movie ID's, then search by movie ID
            String[] nameArray = name.split(" ");
            String firstName = nameArray[0];
            String lastName = nameArray[1];

            query = "SELECT ID from person where firstName = ? and lastName = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1,firstName);
            statement.setString(2,lastName);
            set = statement.executeQuery();
            //Should only be 1 person since ID is unique.
            int personId = -1;
            while(set.next()) {
                personId = set.getInt(1);
            }
            String table;
            String idType;
            if(actorFlag == 1){
                table = "casted";
                idType = "casteeID";
            }else if(directorFlag == 1){
                table = "directed";
                idType = "directorID";
            }else{
                table = "produced";
                idType = "producerID";
            }
            query = "SELECT movieID from "+table +" where "+idType+" = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1,personId);
            set = statement.executeQuery();
            while(set.next()) {
                movieIds.add(set.getInt(1));
            }
            searchCriteria = "movieID";

        }

        query = "SELECT * from Movie where " + searchCriteria + " = ?";
        statement = connection.prepareStatement(query);
        if(actorFlag == 1 | directorFlag == 1 | producerFlag == 1){
            if (movieIds.size() == 0){
                return movies; //Return empty list
            }
            query = "SELECT * from Movie where movieID = ?";
            for(int i = 1; i < movieIds.size(); i++){
                query = query.concat(" OR movieID = ?");
            }
            statement = connection.prepareStatement(query);
            for(int i = 0; i < movieIds.size(); i++) {
                statement.setInt(i + 1, (Integer) movieIds.get(i));
            }
        }else {
            statement.setString(1, name);
        }
        set = statement.executeQuery();

        while(set.next()){
            id = set.getInt(1);
            genre = set.getString(2);
            movieName = set.getString(3);
            releaseDate = set.getString(4);
            rating = set.getDouble(5);
            duration = set.getInt(6);
            Movie movie = new Movie(id,movieName,genre,releaseDate,rating,duration);
            movies.add(movie);
        }
        statement.close();
        set.close();
        connection.close();
        return movies;
    }

    public ArrayList<Movie> getMoviesByButtons(int action) throws SQLException {
        int id;
        String movieName;
        String genre;
        String releaseDate;
        double rating;
        int duration;
        PreparedStatement statement;
        ResultSet set;
        String query = null;
        String selectedAction = null;
        ArrayList<Movie> movies = new ArrayList<>();
        if(action == 1){
            selectedAction = "rating >= 9";
        }

        query = "SELECT * FROM Movie WHERE " + selectedAction;
        if(action == 2){
            query = "SELECT * FROM Movie";
        }

        if (action == 3) {
            query = "SELECT * FROM Movie WHERE duration > 150";
        }
        statement = connection.prepareStatement(query);
        set = statement.executeQuery();

        while(set.next()){
            id = set.getInt(1);
            genre = set.getString(2);
            movieName = set.getString(3);
            releaseDate = set.getString(4);
            rating = set.getDouble(5);
            duration = set.getInt(6);
            Movie movie = new Movie(id,movieName,genre,releaseDate,rating,duration);
            movies.add(movie);
        }
        statement.close();
        set.close();
        connection.close();
        return movies;
    }
    public ArrayList<Person> getAdditionalInfo(int movieID, String type) throws SQLException {
        ArrayList<Person> cast = new ArrayList<>();
        PreparedStatement statement;
        ResultSet set;
        String query = null;
        query = "SELECT * FROM " +type + " WHERE movieID = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1,movieID);
        set = statement.executeQuery();

        ArrayList<Integer> ids = new ArrayList<>();

        while(set.next()){
            ids.add(set.getInt(3)); //Gets the ID of the person...
        }
        if(type.equals("distributed")){
            query = "SELECT * FROM distributor WHERE ID = ?";
        }else
        query = "SELECT * FROM person WHERE ID = ?";
        for(int k = 1; k < ids.size(); k++){
            query = query.concat(" OR ID = ?");
        }
        statement = connection.prepareStatement(query);
        for(int k = 0; k < ids.size(); k++) {
            statement.setInt(k + 1, (Integer) ids.get(k));
        }

        set = statement.executeQuery();

        while(set.next()){
            Person person = new Person();
            if(type.equals("distributed")){
                person.setFirstName(set.getString(2)); //First name == distributor name
                person.setLastName(set.getString(3));//Last name == location
            }else {
                person.setFirstName(set.getString(2));
                person.setLastName(set.getString(3));
                person.setAge(set.getInt(4));
                person.setProfession(set.getString(5));
            }
            cast.add(person);

        }
        statement.close();
        set.close();
        connection.close();



        return cast;
    }
    /*
        Used to restart connection
        Must close and restart connection for each transaction.
     */
    public void restartConnect(){
        connection = SQLiteConnection.Connector();
        if (connection == null){
            System.out.println("Connection not successful");
            System.exit(1);
        }
    }

    public ArrayList<String> getAwards(String firstName, String lastName) throws SQLException {
        ArrayList<String> awards = new ArrayList<>();
        PreparedStatement statement;
        ResultSet set;
        String query = null;
        query = "SELECT ID FROM person WHERE firstName = ? AND lastName = ?";
        statement = connection.prepareStatement(query);
        statement.setString(1,firstName);
        statement.setString(2,lastName);
        set = statement.executeQuery();
        int id = 0;
        while(set.next()){
            id = set.getInt(1);
        }
        query = "SELECT * FROM awards WHERE awardeeID = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        set = statement.executeQuery();
        while(set.next()){
            awards.add(set.getString(2));
        }
        return awards;
    }
}
