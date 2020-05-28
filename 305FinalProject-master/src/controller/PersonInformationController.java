package controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import model.DatabaseModel;
import model.Movie;
import model.Person;

import java.sql.SQLException;
import java.util.ArrayList;

public class PersonInformationController {
    @FXML
    private Text moviesText;

    @FXML
    private Text ageText;

    @FXML
    private Text professionText;

    @FXML
    private Text nameText;

    @FXML
    private Text awardsText;

    DatabaseModel databaseModel = new DatabaseModel();


    void setAllText(Person person) throws SQLException {
        nameText.setText((person.getFirstName() + " " + person.getLastName()));
        ageText.setText(ageText.getText().concat(" " + person.getAge()));
        professionText.setText(professionText.getText().concat(" " +person.getProfession()));
        String fullName = person.getFirstName() + " " + person.getLastName();
        String profession = person.getProfession();
        ArrayList<Movie> movies = databaseModel.getMovies(fullName, profession);
        databaseModel.restartConnect();
        for(int i = 0; i < movies.size(); i++){
            moviesText.setText(moviesText.getText().concat(movies.get(i).getMovieName()));
            if(i != movies.size() - 1){
                moviesText.setText(moviesText.getText().concat(", "));
            }
        }
        ArrayList<String> awards = databaseModel.getAwards(person.getFirstName(),person.getLastName());
        if(awards.size() == 0){
            awardsText.setText(awardsText.getText().concat("No awards added"));
        }
        for(int i = 0; i < awards.size(); i++){

            awardsText.setText(awardsText.getText().concat(awards.get(i)));
            if (i != awards.size() - 1){
                moviesText.setText(moviesText.getText().concat(", "));
            }
        }

    }
}
