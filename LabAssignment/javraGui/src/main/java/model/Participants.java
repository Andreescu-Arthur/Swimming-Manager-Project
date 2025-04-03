package model;

import repository.HasId;

public class Participants implements HasId<Integer> {
    private int participantId;
    private int age;
    private String name;

    public Participants(int age, String name)
    {
        this.age = age;
        this.name = name;
    }

    public Participants(int participantId, String name, int age) {
        this.participantId = participantId;
        this.name = name;
        this.age = age;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public Participants getId() {
        return null;
    }

    @Override
    public void setId(Integer integer) {

    }


    public void setId(Participants participants) {
        participants.setParticipantId(participantId);
    }
}
