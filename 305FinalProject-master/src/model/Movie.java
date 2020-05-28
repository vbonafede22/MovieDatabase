package model;

import javafx.scene.control.Button;

import java.time.Duration;

public class Movie {
    private int id;
    private String movieName;
    private String genre;
    private String releaseDate;
    private double rating;
    private int duration;
    private Button button = new Button("More info");

    public Movie(int id, String movieName, String genre, String releaseDate, double rating, int duration) {
        this.id = id;
        this.movieName = movieName;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.duration = duration;
        button.setStyle("-fx-background-color: #2a4170; -fx-text-fill: #FFFFFF");
    }
    public Button getButton(){
        return button;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", movieName='" + movieName + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", rating=" + rating +
                ", duration=" + duration +
                ", button=" + button +
                '}';
    }
}
