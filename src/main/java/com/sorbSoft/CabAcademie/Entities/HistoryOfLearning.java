package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "history_of_learning")
public class HistoryOfLearning implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name="enrollement_id")
    private Enrollement enrollement;
    @OneToOne(optional = false)
    @JoinColumn(name="video_id")
    private Video video;
    private Date timeStamp;
}
