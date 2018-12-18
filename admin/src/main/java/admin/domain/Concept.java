package admin.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@JsonPropertyOrder(value = { "id", "word", "definition", "score"})
public class Concept {
    @Id
    @GeneratedValue
    private Long id;
    private String word;
    private String definition;
    private Integer score;

    @Override
    public String toString() {
        return "Concept{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", definition='" + definition + '\'' +
                ", score=" + score +
                '}';
    }

    public Concept() {
    }

    public Concept(Long id, String word, String definition, Integer score) {
        this.id = id;
        this.word = word;
        this.definition = definition;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public Integer getScore() {
        return score;
    }
}
