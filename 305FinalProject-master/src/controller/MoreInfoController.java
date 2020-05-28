package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.DatabaseModel;
import model.Movie;
import model.Person;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class MoreInfoController {

    @FXML
    private Text directorsText;

    @FXML
    private Text durationText;

    @FXML
    private Text producersText;

    @FXML
    private Text castText;

    @FXML
    private Text releasedText;

    @FXML
    private Text ratingText;

    @FXML
    private Text nameText;

    @FXML
    private Text genreText;

    @FXML
    private Text distributorsText;

    @FXML
    private HBox castBox;

    @FXML
    private HBox directorBox;

    @FXML
    private HBox producerBox;

    DatabaseModel databaseModel = new DatabaseModel();

    @FXML
    void closeWindow(ActionEvent event){
        Node  source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }


    void setAllText(Movie movie) throws SQLException {
        nameText.setText(movie.getMovieName());
        releasedText.setText(releasedText.getText().concat(" " + movie.getReleaseDate()));
        genreText.setText(genreText.getText().concat(" " + movie.getGenre()));
        ratingText.setText(ratingText.getText().concat(" " + movie.getRating()));
        int hours;
        int minutes;
        hours = movie.getDuration() / 60;
        minutes = movie.getDuration() % 60;
        String hour;
        if(hours == 1){
            hour = "hour";
        }else{
            hour = "hours";
        }
        durationText.setText(durationText.getText().concat(" " +hours + " "+ hour + " " + minutes + " minutes"));
        ArrayList<Person> cast = databaseModel.getAdditionalInfo(movie.getId(), "casted");
        databaseModel.restartConnect();
        setPeople(cast, castText);
        ArrayList<Person> directors = databaseModel.getAdditionalInfo(movie.getId(), "directed");
        databaseModel.restartConnect();
        setPeople(directors, directorsText);
        ArrayList<Person> producers = databaseModel.getAdditionalInfo(movie.getId(), "produced");
        databaseModel.restartConnect();
        setPeople(producers,producersText);
        ArrayList<Person> distributors = databaseModel.getAdditionalInfo(movie.getId(), "distributed");
        databaseModel.restartConnect();
        setPeople(distributors,distributorsText);





    }

    private void setPeople(ArrayList<Person> people, Text text) {
        for(int i = 0; i < people.size(); i++){
            Person person = people.get(i);
            if(text.getText().equals("Distributors : ")){
                text.setText(text.getText().concat(" " +person.getFirstName() + " (" + person.getLastName() + ")"));
                if(i != people.size() - 1){
                    text.setText(text.getText().concat(","));
                }
            }else
            {
                Hyperlink link = new Hyperlink(person.getFirstName() + " " + person.getLastName());
                link.setFont(new Font(21));
                link.setOnAction(e->{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PersonInformationView.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Stage newStage = new Stage();
                    newStage.setTitle("Additional Information");
                    newStage.setScene(new Scene(root,600,400));

                    PersonInformationController controller = loader.getController();
                    try {
                        controller.setAllText(person);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    newStage.show();
                });
                if(text.getText().equals("Cast : ")) {
                    castBox.getChildren().add(link);
                }
                if(text.getText().equals("Producers : ")){
                    producerBox.getChildren().add(link);
                }
                if(text.getText().equals("Directors : ")){
                    directorBox.getChildren().add(link);
                }
            }

        }
    }

}
