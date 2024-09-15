package com.example.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "stack", schema = "voting_system", catalog = "")
public class StackEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "decision_number")
    private String decisionNumber;
    @Basic
    @Column(name = "decision_date")
    private Date decisionDate;
    @Basic
    @Column(name = "request_date")
    private Date requestDate;
    @OneToMany(mappedBy = "stack")
    private List<ObserverEntity> observers;
    @ManyToOne
    @JoinColumn(name = "political_organization_id", referencedColumnName = "id", nullable = false)
    private PoliticalOrganizationEntity politicalOrganization;

    @Override
    public String toString() {
        return "StackEntity{" +
                "id=" + id +
                ", decisionNumber='" + decisionNumber + '\'' +
                ", date=" + decisionDate +
                ", date=" + requestDate +
                '}';
    }

    public String convertDecisionDate() {
        if(decisionDate == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(decisionDate);
        return formattedDate;
    }

    public String convertRequestDate() {
        if(requestDate == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(requestDate);
        return formattedDate;
    }

    public String getExpirationDecisionDate() {
        if(decisionDate == null)
            return null;


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(decisionDate);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        java.util.Date date7DaysAhead = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(date7DaysAhead);

        return formattedDate;
    }
}
