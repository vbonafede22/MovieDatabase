/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  vinbonafede1
 * Created: Oct 19, 2018
 */

CREATE TABLE movie(
    movieId INTEGER NOT NULL,
    genre VARCHAR(n),
    movieName VARCHAR(n),
    releaseDate DATE,
    directorName VARCHAR(n),    
    actorName VARCHAR(n),      
    producerName VARCHAR(n),    
    languages VARCHAR(n),
    rating DECIMAL(4,2),
    distributerName VARCHAR(n), 
    duration TIME,
    PRIMARYKEY(movieId)
)

CREATE TABLE director(
    directorId INTEGER NOT NULL,
    directorName VARCHAR(n),
    directorAge INTEGER,
    directorAward VARCHAR(n),
    PRIMARYKEY(directorId)
)

CREATE TABLE directedBy(
    directorId INTEGER NOT NULL,
    movieId INTEGER NOT NULL,
    FORGIENKEY(directorId) REFERENCES director(directorId),
    FORGIENKEY(movieId) REFERENCES movie(movieId)
)

CREATE TABLE actor(
    actorId INTEGER NOT NULL,
    actorName VARCHAR(n),
    actorAge INTEGER,
    actorAwards VARCHAR(n),
    PRIMARYKEY(actorId)
)

CREATE TABLE actorIn(
    actorId INTEGER NOT NULL,
    movieId INTEGER NOT NULL,
    FORGIENKEY(actorId) REFERENCES actor(actorId),
    FORGIENKEY(movieId) REFERENCES movie(movieId)
)

CREATE TABLE producer(
    producerId INTEGER NOT NULL,
    producerName VARCHAR(n),
    PRIMARYKEY(producerId)
)

CREATE TABLE producedBy(
    producerId INTEGER NOT NULL,
    movieId INTEGER NOT NULL,
    FORGIENKEY(producerId) REFERENCES producer(producerId),
    FORGIENKEY(movieId) REFERENCES movie(movieId)
)

CREATE TABLE distributer(
    distributerId INTEGER NOT NULL,
    distributerName VARCHAR(n),
    distributerLocation VARCHAR(n),
    PRIMARYKEY(distributerId)
)

CREATE TABLE distributedBy(
    distributerId INTEGER NOT NULL,
    movieId INTEGER NOT NULL,
    FORGIENKEY(distributerId) REFERENCES distributer(distributerId),
    FORGIENKEY(movieId) REFERENCES movie(movieId)
)