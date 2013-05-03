package websiteschema.mpsegment.web.api.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "PartOfSpeeches"
)
public class PartOfSpeech {
    @Id
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Note")
    private String note;

    @Column(name = "CreateAt")
    private Date createAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateAt() {
        return createAt;
    }
}
