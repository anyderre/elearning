package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "languages")
public class LanguageEntity {


    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String locale;

    @Column(name = "ln_key")
    private String key;

    @Column(name = "ln_content")
    private String content;
  
}