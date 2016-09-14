package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RankingRule.
 */
@Entity
@Table(name = "ranking_rule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RankingRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "win")
    private Integer win;

    @Column(name = "loss")
    private Integer loss;

    @Column(name = "draw")
    private Integer draw;

    @Column(name = "score_for")
    private Integer scoreFor;

    @Column(name = "score_again")
    private Integer scoreAgain;

    @ManyToOne
    private Dict rankedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public Integer getLoss() {
        return loss;
    }

    public void setLoss(Integer loss) {
        this.loss = loss;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getScoreFor() {
        return scoreFor;
    }

    public void setScoreFor(Integer scoreFor) {
        this.scoreFor = scoreFor;
    }

    public Integer getScoreAgain() {
        return scoreAgain;
    }

    public void setScoreAgain(Integer scoreAgain) {
        this.scoreAgain = scoreAgain;
    }

    public Dict getRankedOn() {
        return rankedOn;
    }

    public void setRankedOn(Dict dict) {
        this.rankedOn = dict;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RankingRule rankingRule = (RankingRule) o;
        if(rankingRule.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rankingRule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RankingRule{" +
            "id=" + id +
            ", win='" + win + "'" +
            ", loss='" + loss + "'" +
            ", draw='" + draw + "'" +
            ", scoreFor='" + scoreFor + "'" +
            ", scoreAgain='" + scoreAgain + "'" +
            '}';
    }
}
