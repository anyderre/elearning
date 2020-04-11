package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "video")
@Where(clause = "deleted = false")
public class Video implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String videoURL;
    @Lob
    private String videoTitle;
    @Lob
    private String attachment;
    private boolean deleted = false;
}
