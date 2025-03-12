package model;

public class Race {
    private int distance;
    private String style;
    private int totalParticipants;


    public Race(int distance, String style, int totalParticipants) {
        this.distance = distance;
        this.style = style;
        this.totalParticipants = totalParticipants;
    }

    int getDistance(){
        return distance;
    }

    void setDistance(int distance){
        this.distance = distance;
    }

    String getStyle(){
        return style;
    }
    void setStyle(String style){
        this.style = style;
    }
    int getTotalParticipants(){
        return totalParticipants;
    }
    void setTotalParticipants(int totalParticipants){
        this.totalParticipants = totalParticipants;
    }

}
