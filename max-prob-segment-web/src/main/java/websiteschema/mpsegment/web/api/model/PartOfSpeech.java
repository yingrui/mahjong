package websiteschema.mpsegment.web.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(
        name = "PartOfSpeeches"
)
public class PartOfSpeech {
    @Id
    private int id;

    @Column(name = "PartOfSpeech")
    private String partOfSpeech;

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

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
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
