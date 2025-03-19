package model;

import repository.HasId;

public class Race implements HasId<Integer> {
    private int raceId;
    private int distance;
    private String style;
    private int totalParticipants;


    public Race(int raceId, int distance, String style, int totalParticipants) {
        this.raceId = raceId;
        this.distance = distance;
        this.style = style;
        this.totalParticipants = totalParticipants;
    }



    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public int getDistance(){
        return distance;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public String getStyle(){
        return style;
    }
    public void setStyle(String style){
        this.style = style;
    }
    public int getTotalParticipants(){
        return totalParticipants;
    }
    public void setTotalParticipants(int totalParticipants){
        this.totalParticipants = totalParticipants;
    }

    @Override
    public Participants getId() {
        return null;
    }

    @Override
    public void setId(Integer integer) {
        this.setRaceId(integer);
    }

    public void setId(Race race) {
        race.setRaceId(raceId);
    }
}
