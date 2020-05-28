package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.DatabaseModel;
import model.Movie;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @FXML
    private TableColumn<?, ?> ratingColumn;

    @FXML
    private Button searchButton;

    @FXML
    private Button longestButton;

    @FXML
    private Button ratedButton;

    @FXML
    private Button allButton;

    @FXML
    private ComboBox<?> searchComboBox;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Text titleText;

    @FXML
    private TableColumn<?, ?> dateColumn;


    @FXML
    private TableColumn<?, ?> genreColumn;

    @FXML
    private TableColumn<?, ?> buttonColumn;

    @FXML
    private TableView<Movie> movieTable;

    DatabaseModel databaseModel = new DatabaseModel();
    /*
        Initalize Combo Box options
     */
    @FXML
    void addToTable(ActionEvent event) throws SQLException {
        String name = searchField.getText();
        String searchBy = (String) searchComboBox.getValue();
        if(searchBy == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a search criteria");
            alert.setHeaderText("Error");
            alert.showAndWait();
        }else {
            List movies = databaseModel.getMovies(name,searchBy);
            databaseModel.restartConnect();
            ObservableList data = FXCollections.observableList(movies);
            movieTable.setItems(data);
            nameColumn.setCellValueFactory(new PropertyValueFactory("movieName"));
            genreColumn.setCellValueFactory(new PropertyValueFactory("genre"));
            dateColumn.setCellValueFactory(new PropertyValueFactory("releaseDate"));
            ratingColumn.setCellValueFactory(new PropertyValueFactory("rating"));
            buttonColumn.setCellValueFactory(new PropertyValueFactory("button"));
            setMoreInfoOnAction((ArrayList<Movie>) movies);
        }

    }
    @FXML
    void ratedButtonEvent(ActionEvent event) throws SQLException {
        List movies = databaseModel.getMoviesByButtons(1);
        databaseModel.restartConnect();
        ObservableList data = FXCollections.observableList(movies);
        movieTable.setItems(data);
        nameColumn.setCellValueFactory(new PropertyValueFactory("movieName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory("genre"));
        dateColumn.setCellValueFactory(new PropertyValueFactory("releaseDate"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory("rating"));
        buttonColumn.setCellValueFactory(new PropertyValueFactory("button"));
        setMoreInfoOnAction((ArrayList<Movie>) movies);

    }
    @FXML
    void allButtonEvent(ActionEvent event) throws SQLException {
        List movies = databaseModel.getMoviesByButtons(2);
        databaseModel.restartConnect();
        ObservableList data = FXCollections.observableList(movies);
        movieTable.setItems(data);
        nameColumn.setCellValueFactory(new PropertyValueFactory("movieName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory("genre"));
        dateColumn.setCellValueFactory(new PropertyValueFactory("releaseDate"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory("rating"));
        buttonColumn.setCellValueFactory(new PropertyValueFactory("button"));
        setMoreInfoOnAction((ArrayList<Movie>) movies);

    }
    @FXML
    void longestButtonEvent(ActionEvent event) throws SQLException {
        List movies = databaseModel.getMoviesByButtons(3);
        databaseModel.restartConnect();
        ObservableList data = FXCollections.observableList(movies);
        movieTable.setItems(data);
        nameColumn.setCellValueFactory(new PropertyValueFactory("movieName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory("genre"));
        dateColumn.setCellValueFactory(new PropertyValueFactory("releaseDate"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory("rating"));
        buttonColumn.setCellValueFactory(new PropertyValueFactory("button"));
        setMoreInfoOnAction((ArrayList<Movie>) movies);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List list = new ArrayList<String>();
        list.add("Name");
        list.add("Genre");
        list.add("Release Date");
        list.add("Rating");
        list.add("Actor");
        list.add("Director");
        searchComboBox.setItems(FXCollections.observableList(list));
        searchField.setStyle("-fx-text-inner-color: #FFFFFF; -fx-font-size: 14; -fx-background-color:  #2a4170");
    }

    void setMoreInfoOnAction(ArrayList<Movie> movies){
        for(int i = 0; i < movies.size(); i++){
            Movie movie = movies.get(i);
            movies.get(i).getButton().setOnAction(e->{
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MoreInfoView.fxml"));
                    Parent root = loader.load();
                    Stage newStage = new Stage();
                    newStage.setTitle("Additional Information");
                    newStage.setScene(new Scene(root,600,400));

                    MoreInfoController infoController = loader.getController();
                    infoController.setAllText(movie);
                    newStage.show();
                } catch (IOException | SQLException e1) {
                    e1.printStackTrace();
                }
            });
        }
    }
}

