package repository;

import model.Participants;

public interface HasId<ID> {
    Participants getId();
    void setId(ID id);
}
